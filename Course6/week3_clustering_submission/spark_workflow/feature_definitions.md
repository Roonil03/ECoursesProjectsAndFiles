# Feature Definitions

**1. total_game_clicks**
- **Raw source file**: `game-clicks.csv`
- **User-level aggregation formula**: `COUNT(*)` grouped by `userId`.
- **Null handling**: If a user had no game clicks, they were assigned `0`.
- **Why it is useful**: Captures overall raw engagement with the application.
- **Units and interpretation**: Count of total clicks; higher means the user plays more actively.

**2. total_ad_clicks**
- **Raw source file**: `ad-clicks.csv`
- **User-level aggregation formula**: `COUNT(*)` grouped by `userId`.
- **Null handling**: If a user had no ad clicks, they were assigned `0`.
- **Why it is useful**: Captures advertising interaction and receptiveness to marketing.
- **Units and interpretation**: Count of ad clicks; higher means greater susceptibility to advertisements.

**3. total_revenue**
- **Raw source file**: `buy-clicks.csv`
- **User-level aggregation formula**: `SUM(price)` grouped by `userId`.
- **Null handling**: If a user made no purchases, they were assigned `0.0`.
- **Why it is useful**: Directly captures the exact monetization output of a user.
- **Units and interpretation**: Monetary value (Dollars/currency); higher means more profitable user.
