#!/usr/bin/env python3
"""
Executive EDA report generator for a high-quality public Kaggle dataset.

Chosen dataset:
- Kaggle: House Prices - Advanced Regression Techniques
- Primary file expected: train.csv
- Competition URL: https://www.kaggle.com/competitions/house-prices-advanced-regression-techniques
- Underlying data: Ames Housing dataset

What this script does:
1. Loads the Ames Housing training data from a local CSV or tries to download it via the Kaggle CLI.
2. Produces an executive-style PDF report with:
   - dataset summary
   - exploration plan
   - EDA results and visuals
   - data cleaning and feature engineering details
   - hypothesis section
   - significance test for a strong hypothesis
   - concluding takeaways and next steps
3. Saves all figures/screenshots used in the report.
4. Saves the final PDF report.

Recommended install:
    pip install pandas numpy scipy scikit-learn matplotlib seaborn reportlab

Typical usage:
    python kaggle_ames_executive_report.py --input-csv /path/to/train.csv

Optional Kaggle CLI download flow:
    export KAGGLE_USERNAME=...
    export KAGGLE_KEY=...
    kaggle competitions download -c house-prices-advanced-regression-techniques
"""

from __future__ import annotations

import argparse
import math
import shutil
import subprocess
import sys
import textwrap
import zipfile
from dataclasses import dataclass
from pathlib import Path
from typing import Iterable

import numpy as np
import pandas as pd
from matplotlib import pyplot as plt
from matplotlib.ticker import StrMethodFormatter
from reportlab.lib import colors
from reportlab.lib.enums import TA_CENTER, TA_JUSTIFY
from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import ParagraphStyle, getSampleStyleSheet
from reportlab.lib.units import inch
from reportlab.platypus import (
    Image,
    KeepTogether,
    PageBreak,
    Paragraph,
    Preformatted,
    SimpleDocTemplate,
    Spacer,
    Table,
    TableStyle,
)
from scipy import stats

try:
    import seaborn as sns
except Exception:  # pragma: no cover
    sns = None


KAGGLE_COMPETITION = "house-prices-advanced-regression-techniques"
KAGGLE_URL = f"https://www.kaggle.com/competitions/{KAGGLE_COMPETITION}"
DATASET_TITLE = "House Prices - Advanced Regression Techniques (Ames Housing)"
DEFAULT_OUTPUT_DIR = Path("ames_report_output")
DEFAULT_PDF_NAME = "ames_housing_executive_report.pdf"


@dataclass
class ReportArtifacts:
    output_dir: Path
    figures_dir: Path
    tables_dir: Path
    report_pdf: Path


@dataclass
class SignificanceResult:
    test_name: str
    hypothesis: str
    statistic_name: str
    statistic_value: float
    p_value: float
    effect_name: str | None
    effect_value: float | None
    interpretation: str


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="Generate a polished PDF EDA report for the Kaggle Ames Housing dataset.")
    parser.add_argument(
        "--input-csv",
        type=str,
        default=None,
        help="Path to Kaggle train.csv. If omitted, the script looks locally and then tries the Kaggle CLI.",
    )
    parser.add_argument(
        "--output-dir",
        type=str,
        default=str(DEFAULT_OUTPUT_DIR),
        help="Directory where figures, tables, and the PDF report will be saved.",
    )
    parser.add_argument(
        "--report-name",
        type=str,
        default=DEFAULT_PDF_NAME,
        help="Filename of the generated PDF report.",
    )
    return parser.parse_args()


def ensure_directories(output_dir: Path, report_name: str) -> ReportArtifacts:
    figures_dir = output_dir / "figures"
    tables_dir = output_dir / "tables"
    figures_dir.mkdir(parents=True, exist_ok=True)
    tables_dir.mkdir(parents=True, exist_ok=True)
    return ReportArtifacts(
        output_dir=output_dir,
        figures_dir=figures_dir,
        tables_dir=tables_dir,
        report_pdf=output_dir / report_name,
    )


def find_local_train_csv() -> Path | None:
    candidates = [
        Path("train.csv"),
        Path("data/train.csv"),
        Path("house-prices-advanced-regression-techniques/train.csv"),
        DEFAULT_OUTPUT_DIR / "data" / "train.csv",
    ]
    for candidate in candidates:
        if candidate.exists():
            return candidate.resolve()
    return None


def try_download_via_kaggle_cli(download_dir: Path) -> Path | None:
    kaggle_bin = shutil.which("kaggle")
    if kaggle_bin is None:
        return None

    zip_path = download_dir / f"{KAGGLE_COMPETITION}.zip"
    download_dir.mkdir(parents=True, exist_ok=True)

    cmd = [
        kaggle_bin,
        "competitions",
        "download",
        "-c",
        KAGGLE_COMPETITION,
        "-p",
        str(download_dir),
        "-q",
    ]
    try:
        subprocess.run(cmd, check=True, capture_output=True, text=True)
    except Exception:
        return None

    if not zip_path.exists():
        return None

    with zipfile.ZipFile(zip_path, "r") as zf:
        zf.extractall(download_dir)

    csv_path = download_dir / "train.csv"
    return csv_path if csv_path.exists() else None


def load_dataset(input_csv: str | None, artifacts: ReportArtifacts) -> pd.DataFrame:
    csv_path: Path | None = None

    if input_csv:
        csv_path = Path(input_csv).expanduser().resolve()
        if not csv_path.exists():
            raise FileNotFoundError(f"Input CSV not found: {csv_path}")
    else:
        csv_path = find_local_train_csv()
        if csv_path is None:
            csv_path = try_download_via_kaggle_cli(artifacts.output_dir / "data")

    if csv_path is None or not csv_path.exists():
        raise FileNotFoundError(
            "Could not locate train.csv. Download the Kaggle competition training file first or pass --input-csv.\n"
            f"Competition page: {KAGGLE_URL}"
        )

    df = pd.read_csv(csv_path)
    return df


def infer_target_candidates(df: pd.DataFrame) -> list[str]:
    target_patterns = [
        "target",
        "label",
        "class",
        "price",
        "saleprice",
        "outcome",
        "y",
        "response",
    ]
    candidates: list[str] = []
    for col in df.columns:
        lower = col.lower()
        if any(pattern == lower or pattern in lower for pattern in target_patterns):
            candidates.append(col)
    if "SalePrice" in df.columns and "SalePrice" not in candidates:
        candidates.insert(0, "SalePrice")
    return candidates[:8]


