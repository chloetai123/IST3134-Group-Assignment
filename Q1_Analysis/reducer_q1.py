#!/usr/bin/env python3
import sys

current_app = None
pos_total = 0
neg_total = 0

for line in sys.stdin:
    app_id, pos, neg = line.strip().split("\t")
    pos = int(pos)
    neg = int(neg)

    if current_app == app_id:
        pos_total += pos
        neg_total += neg
    else:
        if current_app:
            total = pos_total + neg_total
            ratio = pos_total / total if total else 0
            print(f"{current_app}\t{pos_total}\t{neg_total}\t{ratio:.2f}")
        current_app = app_id
        pos_total = pos
        neg_total = neg

if current_app:
    total = pos_total + neg_total
    ratio = pos_total / total if total else 0
    print(f"{current_app}\t{pos_total}\t{neg_total}\t{ratio:.2f}")

