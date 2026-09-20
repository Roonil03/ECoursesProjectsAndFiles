# Catch the Pink Flamingo Data Analysis Results

This document contains the calculated results using the provided CSV files.

## 1. Distribution of `platformType` in `user-session.csv`
* **SPL Query**: `source="user-session.csv" | stats count by platformType | sort - count`
* **Python Equivalent**: `df["platformType"].value_counts()`
* **Result**: iPhone 3874, Android 3274, Windows 1240, Linux 504, Mac 358
* **Explanation**: This shows the operating system breakdown of player sessions. iPhone and Android are the most dominant platforms. Note: The source filename is `user-session.csv` (singular), while some instructions may refer to it as plural.

## 2. Two most commonly clicked ads from `ad-clicks.csv`
* **SPL Query**: `source="ad-clicks.csv" | stats count by adCategory | sort - count | head 2`
* **Python Equivalent**: `df["adCategory"].value_counts().head(2)`
* **Result**: computers 2638, games 2601
* **Explanation**: This highlights the ad categories that generate the highest engagement, which are "computers" and "games".

## 3. Two most commonly purchased products from `buy-clicks.csv`
* **SPL Query**: `source="buy-clicks.csv" | stats count by buyId | sort - count | head 2`
* **Python Equivalent**: `df["buyId"].value_counts().head(2)`
* **Result**: buyId=2 (714 purchases), buyId=5 (610 purchases)
* **Explanation**: These represent the most popular in-game microtransaction items purchased by users.

## 4. Average team size from `team-assignments.csv`
* **SPL Query**: `source="team-assignments.csv" | stats count by team | stats avg(count) as AverageTeamSize`
* **Python Equivalent**: `df.groupby("team")["userId"].count().mean()`
* **Result**: 77.984127
* **Explanation**: On average, a team consists of roughly 78 players, providing insight into the game's social organization scale.

## 5. Hit ratio: average `isHit` in `game-clicks.csv`
* **SPL Query**: `source="game-clicks.csv" | stats avg(isHit) as HitRatio`
* **Python Equivalent**: `df["isHit"].mean()`
* **Result**: 0.110323
* **Explanation**: About 11.03% of all clicks made in the game are successful hits, indicating the game's difficulty or user accuracy.

## 6. Minimum, maximum, and total `price` in `buy-clicks.csv`
* **SPL Query**: `source="buy-clicks.csv" | stats min(price) as MinPrice, max(price) as MaxPrice, sum(price) as TotalPurchaseValue`
* **Python Equivalent**: `df["price"].min()`, `df["price"].max()`, `df["price"].sum()`
* **Result**: Minimum price: 1.00, Maximum price: 20.00, Total purchase value: 21407.0
* **Explanation**: This defines the economy scaling from $1 to $20 items, and shows a total generated transaction volume of $21,407.

## 7. Average `teamLevel` for `sessionType="end"` on Windows
* **SPL Query**: `source="user-session.csv" sessionType="end" platformType="windows" | stats avg(teamLevel) as AverageEndingTeamLevel`
* **Python Equivalent**: `df[(df["sessionType"] == "end") & (df["platformType"] == "windows")]["teamLevel"].mean()`
* **Result**: 4.364516
* **Explanation**: Windows users end their sessions at an average team level of ~4.36, indicating their typical progression before logging off.

## 8. Average `teamLevel` for `sessionType="end"` on iPhone or Mac
* **SPL Query**: `source="user-session.csv" sessionType="end" (platformType="iphone" OR platformType="mac") | stats avg(teamLevel) as AverageEndingTeamLevel`
* **Python Equivalent**: `df[(df["sessionType"] == "end") & (df["platformType"].isin(["iphone", "mac"]))]["teamLevel"].mean()`
* **Result**: 4.352079
* **Explanation**: Apple ecosystem users (iPhone/Mac) end their sessions at a very similar progression level (~4.35) as Windows users.

## 9. Platforms used by `userId=1017`
* **SPL Query**: `source="user-session.csv" userId=1017 | stats count by platformType`
* **Python Equivalent**: `df[df["userId"] == 1017]["platformType"].unique()`
* **Result**: iPhone
* **Explanation**: User 1017 exclusively plays the game using an iPhone.

## 10. Android users who completed level 2
* **SPL Query**:
```spl
source="level-events.csv" teamLevel=2 eventType="end"
| rename teamId as team
| join team [ search source="team-assignments.csv" ]
| join userId [ search source="user-session.csv" platformType="android" ]
| stats dc(userId)
```
* **Python Equivalent**: Filter `level-events.csv` for `teamLevel=2` and `eventType="end"`, find related `teamId`s, use `team-assignments.csv` to map to `userId`s, and intersect with Android users from `user-session.csv`.
* **Result**: 398 exact unique Android users completed level 2.
* **Explanation**: This metric defines the cohort size of mobile (Android) players who reached a specific progression milestone.

---

## Quiz Answers

**1. Ad-click revenue at $0.50 per click**
* **Calculated Value**: $8,161.50
* **Derivation**: `len(ad_clicks) * 0.50` (16,323 * 0.5)

**2. Number of distinct advertisement categories**
* **Calculated Value**: 9
* **Derivation**: `ad_clicks["adCategory"].nunique()`

**3. Ad revenue where electronics earns $0.75 per click and every other category earns $0.40**
* **Calculated Value**: $6,913.15
* **Derivation**: `1097 * 0.75 + 15226 * 0.40` (Summing $0.75 for electronics and $0.40 for the rest)

**4. Company revenue from purchases at 2% of `price`**
* **Calculated Value**: $428.14
* **Derivation**: `$21,407.00 * 0.02` (Total purchase value * 2%)

**5. Number of distinct purchasable items**
* **Calculated Value**: 6
* **Derivation**: `buy_clicks["buyId"].nunique()`

**6. Price of the most expensive item**
* **Calculated Value**: $20.00
* **Derivation**: `buy_clicks["price"].max()`

**7. `buyId` purchased most often**
* **Calculated Value**: buyId 2
* **Derivation**: `buy_clicks["buyId"].value_counts().idxmax()` (Purchased 714 times)
