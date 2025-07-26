#!/usr/bin/env python3
import sys
import csv

for line in sys.stdin:
    try:
        row = next(csv.reader([line]))
        app_id = row[0]
        review_score = int(row[3])  # 1 = positive, -1 = negative

        if review_score == 1:
            print(f"{app_id}\t1\t0")
        elif review_score == -1:
            print(f"{app_id}\t0\t1")
    except:
        continue  # Skip malformed rows