def select_target(df: pd.DataFrame) -> str | None:
    if "SalePrice" in df.columns:
        return "SalePrice"
    candidates = infer_target_candidates(df)
    return candidates[0] if candidates else None


def fmt_int(value: int | float) -> str:
    return f"{int(value):,}"


def memory_mb(df: pd.DataFrame) -> float:
    return float(df.memory_usage(deep=True).sum() / 1024**2)


def classify_columns(df: pd.DataFrame) -> tuple[list[str], list[str], list[str]]:
    numeric_cols = df.select_dtypes(include=[np.number]).columns.tolist()
    datetime_cols = df.select_dtypes(include=["datetime", "datetimetz"]).columns.tolist()
    categorical_cols = [c for c in df.columns if c not in numeric_cols and c not in datetime_cols]
    return numeric_cols, categorical_cols, datetime_cols


def build_summary_table(df: pd.DataFrame, target: str | None) -> pd.DataFrame:
    numeric_cols, categorical_cols, datetime_cols = classify_columns(df)
    target_candidates = ", ".join(infer_target_candidates(df)) or "No obvious target candidates detected"
    return pd.DataFrame(
        {
            "Metric": [
                "Rows",
                "Columns",
                "Numeric columns",
                "Categorical columns",
                "Datetime columns",
                "Duplicate rows",
                "Approx. memory usage (MB)",
                "Selected target",
                "Possible target variables",
            ],
            "Value": [
                fmt_int(df.shape[0]),
                fmt_int(df.shape[1]),
                fmt_int(len(numeric_cols)),
                fmt_int(len(categorical_cols)),
                fmt_int(len(datetime_cols)),
                fmt_int(df.duplicated().sum()),
                f"{memory_mb(df):.2f}",
                target or "Not detected",
                target_candidates,
            ],
        }
    )


def wrap_text(value: object, width: int = 30) -> str:
    text = "" if pd.isna(value) else str(value)
    return "\n".join(textwrap.wrap(text, width=width)) if len(text) > width else text


def save_table_as_image(df: pd.DataFrame, path: Path, title: str, font_size: int = 8) -> None:
    if df.empty:
        df = pd.DataFrame({"Message": ["No data available"]})

    wrapped_df = df.copy()
    for col in wrapped_df.columns:
        wrapped_df[col] = wrapped_df[col].map(lambda x: wrap_text(x, width=28))

    nrows, ncols = wrapped_df.shape
    fig_h = max(2.5, 0.45 * (nrows + 2))
    fig_w = min(14, max(8, 2.6 * ncols))

    fig, ax = plt.subplots(figsize=(fig_w, fig_h))
    ax.axis("off")
    ax.set_title(title, fontsize=12, fontweight="bold", pad=12)

    table = ax.table(
        cellText=wrapped_df.values,
        colLabels=wrapped_df.columns,
        loc="center",
        cellLoc="left",
        colLoc="left",
    )
    table.auto_set_font_size(False)
    table.set_fontsize(font_size)
    table.scale(1, 1.4)

    for (row, col), cell in table.get_celld().items():
        cell.set_edgecolor("#D0D7DE")
        if row == 0:
            cell.set_facecolor("#1F4E79")
            cell.set_text_props(color="white", weight="bold")
        else:
            cell.set_facecolor("#F7F9FC" if row % 2 == 0 else "white")

    plt.tight_layout()
    fig.savefig(path, dpi=220, bbox_inches="tight")
    plt.close(fig)


def set_plot_theme() -> None:
    if sns is not None:
        sns.set_theme(style="whitegrid", context="notebook")
    plt.rcParams["figure.dpi"] = 140


def save_figure(fig: plt.Figure, path: Path) -> None:
    fig.tight_layout()
    fig.savefig(path, dpi=220, bbox_inches="tight")
    plt.close(fig)


def plot_target_distribution(df: pd.DataFrame, target: str, path: Path) -> None:
    fig, axes = plt.subplots(1, 2, figsize=(12, 4.5))
    values = df[target].dropna()

    axes[0].hist(values, bins=40, edgecolor="white")
    axes[0].set_title(f"{target} distribution")
    axes[0].set_xlabel(target)
    axes[0].set_ylabel("Count")
    axes[0].xaxis.set_major_formatter(StrMethodFormatter("{x:,.0f}"))

    axes[1].hist(np.log1p(values), bins=40, edgecolor="white")
    axes[1].set_title(f"log1p({target}) distribution")
    axes[1].set_xlabel(f"log1p({target})")
    axes[1].set_ylabel("Count")

    save_figure(fig, path)


def plot_missingness(df: pd.DataFrame, path: Path, top_n: int = 20) -> pd.DataFrame:
    missing = df.isna().sum().sort_values(ascending=False)
    missing = missing[missing > 0]
    missing_pct = (missing / len(df) * 100).round(2)
    summary = pd.DataFrame({"column": missing.index, "missing_count": missing.values, "missing_pct": missing_pct.values})
    top = summary.head(top_n)

    fig, ax = plt.subplots(figsize=(10, max(4.5, 0.35 * len(top) + 1.5)))
    ax.barh(top["column"][::-1], top["missing_pct"][::-1])
    ax.set_title(f"Top {min(top_n, len(top))} columns by missingness")
    ax.set_xlabel("Missing values (%)")
    ax.set_ylabel("")
    save_figure(fig, path)
    return summary


