import pandas as pd
import matplotlib.pyplot as plt
import os
from pathlib import Path

# Paths
data_dir = Path("flamingo-data")
out_dir = Path("week1_template_fill_data")

# 1. Dataset Overview: This will be written directly into res.md from the CSVs.
csv_files = list(data_dir.glob("*.csv"))

# 2. Aggregation
buy_clicks = pd.read_csv(data_dir / "buy-clicks.csv")

# Amount spent
total_spent = buy_clicks["price"].sum()
print(f"Total spent buying items: {total_spent:.2f}")

# Unique items
unique_items = buy_clicks["buyId"].nunique()
print(f"Number of unique items available: {unique_items}")

# Chart A: Number of purchases by item
purchase_counts = buy_clicks["buyId"].value_counts().sort_index()
print("\n--- Purchase Count by Item ---")
print(purchase_counts)
plt.figure(figsize=(10, 6))
purchase_counts.plot(kind="bar", color="skyblue", edgecolor="black")
plt.title("Number of Purchases by Item")
plt.xlabel("Item ID (buyId)")
plt.ylabel("Number of Purchases")
plt.xticks(rotation=0)
plt.tight_layout()
plt.savefig(out_dir / "chart_purchase_count_by_item.png")
plt.close()

# Chart B: Money spent by item
revenue_by_item = buy_clicks.groupby("buyId")["price"].sum().sort_index()
print("\n--- Total amount spent by Item ---")
print(revenue_by_item)
plt.figure(figsize=(10, 6))
revenue_by_item.plot(kind="bar", color="lightgreen", edgecolor="black")
plt.title("Total Revenue by Item")
plt.xlabel("Item ID (buyId)")
plt.ylabel("Total Revenue ($)")
plt.xticks(rotation=0)
plt.tight_layout()
plt.savefig(out_dir / "chart_revenue_by_item.png")
plt.close()

# 3. Filtering
# Top ten users by total spending
user_spending = buy_clicks.groupby("userId")["price"].sum().sort_values(ascending=False)
top_10_users = user_spending.head(10)

print("\n--- Top 10 Users by Spending ---")
print(top_10_users)
plt.figure(figsize=(12, 6))
top_10_users.plot(kind="bar", color="salmon", edgecolor="black")
plt.title("Total Spending by Top 10 Users")
plt.xlabel("User ID")
plt.ylabel("Total Spending ($)")
plt.xticks(rotation=45)
plt.tight_layout()
plt.savefig(out_dir / "chart_top_10_users_by_spending.png")
plt.close()

# Top three buying users: platform and hit ratio
top_3_users = top_10_users.head(3).index.tolist()

user_session = pd.read_csv(data_dir / "user-session.csv")
game_clicks = pd.read_csv(data_dir / "game-clicks.csv")

print("\n--- Top 3 Users Details ---")
rank = 1
for uid in top_3_users:
    # Platform logic: Get unique platforms for this user
    user_platforms = user_session[user_session["userId"] == uid]["platformType"].unique().tolist()
    platform_str = ", ".join(user_platforms) if user_platforms else "Unknown"
    
    # Hit ratio logic: total hits / total clicks * 100
    user_game_clicks = game_clicks[game_clicks["userId"] == uid]
    if len(user_game_clicks) > 0:
        total_hits = user_game_clicks["isHit"].sum()
        total_clicks = len(user_game_clicks)
        hit_ratio = (total_hits / total_clicks) * 100
    else:
        hit_ratio = 0.0
        
    print(f"Rank {rank} | User {uid} | Platform: {platform_str} | Hit Ratio: {hit_ratio}%")
    rank += 1
