# Question 1 – Steam Game Review Sentiment Analysis using Hadoop JAR on AWS EMR

## 📌 Problem Statement

This task analyzes user sentiment on Steam game reviews using Hadoop MapReduce (executed in AWS EMR). The objectives are:

- Count the number of **positive** and **negative** reviews per game.
- Compute the **positive review ratio** for each game.
- Identify:
  - The game with the **highest number of positive reviews**
  - The game with the **highest number of negative reviews**

> All MapReduce jobs are executed on **AWS EMR** using a **Streaming Program**. Final results are processed using a local Python script (`q1.py`).

---
## 📁 Folder Structure

```
Q1_JAR_Analysis/
├── part-00000       # Output file from EMR
├── part-00001       # Output file from EMR
├── part-00002       # Output file from EMR
├── mapper_q1.py     # 
├── reducer_q1.py    # 
├── q1.py            # Python script to analyze EMR output
└── dataset.csv      # Raw dataset (excluded from GitHub due to size)
```

---

## 🛰️ How to Run on AWS EMR

### 🧾 Step 1: Upload to S3

Structure your bucket like this:

```
s3://your-bucket-name/
├── input/     # Contains dataset.csv
├── scripts/   # Contains mapper_q1.py, reducer_q1.py
└── output/    # Leave empty – EMR will write output here
```


### ⚙ Step 2: Create EMR Cluster

In the AWS EMR Console:

- **Software Configuration:**
  - Hadoop
  - EMR Release: `emr-6.10.0` or compatible

- **Cluster Settings:**
  - EC2 Key Pair (to SSH if needed)
  - IAM roles (with S3 read/write + EMR full access)
  - Log location (optional)

---

### ✅ Step 3: Add EMR Step (JAR)

1. Go to **Steps > Add Step**
2. **Step Type:** `Streaming program`

## 🧪 How to Analyze Results Locally

### 1️⃣ Prepare Files

Ensure the following files are in the same folder:

- `part-00000`, `part-00001`, `part-00002` – EMR outputs
- `q1.py` – Python script
- `dataset.csv` – Manually added (excluded from GitHub)

---

### 2️⃣ Run Analysis

Run the following command:

```
python q1.py
```

💡 Expected Output
- Total number of positive and negative reviews
- Game with highest positive review count
- Game with highest negative review count

---

## ⚠️ Notes
dataset.csv is not included in this repo due to its size (>2GB). Please place it manually in the same directory to run q1.py.