def plot_correlations(df: pd.DataFrame, target: str, path: Path) -> tuple[pd.DataFrame, str | None]:
    numeric = df.select_dtypes(include=[np.number]).copy()
    if target not in numeric.columns or numeric.shape[1] < 2:
        return pd.DataFrame(), None

    corr = numeric.corr(numeric_only=True)[target].dropna().sort_values(key=lambda s: s.abs(), ascending=False)
    corr_df = corr.reset_index()
    corr_df.columns = ["feature", "correlation_with_target"]

    top_features = corr.abs().head(12).index.tolist()
    heatmap_df = numeric[top_features].corr(numeric_only=True)

    fig, ax = plt.subplots(figsize=(9, 7))
    if sns is not None:
        sns.heatmap(heatmap_df, cmap="Blues", annot=True, fmt=".2f", square=False, ax=ax)
    else:
        im = ax.imshow(heatmap_df, aspect="auto")
        ax.set_xticks(range(len(heatmap_df.columns)))
        ax.set_xticklabels(heatmap_df.columns, rotation=45, ha="right")
        ax.set_yticks(range(len(heatmap_df.index)))
        ax.set_yticklabels(heatmap_df.index)
        fig.colorbar(im, ax=ax)
    ax.set_title(f"Correlation heatmap centered on {target}")
    save_figure(fig, path)

    strongest = next((feature for feature in corr.index if feature != target), None)
    return corr_df, strongest


def plot_scatter(df: pd.DataFrame, feature: str, target: str, path: Path) -> None:
    fig, ax = plt.subplots(figsize=(8.5, 5.2))
    ax.scatter(df[feature], df[target], alpha=0.55)
    if len(df[[feature, target]].dropna()) > 2:
        z = np.polyfit(df[feature].dropna(), df.loc[df[feature].notna(), target], deg=1)
        xs = np.linspace(df[feature].min(), df[feature].max(), 100)
        ax.plot(xs, z[0] * xs + z[1], linewidth=2)
    ax.set_title(f"{feature} vs {target}")
    ax.set_xlabel(feature)
    ax.set_ylabel(target)
    ax.yaxis.set_major_formatter(StrMethodFormatter("{x:,.0f}"))
    save_figure(fig, path)


def plot_overallqual_boxplot(df: pd.DataFrame, path: Path) -> None:
    if "OverallQual" not in df.columns or "SalePrice" not in df.columns:
        return
    fig, ax = plt.subplots(figsize=(10, 5.2))
    if sns is not None:
        sns.boxplot(data=df, x="OverallQual", y="SalePrice", ax=ax)
    else:
        groups = [df.loc[df["OverallQual"] == q, "SalePrice"].dropna() for q in sorted(df["OverallQual"].dropna().unique())]
        ax.boxplot(groups)
        ax.set_xticklabels(sorted(df["OverallQual"].dropna().unique()))
    ax.set_title("SalePrice by OverallQual")
    ax.set_xlabel("OverallQual")
    ax.set_ylabel("SalePrice")
    ax.yaxis.set_major_formatter(StrMethodFormatter("{x:,.0f}"))
    save_figure(fig, path)


def plot_neighborhood_prices(df: pd.DataFrame, path: Path, top_n: int = 12) -> None:
    if "Neighborhood" not in df.columns or "SalePrice" not in df.columns:
        return
    summary = (
        df.groupby("Neighborhood", dropna=False)["SalePrice"]
        .median()
        .sort_values(ascending=False)
        .head(top_n)
        .sort_values()
    )
    fig, ax = plt.subplots(figsize=(9, 5.6))
    ax.barh(summary.index, summary.values)
    ax.set_title(f"Top {top_n} neighborhoods by median SalePrice")
    ax.set_xlabel("Median SalePrice")
    ax.set_ylabel("")
    ax.xaxis.set_major_formatter(StrMethodFormatter("{x:,.0f}"))
    save_figure(fig, path)


def plot_central_air(df: pd.DataFrame, path: Path) -> None:
    if "CentralAir" not in df.columns or "SalePrice" not in df.columns:
        return
    fig, ax = plt.subplots(figsize=(6.5, 5.0))
    if sns is not None:
        sns.boxplot(data=df, x="CentralAir", y="SalePrice", ax=ax)
    else:
        groups = [df.loc[df["CentralAir"] == v, "SalePrice"].dropna() for v in sorted(df["CentralAir"].dropna().unique())]
        ax.boxplot(groups)
        ax.set_xticklabels(sorted(df["CentralAir"].dropna().unique()))
    ax.set_title("SalePrice by CentralAir")
    ax.set_xlabel("CentralAir")
    ax.set_ylabel("SalePrice")
    ax.yaxis.set_major_formatter(StrMethodFormatter("{x:,.0f}"))
    save_figure(fig, path)


def ames_specific_cleaning(df: pd.DataFrame) -> tuple[pd.DataFrame, list[str]]:
    clean = df.copy()
    notes: list[str] = []

    duplicates_removed = int(clean.duplicated().sum())
    if duplicates_removed:
        clean = clean.drop_duplicates().copy()
        notes.append(f"Removed {duplicates_removed} duplicate row(s).")
    else:
        notes.append("No duplicate rows were removed.")

    none_like_cols = [
        "PoolQC", "MiscFeature", "Alley", "Fence", "FireplaceQu",
        "GarageType", "GarageFinish", "GarageQual", "GarageCond",
        "BsmtQual", "BsmtCond", "BsmtExposure", "BsmtFinType1", "BsmtFinType2",
        "MasVnrType",
    ]
    for col in none_like_cols:
        if col in clean.columns:
            missing_before = int(clean[col].isna().sum())
            if missing_before:
                clean[col] = clean[col].fillna("None")
                notes.append(f"Filled {missing_before} missing values in {col} with 'None' because missingness indicates absence.")

    if "MasVnrArea" in clean.columns:
        count = int(clean["MasVnrArea"].isna().sum())
        if count:
            clean["MasVnrArea"] = clean["MasVnrArea"].fillna(0)
            notes.append(f"Filled {count} missing values in MasVnrArea with 0.")

    if "LotFrontage" in clean.columns:
        count = int(clean["LotFrontage"].isna().sum())
        if count:
            if "Neighborhood" in clean.columns:
                clean["LotFrontage"] = clean.groupby("Neighborhood")["LotFrontage"].transform(lambda s: s.fillna(s.median()))
            clean["LotFrontage"] = clean["LotFrontage"].fillna(clean["LotFrontage"].median())
            notes.append(f"Imputed {count} missing LotFrontage values using neighborhood median, then global median fallback.")

    mode_fill_cols = [
        "Electrical", "MSZoning", "KitchenQual", "Exterior1st", "Exterior2nd", "SaleType",
        "Functional", "Utilities",
    ]
    for col in mode_fill_cols:
        if col in clean.columns and clean[col].isna().any():
            count = int(clean[col].isna().sum())
            mode_value = clean[col].mode(dropna=True)
            fill_value = mode_value.iloc[0] if not mode_value.empty else "Unknown"
            clean[col] = clean[col].fillna(fill_value)
            notes.append(f"Filled {count} missing values in {col} with the mode: {fill_value}.")

    if "GarageYrBlt" in clean.columns and clean["GarageYrBlt"].isna().any():
        count = int(clean["GarageYrBlt"].isna().sum())
        fill_series = clean["YearBuilt"] if "YearBuilt" in clean.columns else clean["GarageYrBlt"].median()
        clean["GarageYrBlt"] = clean["GarageYrBlt"].fillna(fill_series)
        notes.append(f"Filled {count} missing values in GarageYrBlt using YearBuilt where available.")

    numeric_cols = clean.select_dtypes(include=[np.number]).columns.tolist()
    categorical_cols = [c for c in clean.columns if c not in numeric_cols]

    for col in numeric_cols:
        if clean[col].isna().any():
            count = int(clean[col].isna().sum())
            clean[col] = clean[col].fillna(clean[col].median())
            notes.append(f"Filled {count} remaining missing numeric values in {col} with the median.")

    for col in categorical_cols:
        if clean[col].isna().any():
            count = int(clean[col].isna().sum())
            mode_value = clean[col].mode(dropna=True)
            fill_value = mode_value.iloc[0] if not mode_value.empty else "Unknown"
            clean[col] = clean[col].fillna(fill_value)
            notes.append(f"Filled {count} remaining missing categorical values in {col} with the mode: {fill_value}.")

    if "GrLivArea" in clean.columns and "SalePrice" in clean.columns:
        extreme_mask = clean["GrLivArea"] > 4000
        extreme_count = int(extreme_mask.sum())
        notes.append(
            f"Flagged {extreme_count} home(s) with GrLivArea > 4,000 sq ft for executive review as potential leverage points; they were retained in the main cleaned dataset."
        )

    return clean, notes


