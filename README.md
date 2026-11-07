# DAA Assignment 4 – Dynamic Programming and String Algorithms

## Project layout

```
src/main/java/kz/daa/assignment4/
    EditDistance.java      – recursive, table, two-rows
    StringSearch.java      – Naive + KMP (public lps())
    InputGenerator.java    – deterministic generators (fixed seed)
    ExperimentRunner.java  – runs the experiment → results/results.csv

src/test/java/kz/daa/assignment4/
    EditDistanceTest.java
    StringSearchTest.java

results/
    results.csv
    plots/                 

REPORT.md                  – experiment design card + analysis
```

## Build & Test

```bash
# requires Java 17+ and Maven
mvn clean test
```

All reference values from the assignment (section 7) are covered by the unit tests.

## Run the experiment

```bash
mvn clean compile exec:java
# or
mvn -q exec:java -Dexec.mainClass="kz.daa.assignment4.ExperimentRunner"
```

This writes `results/results.csv` with columns:

```
algorithm,input,n,m,time_ms,counter,distance_or_matches
```

## Generating plots

You can use any tool (Python + matplotlib, Excel, R, …).  
Example Python snippet (place under `results/`):

```python
import pandas as pd
import matplotlib.pyplot as plt

df = pd.read_csv("results.csv")

# Example: Edit-distance table vs twoRows counter growth
dp = df[df.algorithm.isin(["table", "twoRows"])]
for algo in ["table", "twoRows"]:
    sub = dp[dp.algorithm == algo]
    for inp in sub.input.unique():
        s = sub[sub.input == inp]
        plt.plot(s.n, s.counter, marker="o", label=f"{algo}-{inp}")
plt.xlabel("n (= m)")
plt.ylabel("counter (cell fills)")
plt.title("Edit Distance – work vs n")
plt.legend()
plt.grid(True)
plt.savefig("plots/edit_counter.png", dpi=150)
```

At least three plots are required; one of them must use a log axis or a ratio.

## Git

```bash
git init
git add .
git commit -m "feat: initial implementation of EditDistance and StringSearch"
# … more small commits …
git tag v1.0
```

Work on `main`, final tag `v1.0`.
