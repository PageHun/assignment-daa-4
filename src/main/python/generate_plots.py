#!/usr/bin/env python3
"""
Generate plots for DAA Assignment 4 from results/results.csv

Produces at least 3 plots (one uses a ratio / log scale) that satisfy:
- title, labeled axes with units
- one-sentence takeaway under each figure (printed to console)
"""

import os
import pandas as pd
import matplotlib.pyplot as plt
import matplotlib
matplotlib.use("Agg")  # non-interactive backend

# ------------------------------------------------------------------
# Setup
# ------------------------------------------------------------------
CSV_PATH = "results/results.csv"
OUT_DIR = "results/plots"
os.makedirs(OUT_DIR, exist_ok=True)

df = pd.read_csv(CSV_PATH)

# Style
plt.rcParams.update({
    "figure.figsize": (8, 5),
    "font.size": 11,
    "axes.titlesize": 13,
    "axes.labelsize": 11,
    "legend.fontsize": 10,
    "lines.linewidth": 2,
    "lines.markersize": 7,
})

# ------------------------------------------------------------------
# Plot 1 – Edit Distance: table vs twoRows counter growth
# ------------------------------------------------------------------
dp = df[df["algorithm"].isin(["table", "twoRows"])].copy()
dp_random = dp[dp["input"] == "random"]

fig, ax = plt.subplots()
for algo, marker, color in [("table", "o", "#1f77b4"), ("twoRows", "s", "#ff7f0e")]:
    sub = dp_random[dp_random["algorithm"] == algo].sort_values("n")
    ax.plot(sub["n"], sub["counter"], marker=marker, color=color, label=algo)

ax.set_xlabel("String length n (= m)")
ax.set_ylabel("Counter (table cells filled)")
ax.set_title("Edit Distance – Work of table vs twoRows")
ax.legend()
ax.grid(True, alpha=0.3)
ax.set_xticks(sorted(dp_random["n"].unique()))

path1 = os.path.join(OUT_DIR, "01_edit_table_vs_tworows.png")
fig.tight_layout()
fig.savefig(path1, dpi=150)
plt.close(fig)
print(f"[Plot 1] Saved {path1}")
print("         Takeaway: Both DP versions perform almost identical Θ(n·m) work;")
print("                   the two curves lie on top of each other.\n")

# ------------------------------------------------------------------
# Plot 2 – Recursive explosion (counter vs n, log-y)
# ------------------------------------------------------------------
rec = df[df["algorithm"] == "recursive"].copy()
rec_random = rec[rec["input"] == "random"].sort_values("n")
rec_ident  = rec[rec["input"] == "identical"].sort_values("n")

fig, ax = plt.subplots()
ax.plot(rec_random["n"], rec_random["counter"], "o-", color="#d62728",
        label="random (explodes)")
ax.plot(rec_ident["n"],  rec_ident["counter"],  "s--", color="#2ca02c",
        label="identical (linear)")
ax.set_yscale("log")
ax.set_xlabel("String length n")
ax.set_ylabel("Counter (recursive calls)  [log scale]")
ax.set_title("Recursive Edit Distance – Exponential Blow-up")
ax.legend()
ax.grid(True, which="both", alpha=0.3)

path2 = os.path.join(OUT_DIR, "02_recursive_explosion.png")
fig.tight_layout()
fig.savefig(path2, dpi=150)
plt.close(fig)
print(f"[Plot 2] Saved {path2}")
print("         Takeaway: On random inputs the call count grows exponentially;")
print("                   beyond n≈10–12 the pure recursive version is already impractical.\n")

# ------------------------------------------------------------------
# Plot 3 – Naive vs KMP on worst-case input
# ------------------------------------------------------------------
search = df[df["algorithm"].isin(["naive", "kmp"])].copy()
worst = search[search["input"] == "worst"].sort_values("n")

fig, ax = plt.subplots()
for algo, marker, color in [("naive", "o", "#d62728"), ("kmp", "s", "#1f77b4")]:
    sub = worst[worst["algorithm"] == algo]
    ax.plot(sub["n"], sub["counter"], marker=marker, color=color, label=algo)

ax.set_xlabel("Text length N")
ax.set_ylabel("Counter (character comparisons)")
ax.set_title("String Search – Naive vs KMP on Worst-Case Input")
ax.legend()
ax.grid(True, alpha=0.3)

path3 = os.path.join(OUT_DIR, "03_search_worst_case.png")
fig.tight_layout()
fig.savefig(path3, dpi=150)
plt.close(fig)
print(f"[Plot 3] Saved {path3}")
print("         Takeaway: Naive comparisons grow roughly as O(N·m) while KMP stays")
print("                   linear in N; the gap widens with larger texts.\n")

# ------------------------------------------------------------------
# Plot 4 – Ratio Naive/KMP counters (log axis) on worst-case
# ------------------------------------------------------------------
naive_w = worst[worst["algorithm"] == "naive"].set_index("n")["counter"]
kmp_w   = worst[worst["algorithm"] == "kmp"].set_index("n")["counter"]
ratio   = (naive_w / kmp_w).dropna().sort_index()

fig, ax = plt.subplots()
ax.plot(ratio.index, ratio.values, "D-", color="#9467bd", markersize=8)
ax.set_yscale("log")
ax.set_xlabel("Text length N")
ax.set_ylabel("Ratio  Naive_counter / KMP_counter  [log scale]")
ax.set_title("How Many Times More Work Does Naive Do? (Worst Case)")
ax.grid(True, which="both", alpha=0.3)
ax.axhline(1, color="gray", linestyle=":", linewidth=1)

path4 = os.path.join(OUT_DIR, "04_naive_kmp_ratio.png")
fig.tight_layout()
fig.savefig(path4, dpi=150)
plt.close(fig)
print(f"[Plot 4] Saved {path4}")
print("         Takeaway: On the worst-case input the ratio grows with N;")
print("                   Naive performs tens of times more comparisons than KMP.\n")

# ------------------------------------------------------------------
# Plot 5 (bonus) – Naive vs KMP on all three input types
# ------------------------------------------------------------------
fig, axes = plt.subplots(1, 3, figsize=(14, 4.5), sharey=False)
input_types = ["best", "random", "worst"]
titles = ["Best case (immediate mismatch)", "Random", "Worst case (almost matching)"]

for ax, inp, title in zip(axes, input_types, titles):
    sub = search[search["input"] == inp].sort_values("n")
    for algo, marker, color in [("naive", "o", "#d62728"), ("kmp", "s", "#1f77b4")]:
        s = sub[sub["algorithm"] == algo]
        ax.plot(s["n"], s["counter"], marker=marker, color=color, label=algo)
    ax.set_title(title)
    ax.set_xlabel("Text length N")
    ax.grid(True, alpha=0.3)
    ax.legend()

axes[0].set_ylabel("Counter (character comparisons)")
fig.suptitle("String Search – Effect of Input Type on Work", fontsize=14, y=1.02)
fig.tight_layout()

path5 = os.path.join(OUT_DIR, "05_search_all_inputs.png")
fig.savefig(path5, dpi=150, bbox_inches="tight")
plt.close(fig)
print(f"[Plot 5] Saved {path5}")
print("         Takeaway: The advantage of KMP is most visible on the worst-case input;")
print("                   on best-case and random inputs the counters are close.\n")

print("=" * 60)
print(f"All plots written to {OUT_DIR}/")
print("Use these PNGs in REPORT.md (insert under the corresponding headings).")