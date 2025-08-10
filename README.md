# IST3134 – Big Data Analytics in the Cloud  
**Steam Game Review Sentiment Analysis on AWS EMR**

## 📌 Project Overview
This repository analyses Steam game reviews using **Hadoop MapReduce** on **AWS EMR** with two implementations:
- **Q1 (Python, Hadoop Streaming):** Aggregates sentiment per game (positive/negative counts and positive ratio).
- **Q2 (Java, JAR on EMR):** Extracts the top negative keywords from reviews marked as helpful.

The goal is to demonstrate large-scale processing on the cloud, compare approaches, and surface insights useful to game stakeholders.

---

## 📂 Repository Structure

```

├── Q1_Analysis/ # Python MapReduce (Hadoop Streaming)
│ ├── mapper_q1.py
│ ├── reducer_q1.py
│ ├── q1.py # Local post-processing & summary
│ ├── part-00000/1/2 # EMR outputs (examples)
│ └── README.md # Detailed Q1 steps
│
├── Q2_Analysis/ # Java MapReduce (Gradle + JAR)
│ ├── src/main/java/wordcount/
│ │ ├── WordCountMapper.java
│ │ ├── WordCountReducer.java
│ │ ├── NegativeWordMapper.java
│ │ ├── NegativeWordReducer.java
│ │ ├── WordCountDriver.java
│ │ └── Score.java
│ ├── build.gradle
│ └── README.md # Detailed Q2 steps
│
└── README.md # You are here

```
---

## 📊 Dataset
- **Name:** Steam Reviews  
- **Source:** [Kaggle – Steam Reviews](https://www.kaggle.com/datasets/andrewmvd/steam-reviews)  
- **Size:** ~6.4M rows (~2.1 GB)  
- **Columns:** `app_id`, `app_name`, `review_text`, `review_score`, `review_votes`

> **Note:** `dataset.csv` is *not* included due to size. Download from Kaggle and place it as instructed in each subfolder README.

---

## 🚀 Implementations

### Q1 — Python MapReduce (Hadoop Streaming)
- Counts **positive** and **negative** reviews per game.
- Computes **positive review ratio** per game.
- Identifies games with highest positive and highest negative review counts.
- Executed on **AWS EMR** (Streaming), outputs post-processed locally via `q1.py`.
- 📄 Steps: see [`Q1_Analysis/README.md`](Q1_Analysis/README.md)

### Q2 — Java MapReduce (Gradle JAR)
- Two-stage pipeline:
  1) Word scoring & vote aggregation  
  2) **Top-N negative keywords** from reviews with `review_score = -1` and `review_votes = 1`
- Built with **Gradle**, run as a **fat JAR** on **AWS EMR**.
- 📄 Steps: see [`Q2_Analysis/README.md`](Q2_Analysis/README.md)

---

## ⚖️ Comparison Summary
| Aspect              | Python MapReduce                                      | Java MapReduce                                           |
|---------------------|--------------------------------------------------------|----------------------------------------------------------|
| **Objective**       | Game-level sentiment aggregation (counts & ratio)      | Negative keyword mining (top-N from helpful negatives)   |
| **Inputs**          | `app_name`, `review_score`                             | `review_text`, `review_score`, `review_votes`            |
| **Outputs**         | Positive/negative counts and ratio per game            | Ranked list of complaint-related words                   |
| **Strengths**       | Quick to implement; simple post-processing; great for numeric aggregation | Fine-grained text control; suited for multi-step pipelines |
| **Limitations**     | No per-word insight                                    | More code & configuration overhead                       |

**Takeaway:** Python delivers fast, high-level sentiment trends; Java surfaces concrete complaint patterns. Using both yields a fuller picture.

---

## ▶️ Quick Start
```bash
# 1) Clone
git clone https://github.com/alloyscius/IST3134-Group-Assignment.git
cd IST3134-Group-Assignment

# 2) Follow subproject guides
# Q1: Python Hadoop Streaming on EMR
# Q2: Java JAR on EMR (Gradle build)

```

## ⚠ Notes & Tips

1) Ensure S3 paths follow each subfolder’s README (input/, scripts/, output/).

2) Check IAM roles for S3 read/write and EMR execution.

3) Terminate EMR clusters after runs to avoid costs.
