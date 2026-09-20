# Data Exploration

## Data Set Overview

*Note on discrepancy*: The assignment template expects 9 files in the dataset overview table, but the repository contains only **8** CSV files. The following table describes the 8 actual CSV files present in the dataset.

| Filename | Description | Fields & Descriptions |
| --- | --- | --- |
| `ad-clicks.csv` | Represents individual ad clicks made by users within the game. | `timestamp`: Time of click<br>`txId`: Transaction ID<br>`userSessionId`: Session identifier<br>`teamId`: User's team ID<br>`userId`: User identifier<br>`adId`: Advertisement identifier<br>`adCategory`: Category of the ad |
| `buy-clicks.csv` | Records in-game microtransaction purchases made by users. | `timestamp`: Time of purchase<br>`txId`: Transaction ID<br>`userSessionId`: Session identifier<br>`team`: User's team ID<br>`userId`: User identifier<br>`buyId`: Item identifier purchased<br>`price`: Cost of the item |
| `game-clicks.csv` | Logs the gameplay clicks made by users and whether they hit the target. | `timestamp`: Time of click<br>`clickId`: Unique click identifier<br>`userId`: User identifier<br>`userSessionId`: Session identifier<br>`isHit`: Boolean/integer indicating if the click was a hit (1) or miss (0)<br>`teamId`: User's team ID<br>`teamLevel`: Team's current level |
| `level-events.csv` | Tracks events related to team level progression (start and end). | `timestamp`: Time of event<br>`eventId`: Event identifier<br>`teamId`: Team identifier<br>`teamLevel`: Level being played<br>`eventType`: Start or end of level |
| `team-assignments.csv` | Maps users to the teams they are assigned to. | `timestamp`: Time of assignment<br>`team`: Team identifier<br>`userId`: User identifier<br>`assignmentId`: Assignment identifier |
| `team.csv` | Contains metadata and state information for each team. | `teamId`: Team identifier<br>`name`: Team name<br>`teamCreationTime`: Timestamp of creation<br>`teamEndTime`: Timestamp when team ended<br>`strength`: Team strength metric<br>`currentLevel`: Team's current progression level |
| `user-session.csv` | Logs the start and end of user gameplay sessions and their platform. | `timestamp`: Time of session event<br>`userSessionId`: Session identifier<br>`userId`: User identifier<br>`teamId`: User's team ID<br>`assignmentId`: Assignment identifier<br>`sessionType`: Event type (start/end)<br>`teamLevel`: Team's level during session<br>`platformType`: OS/Platform used |
| `users.csv` | Contains demographic and profile metadata for registered users. | `timestamp`: Time of registration<br>`userId`: User identifier<br>`nick`: User's nickname<br>`twitter`: User's Twitter handle<br>`dob`: Date of birth<br>`country`: Country of residence |

---

# Aggregation

* **Amount spent buying items:** $21,407.00
* **Number of unique items available to purchase:** 6

## A. Number of purchases by item

| buyId | Purchase count |
| :---: | :---: |
| 0 | 592 |
| 1 | 269 |
| 2 | 714 |
| 3 | 337 |
| 4 | 425 |
| 5 | 610 |

* **Recommended Chart Title:** Number of Purchases by Item
* **Recommended X-axis Label:** Item ID (buyId)
* **Recommended Y-axis Label:** Number of Purchases
* **Reproducibility Note:** Computed in Python using pandas: `df["buyId"].value_counts().sort_index()` on `buy-clicks.csv`.
* **Chart Filename:** `chart_purchase_count_by_item.png`

## B. Money spent by item

| buyId | Total amount spent |
| :---: | :---: |
| 0 | $592.00 |
| 1 | $538.00 |
| 2 | $2,142.00 |
| 3 | $1,685.00 |
| 4 | $4,250.00 |
| 5 | $12,200.00 |

* **Recommended Chart Title:** Total Revenue by Item
* **Recommended X-axis Label:** Item ID (buyId)
* **Recommended Y-axis Label:** Total Revenue ($)
* **Reproducibility Note:** Computed in Python using pandas: `df.groupby("buyId")["price"].sum().sort_index()` on `buy-clicks.csv`.
* **Chart Filename:** `chart_revenue_by_item.png`

---

# Filtering

## A. Top ten users by total spending

| Rank | User ID | Total amount spent |
| :---: | :---: | :---: |
| 1 | 2229 | $223.00 |
| 2 | 12 | $215.00 |
| 3 | 471 | $202.00 |
| 4 | 511 | $200.00 |
| 5 | 1027 | $189.00 |
| 6 | 670 | $183.00 |
| 7 | 1260 | $183.00 |
| 8 | 352 | $180.00 |
| 9 | 1697 | $172.00 |
| 10 | 1732 | $172.00 |

* **Recommended Chart Title:** Total Spending by Top 10 Users
* **Recommended X-axis Label:** User ID
* **Recommended Y-axis Label:** Total Spending ($)
* **Reproducibility Note:** Grouped `buy-clicks.csv` by `userId`, summed `price`, sorted descending, and selected the top 10 rows.
* **Chart Filename:** `chart_top_10_users_by_spending.png`

## B. Top three buying users: platform and hit ratio

**Logic for Platform:** Determined by taking the distinct `platformType` for the specific `userId` in `user-session.csv`. All top three users exclusively used `iphone`.
**Logic for Hit Ratio:** Sourced from `game-clicks.csv`. Filtered for the user, summed `isHit` to get total hits, counted total rows for total clicks, and computed `(hits / clicks) * 100`.

**Unrounded hit-ratio values for reference:**
- User 2229: 11.596958174904943%
- User 12: 13.068181818181818%
- User 471: 14.50381679389313%

| Rank | User Id | Platform | Hit-Ratio (%) |
| ---: | ------: | -------- | ------------: |
| 1 | 2229 | iphone | 11.60 |
| 2 | 12 | iphone | 13.07 |
| 3 | 471 | iphone | 14.50 |

---

# Quality and reproducibility notes

* **Exact input CSV filenames used:** `ad-clicks.csv`, `buy-clicks.csv`, `game-clicks.csv`, `level-events.csv`, `team-assignments.csv`, `team.csv`, `user-session.csv`, `users.csv`.
* **Assumptions and join keys:** Joins between user profiles and actions were performed on `userId`. It was assumed that a user's `platformType` in `user-session.csv` applies across their interactions.
* **Null-value handling:** There were no null values encountered in the primary calculation fields (`price`, `isHit`, `buyId`, `platformType`).
* **Filename and schema inconsistencies:** The template implies the existence of 9 files, but only 8 CSVs are in the repository. The file `user-session.csv` is singularly named, whereas some external documentation refers to it plurally as `user-sessions`. The column name `teamId` is simply called `team` in `buy-clicks.csv` and `team-assignments.csv`.
* **Generated chart files:** 
  1. `chart_purchase_count_by_item.png`
  2. `chart_revenue_by_item.png`
  3. `chart_top_10_users_by_spending.png`
* **Execution Environment:** All calculations, aggregations, and chart generation were performed entirely with Python and the `pandas` and `matplotlib` libraries, bypassing Splunk and KNIME completely.
