# Capstone Week 3 Clustering Submission

## 1. Attribute Selection

`features_used = ['total_game_clicks', 'total_ad_clicks', 'total_revenue']`

| Attribute | Rationale for Selection |
| --------- | ----------------------- |
| `total_game_clicks` | Captures raw baseline engagement and application usage. This distinguishes highly active players from dormant users. |
| `total_ad_clicks` | Captures advertising interaction and receptiveness to external marketing. High scores indicate users who can be monetized heavily via ad networks. |
| `total_revenue` | Captures direct purchase behavior. This is the clearest indicator of "Whale" status and direct in-app purchase monetization. |

**Aggregation Note**: These features were aggregated directly from the raw log files (`game-clicks.csv`, `ad-clicks.csv`, `buy-clicks.csv`) at the user level, ensuring exactly one row per `userId` present in `users.csv`. Users with missing activity were zero-filled (e.g., users who never bought an item have `0.0` revenue). We explicitly excluded raw identifiers (`userId`, `userSessionId`) from the K-Means input vectors to prevent meaningless structural bias.

---

## 2. Training Data Set Creation

**First five rows of the training dataset**:
```csv
userId,total_game_clicks,total_ad_clicks,total_revenue
442,176.0,4.0,10.0
949,224.0,10.0,0.0
1654,0.0,0.0,0.0
1586,0.0,0.0,0.0
599,0.0,0.0,0.0
```

* **Dataset Dimensions**: 2393 rows x 4 columns
* **Chosen Number of Clusters**: 3
* **Explanation**: The dataset was created via Pandas left-joins from the master `users.csv` list to aggregated counts from the interaction logs. All NaNs were explicitly replaced with `0`. K=3 was selected because it scored sufficiently on the Silhouette evaluation (0.79) while providing a perfectly interpretable business mapping: "Casuals/Free-to-Play", "Engaged Ad-Clickers", and "Whales".
* **Visual Reference**: See `artifacts/training_data_preview.png` for a screenshot representation.

---

## 3. Cluster Centers

**PySpark Code Snippet (Model Building)**:
```python
scaler = StandardScaler(inputCol="unscaled_features", outputCol="features", withMean=True, withStd=True)
df_scaled = scaler.fit(df_assembled).transform(df_assembled)

kmeans = KMeans(k=3, seed=42)
model = kmeans.fit(df_scaled)
centers = model.clusterCenters()
```

### Cluster Centers (Standardized units directly from PySpark model)
| Cluster | `total_game_clicks` | `total_ad_clicks` | `total_revenue` |
| :---: | :---: | :---: | :---: |
| **0** | 1.3397 | 1.4971 | 0.5385 |
| **1** | -0.3154 | -0.4209 | -0.3165 |
| **2** | 0.9317 | 2.3356 | 3.9665 |

### Cluster Summaries (Original unscaled business units)
*Note: We inversely calculated the original means by aggregating the data partitions to provide context to the standardized centers above.*
| Cluster | Size | Avg Game Clicks | Avg Ad Clicks | Avg Revenue | Description |
| :---: | :---: | :---: | :---: | :---: | --- |
| **0** | 380 | 1019.78 | 28.18 | $22.75 | The "Engaged Ad-Clickers" |
| **1** | 1912 | 150.07 | 0.81 | $0.83 | The "Casuals / Free-to-Play Mass" |
| **2** | 101 | 805.37 | 40.14 | $110.59 | The "Whales" |

**Cluster Descriptions**:
* **Cluster 0 ("The Engaged Ad-Clickers")**: This group plays the game the most overall (highest game clicks) and clicks a substantial number of ads, but their direct in-app purchase spending is only moderate (~$22). They are best monetized via continuous advertising.
* **Cluster 1 ("The Casuals")**: The largest segment (80% of users). They interact very little with the game (~150 clicks) and almost never click ads or spend money. They are dormant or barely engaged.
* **Cluster 2 ("The Whales")**: A highly exclusive group (101 users) that generates immense direct revenue (averaging over $110 each). They play heavily and also click the most ads on average, representing the hyper-engaged, big-spending demographic.

* **Visual Reference**: See `artifacts/cluster_centers.png`.
* **Summary of training set**: 2393 unique users containing no nulls, aggregated dynamically from 3 log files.

---

## 4. Recommended Actions

| Action Recommended | Rationale for the Action |
| ------------------ | ------------------------ |
| **Increase premium ad placements for Cluster 0 (Engaged Ad-Clickers)** | Cluster 0 is highly active (~1019 clicks) and heavily clicks ads, but they don't buy many items. Increasing ad frequency or integrating unskippable video ads specifically for this group can maximize their ad-based monetization without cannibalizing direct sales. |
| **Offer "First Purchase" massive discounts to Cluster 1 (Casuals)** | Cluster 1 contains the vast majority of users but averages only $0.83 revenue. Offering a high-value $0.99 starter pack could convert a fraction of these 1,912 users into paying customers, generating significant aggregate revenue. |
| **Implement an exclusive VIP loyalty tier for Cluster 2 (Whales)** | Cluster 2 users spend an astronomical amount ($110+). Offering them a VIP status with exclusive high-tier cosmetic items or faster progression will incentivize them to retain their spending habits and potentially spend even more, as they clearly have the budget. |

---

## 5. Reproducibility and Handoff

* **Input filenames**: `users.csv`, `game-clicks.csv`, `ad-clicks.csv`, `buy-clicks.csv`
* **Docker Details**: Used `python:3.9-slim` with `default-jre` installed. Executed `pyspark`, `pandas`, `numpy`, and `matplotlib`.
* **Execution Workflow**: The Docker runtime was spun up via `docker compose up --build`. The runtime evaluated K=2 (0.81), K=3 (0.79), and K=4 (0.82) Silhouette scores, generated PNGs via `matplotlib`, saved output CSVs, and gracefully exited. The cluster was completely torn down with `docker compose down`.
* **Retained Deliverables**: 
  - `results.md` (this file)
  - `data/w3_clustering.csv`
  - `artifacts/*.png` and `artifacts/*.csv`
  - `spark_workflow/*.py`, `.ipynb`, and `.md` (Non-executed Blueprints)
  - `Dockerfile` and `docker-compose.yml`
* **Deleted Files**: `data_prep.py` and `temp_runtime.py` have been purged to comply with artifact retention constraints.
* **Data-Quality Constraints**: Users who did not exist in a given log table (e.g. no ad clicks) were cleanly handled using standard pandas `fillna(0)` to prevent PySpark pipeline failure. No Git commands were executed.
