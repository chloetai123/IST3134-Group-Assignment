import pandas as pd
import os

# === CONFIGURATION ===
result_folder = "/Users/alloy/Documents/Learning/Y3S1/IST3134 Big Data Analytics in the cloud/assignment"
raw_data_path = os.path.join(result_folder, "dataset.csv")
result_files = ["part-00000", "part-00001", "part-00002"]

# === Load Raw CSV for Game Names ===
raw_df = pd.read_csv(raw_data_path, usecols=["app_id", "app_name"]).drop_duplicates()

# === Load MapReduce Result Files ===
data = []
for file in result_files:
    with open(os.path.join(result_folder, file), "r", encoding="utf-8") as f:
        for line in f:
            parts = line.strip().split('\t')
            if len(parts) == 4:
                app_id, pos, neg, ratio = parts
                try:
                    data.append({
                        "app_id": int(app_id),
                        "positive": int(pos),
                        "negative": int(neg),
                        "ratio": float(ratio)
                    })
                except ValueError:
                    continue  # skip bad lines

df = pd.DataFrame(data)

# === Join Game Names ===
df = df.merge(raw_df, on="app_id", how="left")

# === Summary Stats ===
total_positive = df["positive"].sum()
total_negative = df["negative"].sum()

# === Most Positive & Most Negative Games ===
most_positive_game = df.loc[df["positive"].idxmax()]
most_negative_game = df.loc[df["negative"].idxmax()]

# === OUTPUT ===
print("📊 Total positive reviews:", total_positive)
print("📊 Total negative reviews:", total_negative)

print("\n✅ Game with Highest Number of Positive Reviews:")
print(f"App ID   : {most_positive_game['app_id']}")
print(f"App Name : {most_positive_game['app_name']}")
print(f"Positive : {most_positive_game['positive']}")
print(f"Negative : {most_positive_game['negative']}")
print(f"Ratio    : {most_positive_game['ratio']:.2f}")

print("\n🚫 Game with Highest Number of Negative Reviews:")
print(f"App ID   : {most_negative_game['app_id']}")
print(f"App Name : {most_negative_game['app_name']}")
print(f"Positive : {most_negative_game['positive']}")
print(f"Negative : {most_negative_game['negative']}")
print(f"Ratio    : {most_negative_game['ratio']:.2f}")