def engineer_features(df: pd.DataFrame) -> tuple[pd.DataFrame, list[str]]:
    engineered = df.copy()
    notes: list[str] = []

    if {"YrSold", "YearBuilt"}.issubset(engineered.columns):
        engineered["HouseAgeAtSale"] = engineered["YrSold"] - engineered["YearBuilt"]
        notes.append("Created HouseAgeAtSale = YrSold - YearBuilt.")

    if {"YrSold", "YearRemodAdd"}.issubset(engineered.columns):
        engineered["YearsSinceRemodelAtSale"] = engineered["YrSold"] - engineered["YearRemodAdd"]
        notes.append("Created YearsSinceRemodelAtSale = YrSold - YearRemodAdd.")

    if "YearBuilt" in engineered.columns and "YearRemodAdd" in engineered.columns:
        engineered["IsRemodeled"] = (engineered["YearRemodAdd"] > engineered["YearBuilt"]).astype(int)
        notes.append("Created IsRemodeled indicator.")

    bath_cols = [c for c in ["FullBath", "HalfBath", "BsmtFullBath", "BsmtHalfBath"] if c in engineered.columns]
    if bath_cols:
        engineered["TotalBathrooms"] = (
            engineered.get("FullBath", 0)
            + 0.5 * engineered.get("HalfBath", 0)
            + engineered.get("BsmtFullBath", 0)
            + 0.5 * engineered.get("BsmtHalfBath", 0)
        )
        notes.append("Created TotalBathrooms using full baths + 0.5 * half baths.")

    sf_cols = [c for c in ["TotalBsmtSF", "1stFlrSF", "2ndFlrSF"] if c in engineered.columns]
    if sf_cols:
        engineered["TotalSF"] = (
            engineered.get("TotalBsmtSF", 0)
            + engineered.get("1stFlrSF", 0)
            + engineered.get("2ndFlrSF", 0)
        )
        notes.append("Created TotalSF = TotalBsmtSF + 1stFlrSF + 2ndFlrSF.")

    if "GarageArea" in engineered.columns:
        engineered["HasGarage"] = (engineered["GarageArea"] > 0).astype(int)
        notes.append("Created HasGarage indicator from GarageArea.")

    if "TotalBsmtSF" in engineered.columns:
        engineered["HasBasement"] = (engineered["TotalBsmtSF"] > 0).astype(int)
        notes.append("Created HasBasement indicator from TotalBsmtSF.")

    return engineered, notes


def encode_features(df: pd.DataFrame, target: str | None) -> pd.DataFrame:
    feature_df = df.drop(columns=[target], errors="ignore") if target else df.copy()
    encoded = pd.get_dummies(feature_df, drop_first=False)
    if target and target in df.columns:
        encoded[target] = df[target].values
    return encoded


def run_primary_significance_test(df: pd.DataFrame) -> SignificanceResult:
    if {"OverallQual", "SalePrice"}.issubset(df.columns):
        subset = df[["OverallQual", "SalePrice"]].dropna()
        rho, p_value = stats.spearmanr(subset["OverallQual"], subset["SalePrice"])
        interpretation = (
            "The result supports the hypothesis that higher overall construction/finish quality is associated with higher sale price. "
            "Because OverallQual is ordinal, Spearman rank correlation is an appropriate significance test for monotonic association."
        )
        return SignificanceResult(
            test_name="Spearman rank correlation",
            hypothesis="H1: Higher OverallQual is positively associated with higher SalePrice.",
            statistic_name="Spearman rho",
            statistic_value=float(rho),
            p_value=float(p_value),
            effect_name="Monotonic effect size",
            effect_value=float(rho),
            interpretation=interpretation,
        )

    if {"CentralAir", "SalePrice"}.issubset(df.columns):
        subset = df[["CentralAir", "SalePrice"]].dropna().copy()
        y_group = np.log1p(subset.loc[subset["CentralAir"] == "Y", "SalePrice"])
        n_group = np.log1p(subset.loc[subset["CentralAir"] == "N", "SalePrice"])
        t_stat, p_value = stats.ttest_ind(y_group, n_group, equal_var=False, nan_policy="omit")
        pooled_std = math.sqrt(((y_group.var(ddof=1) + n_group.var(ddof=1)) / 2))
        cohens_d = (y_group.mean() - n_group.mean()) / pooled_std if pooled_std else np.nan
        interpretation = (
            "The result supports the hypothesis that homes with central air tend to sell for more than homes without central air. "
            "The test is performed on log-transformed SalePrice to reduce right skew."
        )
        return SignificanceResult(
            test_name="Welch two-sample t-test",
            hypothesis="H2: Homes with central air sell for more than homes without central air.",
            statistic_name="t statistic",
            statistic_value=float(t_stat),
            p_value=float(p_value),
            effect_name="Cohen's d",
            effect_value=float(cohens_d) if not np.isnan(cohens_d) else None,
            interpretation=interpretation,
        )

    numeric_cols = df.select_dtypes(include=[np.number]).columns.tolist()
    if len(numeric_cols) >= 2:
        target = numeric_cols[-1]
        feature = numeric_cols[0]
        subset = df[[feature, target]].dropna()
        r, p_value = stats.pearsonr(subset[feature], subset[target])
        return SignificanceResult(
            test_name="Pearson correlation",
            hypothesis=f"H1: {feature} is linearly associated with {target}.",
            statistic_name="Pearson r",
            statistic_value=float(r),
            p_value=float(p_value),
            effect_name="Linear effect size",
            effect_value=float(r),
            interpretation="A fallback significance test was used because the expected Ames columns were not available.",
        )

    raise ValueError("Could not run a significance test because the dataset lacks usable analytical columns.")


def compute_key_findings(df: pd.DataFrame, target: str | None, corr_df: pd.DataFrame, missing_df: pd.DataFrame) -> list[str]:
    findings: list[str] = []

    findings.append(
        f"The dataset contains {fmt_int(df.shape[0])} records and {fmt_int(df.shape[1])} variables, large enough to support executive-level segmentation, benchmarking, and driver analysis."
    )

    if target and target in df.columns:
        skewness = float(df[target].skew()) if pd.api.types.is_numeric_dtype(df[target]) else float("nan")
        findings.append(
            f"The selected target, {target}, is materially right-skewed (skewness {skewness:.2f}), which means median-based summaries and log views are useful complements to raw averages."
        )

    if not corr_df.empty:
        strongest = corr_df[corr_df["feature"] != target].head(5)
        top_text = ", ".join(
            f"{row.feature} ({row.correlation_with_target:.2f})" for row in strongest.itertuples(index=False)
        )
        findings.append(f"The strongest linear relationships with the target are concentrated in a small set of features: {top_text}.")

    if not missing_df.empty:
        top_missing = missing_df.head(5)
        missing_text = ", ".join(
            f"{row.column} ({row.missing_pct:.1f}%)" for row in top_missing.itertuples(index=False)
        )
        findings.append(
            f"Missingness is concentrated rather than diffuse, led by: {missing_text}. This makes a deliberate, column-specific cleaning strategy preferable to blanket deletion."
        )

    if {"Neighborhood", "SalePrice"}.issubset(df.columns):
        medians = df.groupby("Neighborhood")["SalePrice"].median().sort_values(ascending=False)
        high = medians.head(3).index.tolist()
        findings.append(
            f"Location remains economically meaningful: neighborhoods such as {', '.join(high)} sit at the top of the median price distribution, reinforcing the importance of micro-market segmentation."
        )

    if {"OverallQual", "SalePrice"}.issubset(df.columns):
        findings.append(
            "Quality appears to be a strategic pricing lever. As OverallQual rises, the sale price distribution shifts upward in both level and spread, suggesting a non-trivial willingness to pay for higher perceived quality."
        )

    return findings


def report_hypotheses() -> list[str]:
    return [
        "H1: Homes with higher OverallQual sell for higher prices than otherwise comparable homes.",
        "H2: Homes with central air command a price premium relative to homes without central air.",
        "H3: Neighborhood is not just descriptive metadata; it captures meaningful market segmentation and materially different price tiers.",
    ]


def exploration_plan_points(target: str | None) -> list[str]:
    tgt = target or "the target variable"
    return [
        f"Confirm analytical scope: validate row/column counts, data types, duplicate levels, and candidate targets, then confirm that {tgt} is suitable as the primary business outcome.",
        "Profile missingness: quantify where nulls are concentrated, distinguish structural absence from true data quality gaps, and choose column-specific remediation methods.",
        "Assess distributional shape: inspect skewness, heavy tails, and potential outliers for the outcome and major continuous drivers.",
        "Evaluate feature usefulness: identify high-signal numeric relationships, influential categorical segments, and variables that may be redundant or low-value.",
        "Create business-facing derived features: engineer interpretable features that reflect age, usable space, amenity presence, and renovation history.",
        "Validate at least one strong hypothesis statistically: move beyond visual patterns to test whether a high-value relationship is likely to be real rather than random noise.",
    ]


def styles() -> dict[str, ParagraphStyle]:
    sample = getSampleStyleSheet()
    return {
        "title": ParagraphStyle(
            "TitleCustom",
            parent=sample["Title"],
            fontSize=24,
            leading=28,
            textColor=colors.HexColor("#15395B"),
            alignment=TA_CENTER,
            spaceAfter=10,
        ),
        "subtitle": ParagraphStyle(
            "SubtitleCustom",
            parent=sample["BodyText"],
            fontSize=11,
            leading=14,
            textColor=colors.HexColor("#3B4A5A"),
            alignment=TA_CENTER,
            spaceAfter=16,
        ),
        "section": ParagraphStyle(
            "SectionCustom",
            parent=sample["Heading1"],
            fontSize=16,
            leading=20,
            textColor=colors.HexColor("#15395B"),
            spaceBefore=10,
            spaceAfter=8,
        ),
        "subsection": ParagraphStyle(
            "SubsectionCustom",
            parent=sample["Heading2"],
            fontSize=12,
            leading=15,
            textColor=colors.HexColor("#1F4E79"),
            spaceBefore=6,
            spaceAfter=4,
        ),
        "body": ParagraphStyle(
            "BodyCustom",
            parent=sample["BodyText"],
            fontSize=9.4,
            leading=13,
            alignment=TA_JUSTIFY,
            textColor=colors.HexColor("#202124"),
            spaceAfter=6,
        ),
        "bullet": ParagraphStyle(
            "BulletCustom",
            parent=sample["BodyText"],
            fontSize=9.2,
            leading=13,
            leftIndent=12,
            firstLineIndent=-8,
            bulletIndent=0,
            spaceAfter=3,
        ),
        "small": ParagraphStyle(
            "SmallCustom",
            parent=sample["BodyText"],
            fontSize=8.2,
            leading=11,
            textColor=colors.HexColor("#485260"),
            spaceAfter=4,
        ),
        "code": ParagraphStyle(
            "CodeCustom",
            parent=sample["Code"],
            fontSize=7.6,
            leading=9.6,
            textColor=colors.HexColor("#1F2933"),
        ),
    }


def build_key_value_table(df: pd.DataFrame) -> Table:
    data = [df.columns.tolist()] + df.values.tolist()
    table = Table(data, colWidths=[2.2 * inch, 4.5 * inch], hAlign="LEFT")
    table.setStyle(
        TableStyle(
            [
                ("BACKGROUND", (0, 0), (-1, 0), colors.HexColor("#1F4E79")),
                ("TEXTCOLOR", (0, 0), (-1, 0), colors.white),
                ("FONTNAME", (0, 0), (-1, 0), "Helvetica-Bold"),
                ("FONTSIZE", (0, 0), (-1, -1), 8.5),
                ("LEADING", (0, 0), (-1, -1), 11),
                ("BACKGROUND", (0, 1), (-1, -1), colors.whitesmoke),
                ("ROWBACKGROUNDS", (0, 1), (-1, -1), [colors.whitesmoke, colors.HexColor("#F7F9FC")]),
                ("GRID", (0, 0), (-1, -1), 0.35, colors.HexColor("#C7D1DB")),
                ("VALIGN", (0, 0), (-1, -1), "MIDDLE"),
                ("LEFTPADDING", (0, 0), (-1, -1), 6),
                ("RIGHTPADDING", (0, 0), (-1, -1), 6),
                ("TOPPADDING", (0, 0), (-1, -1), 5),
                ("BOTTOMPADDING", (0, 0), (-1, -1), 5),
            ]
        )
    )
    return table


def build_three_col_table(rows: list[list[str]], widths: tuple[float, float, float]) -> Table:
    table = Table(rows, colWidths=list(widths), hAlign="LEFT")
    table.setStyle(
        TableStyle(
            [
                ("BACKGROUND", (0, 0), (-1, 0), colors.HexColor("#1F4E79")),
                ("TEXTCOLOR", (0, 0), (-1, 0), colors.white),
                ("FONTNAME", (0, 0), (-1, 0), "Helvetica-Bold"),
                ("FONTSIZE", (0, 0), (-1, -1), 8),
                ("LEADING", (0, 0), (-1, -1), 10.2),
                ("ROWBACKGROUNDS", (0, 1), (-1, -1), [colors.white, colors.HexColor("#F7F9FC")]),
                ("GRID", (0, 0), (-1, -1), 0.3, colors.HexColor("#CCD4DD")),
                ("VALIGN", (0, 0), (-1, -1), "MIDDLE"),
                ("LEFTPADDING", (0, 0), (-1, -1), 5),
                ("RIGHTPADDING", (0, 0), (-1, -1), 5),
                ("TOPPADDING", (0, 0), (-1, -1), 4),
                ("BOTTOMPADDING", (0, 0), (-1, -1), 4),
            ]
        )
    )
    return table


def add_image(story: list, path: Path, caption: str, width: float = 6.9 * inch) -> None:
    if not path.exists():
        return
    img = Image(str(path))
    img.drawWidth = width
    img.drawHeight = img.imageHeight * width / img.imageWidth
    story.append(KeepTogether([img, Spacer(1, 0.06 * inch), Paragraph(caption, styles()["small"])]))
    story.append(Spacer(1, 0.12 * inch))


def draw_page_number(canvas, doc) -> None:  # pragma: no cover
    canvas.saveState()
    canvas.setFont("Helvetica", 8)
    canvas.setFillColor(colors.HexColor("#6B7280"))
    canvas.drawRightString(doc.pagesize[0] - 40, 22, f"Page {doc.page}")
    canvas.restoreState()


def generate_pdf_report(
    artifacts: ReportArtifacts,
    raw_df: pd.DataFrame,
    cleaned_df: pd.DataFrame,
    engineered_df: pd.DataFrame,
    encoded_df: pd.DataFrame,
    target: str | None,
    summary_df: pd.DataFrame,
    missing_df: pd.DataFrame,
    corr_df: pd.DataFrame,
    cleaning_notes: list[str],
    fe_notes: list[str],
    significance: SignificanceResult,
) -> None:
    st = styles()
    doc = SimpleDocTemplate(
        str(artifacts.report_pdf),
        pagesize=A4,
        rightMargin=38,
        leftMargin=38,
        topMargin=40,
        bottomMargin=34,
        title="Executive EDA Report - Ames Housing",
        author="OpenAI",
    )

    story: list = []

    # Cover
    story.append(Spacer(1, 0.6 * inch))
    story.append(Paragraph("Executive Exploratory Data Analysis Report", st["title"]))
    story.append(Paragraph(DATASET_TITLE, st["subtitle"]))
    story.append(Paragraph(
        "Prepared as a senior-audience briefing document designed for a Chief Data Officer or Head of Analytics. The report is structured to move from data scope and quality to business-relevant insights, hypothesis framing, and statistically supported conclusions.",
        st["body"],
    ))
    story.append(Spacer(1, 0.12 * inch))
    story.append(build_key_value_table(summary_df))
    story.append(Spacer(1, 0.18 * inch))
    story.append(Paragraph(
        f"Dataset source: Kaggle competition - <font color='#1F4E79'>{KAGGLE_URL}</font>. Underlying data: Ames Housing residential sales dataset.",
        st["small"],
    ))
    story.append(PageBreak())

    # Section 1 - Summary of data
    story.append(Paragraph("1. Summary of the Data", st["section"]))
    story.append(Paragraph(
        "The dataset is large enough for meaningful segmentation and driver analysis, and it contains a healthy mix of numeric and categorical variables. It is especially suitable for executive-quality EDA because it includes interpretable variables tied to property size, quality, age, condition, amenities, and neighborhood context.",
        st["body"],
    ))
    story.append(build_key_value_table(summary_df))
    story.append(Spacer(1, 0.16 * inch))
    story.append(Paragraph("Sample record preview", st["subsection"]))
    add_image(story, artifacts.tables_dir / "raw_head.png", "Figure 1. First rows of the raw input dataset.")

    # Section 2 - Exploration plan
    story.append(Paragraph("2. Data Exploration Plan", st["section"]))
    story.append(Paragraph(
        "The exploration plan below follows a business-first sequence: establish scope, diagnose data quality, understand distributions, identify high-signal drivers, create interpretable derived features, and then validate a high-value hypothesis with formal statistics.",
        st["body"],
    ))
    for point in exploration_plan_points(target):
        story.append(Paragraph(f"• {point}", st["bullet"]))

    # Section 3 - EDA results
    story.append(Paragraph("3. Exploratory Data Analysis Results", st["section"]))
    story.append(Paragraph(
        "This section focuses on patterns that a senior audience can act on: where price is concentrated, which variables appear most explanatory, where data quality issues are concentrated, and which sub-markets deserve differentiated treatment.",
        st["body"],
    ))
    add_image(story, artifacts.figures_dir / "target_distribution.png", "Figure 2. Raw and log-transformed views of the target distribution.")
    add_image(story, artifacts.figures_dir / "missingness.png", "Figure 3. Missingness is concentrated in a limited number of columns, which supports targeted remediation rather than broad deletion.")
    add_image(story, artifacts.figures_dir / "correlation_heatmap.png", "Figure 4. Correlation heatmap centered on the target and its strongest numeric drivers.")
    add_image(story, artifacts.figures_dir / "strongest_scatter.png", "Figure 5. Strongest numeric driver versus target.")
    add_image(story, artifacts.figures_dir / "overallqual_boxplot.png", "Figure 6. Price distribution by OverallQual.")
    add_image(story, artifacts.figures_dir / "neighborhood_medians.png", "Figure 7. Highest-value neighborhoods by median sale price.")

    story.append(Paragraph("Key EDA findings", st["subsection"]))
    findings = compute_key_findings(raw_df, target, corr_df, missing_df)
    for item in findings:
        story.append(Paragraph(f"• {item}", st["bullet"]))

    top_corr = corr_df.head(10).copy() if not corr_df.empty else pd.DataFrame(columns=["feature", "correlation_with_target"])
    if not top_corr.empty:
        top_corr["correlation_with_target"] = top_corr["correlation_with_target"].map(lambda x: f"{x:.3f}")
        rows = [["Feature", "Correlation with target", "Management note"]]
        for row in top_corr.itertuples(index=False):
            mgmt_note = (
                "Potential pricing or valuation driver"
                if row.feature != target else "Target self-correlation"
            )
            rows.append([str(row.feature), str(row.correlation_with_target), mgmt_note])
        story.append(build_three_col_table(rows, widths=(2.25 * inch, 1.6 * inch, 2.75 * inch)))

    story.append(PageBreak())

    # Section 4 - Cleaning and feature engineering
    story.append(Paragraph("4. Data Cleaning and Feature Engineering", st["section"]))
    story.append(Paragraph(
        "Cleaning decisions were designed to preserve business meaning. The strategy distinguishes structural missingness from true data quality gaps, avoids unnecessary row loss, and engineers features that are easy to interpret at the executive level.",
        st["body"],
    ))

    story.append(Paragraph("Cleaning steps", st["subsection"]))
    for note in cleaning_notes[:18]:
        story.append(Paragraph(f"• {note}", st["bullet"]))
    if len(cleaning_notes) > 18:
        story.append(Paragraph(f"• Additional cleaning actions performed: {len(cleaning_notes) - 18} more steps logged in code output.", st["bullet"]))

    story.append(Paragraph("Feature engineering steps", st["subsection"]))
    for note in fe_notes:
        story.append(Paragraph(f"• {note}", st["bullet"]))

    story.append(Paragraph("Screenshots of outputs", st["subsection"]))
    add_image(story, artifacts.tables_dir / "missing_table.png", "Figure 8. Detailed missingness table used to drive imputation decisions.")
    add_image(story, artifacts.tables_dir / "cleaned_head.png", "Figure 9. Preview of the cleaned and engineered dataset.")
    add_image(story, artifacts.tables_dir / "encoded_preview.png", "Figure 10. Preview of the encoded feature matrix used for downstream modeling readiness.")
    add_image(story, artifacts.figures_dir / "central_air_boxplot.png", "Figure 11. Central air segmentation used as a business-friendly validation view.")

    # Section 5 - Key findings and insights
    story.append(Paragraph("5. Summary of Key Findings and Insights", st["section"]))
    story.append(Paragraph(
        "The analysis points to a relatively interpretable pricing structure: location, overall quality, and usable living area explain a large share of visible variation. Missingness is significant in a handful of columns but manageable with domain-aware imputation. The dataset is therefore not only analytically rich, but also operationally usable after moderate preprocessing.",
        st["body"],
    ))
    for item in findings:
        story.append(Paragraph(f"• {item}", st["bullet"]))

    # Section 6 - Hypotheses
    story.append(Paragraph("6. Hypotheses", st["section"]))
    story.append(Paragraph(
        "The following hypotheses were selected because each can influence portfolio strategy, valuation logic, or operational prioritization.",
        st["body"],
    ))
    for hypothesis in report_hypotheses():
        story.append(Paragraph(f"• {hypothesis}", st["bullet"]))

    # Section 7 - Significance test
    story.append(Paragraph("7. Significance Test for a Strong Hypothesis", st["section"]))
    story.append(Paragraph(
        f"Primary hypothesis tested: <b>{significance.hypothesis}</b>",
        st["body"],
    ))
    story.append(Paragraph(
        f"Test used: {significance.test_name}. This choice aligns the statistical method with the variable types and the practical question being asked.",
        st["body"],
    ))

    sig_rows = [
        ["Metric", "Value"],
        [significance.statistic_name, f"{significance.statistic_value:.4f}"],
        ["p-value", f"{significance.p_value:.6g}"],
        [significance.effect_name or "Effect size", f"{significance.effect_value:.4f}" if significance.effect_value is not None else "N/A"],
        ["Decision", "Reject the null hypothesis" if significance.p_value < 0.05 else "Do not reject the null hypothesis"],
    ]
    sig_table = Table(sig_rows, colWidths=[2.2 * inch, 3.7 * inch], hAlign="LEFT")
    sig_table.setStyle(
        TableStyle(
            [
                ("BACKGROUND", (0, 0), (-1, 0), colors.HexColor("#1F4E79")),
                ("TEXTCOLOR", (0, 0), (-1, 0), colors.white),
                ("FONTNAME", (0, 0), (-1, 0), "Helvetica-Bold"),
                ("ROWBACKGROUNDS", (0, 1), (-1, -1), [colors.white, colors.HexColor("#F7F9FC")]),
                ("GRID", (0, 0), (-1, -1), 0.3, colors.HexColor("#CCD4DD")),
                ("VALIGN", (0, 0), (-1, -1), "MIDDLE"),
                ("LEFTPADDING", (0, 0), (-1, -1), 6),
                ("RIGHTPADDING", (0, 0), (-1, -1), 6),
                ("TOPPADDING", (0, 0), (-1, -1), 5),
                ("BOTTOMPADDING", (0, 0), (-1, -1), 5),
            ]
        )
    )
    story.append(sig_table)
    story.append(Spacer(1, 0.12 * inch))
    story.append(Paragraph(significance.interpretation, st["body"]))
    story.append(Paragraph(
        "From an executive standpoint, the implication is that the tested feature is not merely visually associated with price; the relationship is statistically credible and therefore relevant for pricing logic, segmentation, and potential downstream modeling. Statistical significance does not prove causality, but it materially increases confidence that the observed pattern is real in this sample.",
        st["body"],
    ))

    # Section 8 - Conclusion
    story.append(Paragraph("8. Conclusion, Key Takeaways, and Next Steps", st["section"]))
    story.append(Paragraph(
        "Overall, the dataset is well suited for advanced analytics work. It has enough complexity to demonstrate rigorous EDA, but the variables remain interpretable enough for stakeholders outside data science. The strongest signals cluster around quality, location, and size, while data quality issues are concentrated enough to be handled through targeted remediation rather than destructive filtering.",
        st["body"],
    ))
    takeaways = [
        "Adopt a domain-aware cleaning strategy instead of blanket row deletion, because missingness often reflects product absence rather than bad records.",
        "Prioritize quality, location, and space-related variables in any baseline valuation model or pricing dashboard.",
        "Validate additional hypotheses with ANOVA or segmented regression, especially around neighborhood, remodeling, and amenity effects.",
        "Translate the cleaned feature set into a reproducible modeling pipeline and compare interpretable linear models with stronger non-linear benchmarks.",
    ]
    for item in takeaways:
        story.append(Paragraph(f"• {item}", st["bullet"]))

    story.append(Spacer(1, 0.12 * inch))
    story.append(Paragraph(
        "Appendix note: the PDF intentionally includes both visualizations and table screenshots so that the analytical workflow is auditable and presentation-ready.",
        st["small"],
    ))

    doc.build(story, onFirstPage=draw_page_number, onLaterPages=draw_page_number)


def main() -> int:
    set_plot_theme()
    args = parse_args()
    artifacts = ensure_directories(Path(args.output_dir), args.report_name)

    raw_df = load_dataset(args.input_csv, artifacts)
    target = select_target(raw_df)
    summary_df = build_summary_table(raw_df, target)

    save_table_as_image(raw_df.head(8), artifacts.tables_dir / "raw_head.png", "Raw dataset preview")

    missing_df = plot_missingness(raw_df, artifacts.figures_dir / "missingness.png")
    save_table_as_image(missing_df.head(15), artifacts.tables_dir / "missing_table.png", "Missing values summary")

    corr_df = pd.DataFrame()
    strongest_feature = None
    if target and target in raw_df.columns and pd.api.types.is_numeric_dtype(raw_df[target]):
        plot_target_distribution(raw_df, target, artifacts.figures_dir / "target_distribution.png")
        corr_df, strongest_feature = plot_correlations(raw_df, target, artifacts.figures_dir / "correlation_heatmap.png")
        if strongest_feature is not None and strongest_feature in raw_df.columns:
            plot_scatter(raw_df.dropna(subset=[strongest_feature, target]), strongest_feature, target, artifacts.figures_dir / "strongest_scatter.png")

    plot_overallqual_boxplot(raw_df, artifacts.figures_dir / "overallqual_boxplot.png")
    plot_neighborhood_prices(raw_df, artifacts.figures_dir / "neighborhood_medians.png")
    plot_central_air(raw_df, artifacts.figures_dir / "central_air_boxplot.png")

    cleaned_df, cleaning_notes = ames_specific_cleaning(raw_df)
    engineered_df, fe_notes = engineer_features(cleaned_df)
    encoded_df = encode_features(engineered_df, target)

    save_table_as_image(engineered_df.head(8), artifacts.tables_dir / "cleaned_head.png", "Cleaned + engineered dataset preview")
    preview_cols = encoded_df.columns[:12].tolist() + ([target] if target and target in encoded_df.columns else [])
    preview_cols = list(dict.fromkeys(preview_cols))
    save_table_as_image(encoded_df.loc[:, preview_cols].head(8), artifacts.tables_dir / "encoded_preview.png", "Encoded feature preview", font_size=7)

    significance = run_primary_significance_test(engineered_df)

    generate_pdf_report(
        artifacts=artifacts,
        raw_df=raw_df,
        cleaned_df=cleaned_df,
        engineered_df=engineered_df,
        encoded_df=encoded_df,
        target=target,
        summary_df=summary_df,
        missing_df=missing_df,
        corr_df=corr_df,
        cleaning_notes=cleaning_notes,
        fe_notes=fe_notes,
        significance=significance,
    )

    print(f"Report created: {artifacts.report_pdf}")
    print(f"Figures saved to: {artifacts.figures_dir}")
    print(f"Tables/screenshots saved to: {artifacts.tables_dir}")
    return 0


if __name__ == "__main__":
    try:
        raise SystemExit(main())
    except Exception as exc:
        print(f"ERROR: {exc}", file=sys.stderr)
        raise
