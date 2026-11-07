package daa.assignment4;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExperimentRunner {

    private static final long SEED = 42L;
    private static final int REPEATS = 3;

    public static void main(String[] args) throws Exception {
        Path resultsDir = Path.of("results");
        Files.createDirectories(resultsDir);
        Path csvPath = resultsDir.resolve("results.csv");

        try (PrintWriter out = new PrintWriter(new FileWriter(csvPath.toFile()))) {
            out.println("algorithm,input,n,m,time_ms,counter,distance_or_matches");

            runEditDistanceExperiments(out);
            runStringSearchExperiments(out);
        }

        System.out.println("Wrote " + csvPath.toAbsolutePath());
        System.out.println("You can now generate plots from results/results.csv");
    }

    private static void runEditDistanceExperiments(PrintWriter out) {
        InputGenerator gen = new InputGenerator(SEED);

        int[] recSizes = {4, 6, 8, 10, 12};
        for (int n : recSizes) {
            String[] identical = gen.editDistanceZero(n);
            measureEdit(out, "recursive", "identical", n,
                    identical[0], identical[1],
                    EditDistance::recursive);
            if (n <= 8) {
                String[] randomPair = gen.editDistanceMax(n);
                measureEdit(out, "recursive", "random", n,
                        randomPair[0], randomPair[1],
                        EditDistance::recursive);
            }
        }

        int[] dpSizes = {50, 100, 200, 400, 600};
        String[] inputTypes = {"random", "identical", "almost"};
        for (int n : dpSizes) {
            for (String type : inputTypes) {
                String a, b;
                switch (type) {
                    case "identical" -> {
                        String[] pair = gen.editDistanceZero(n);
                        a = pair[0];
                        b = pair[1];
                    }
                    case "almost" -> {
                        String[] pair = gen.editDistanceSmall(n);
                        a = pair[0];
                        b = pair[1];
                    }
                    default -> {
                        String[] pair = gen.editDistanceMax(n);
                        a = pair[0];
                        b = pair[1];
                    }
                }
                measureEdit(out, "table", type, n, a, b, EditDistance::table);
                measureEdit(out, "twoRows", type, n, a, b, EditDistance::twoRows);
            }
        }
    }

    @FunctionalInterface
    private interface EditFn {
        int apply(String a, String b);
    }

    private static void measureEdit(PrintWriter out, String algo, String inputType,
                                    int n, String a, String b, EditFn fn) {
        List<Long> times = new ArrayList<>();
        long lastCounter = 0;
        int lastDist = 0;

        for (int r = 0; r < REPEATS; r++) {
            EditDistance.resetCounter();
            long t0 = System.nanoTime();
            lastDist = fn.apply(a, b);
            long t1 = System.nanoTime();
            times.add((t1 - t0) / 1_000_000L); // ms
            lastCounter = EditDistance.getCounter();
        }
        long medianTime = median(times);
        out.printf(Locale.US, "%s,%s,%d,%d,%d,%d,%d%n",
                algo, inputType, n, n, medianTime, lastCounter, lastDist);
        System.out.printf("Edit %-8s %-10s n=%4d  time=%5d ms  counter=%10d  dist=%d%n",
                algo, inputType, n, medianTime, lastCounter, lastDist);
    }

    private static void runStringSearchExperiments(PrintWriter out) {
        InputGenerator gen = new InputGenerator(SEED + 1);

        int[] textSizes = {1_000, 5_000, 10_000, 20_000, 40_000};
        int patternLen = 40;

        for (int n : textSizes) {
            // Random
            String textR = gen.random(n);
            String patR = gen.random(patternLen);
            measureSearch(out, "naive", "random", n, patternLen, textR, patR, StringSearch::naive);
            measureSearch(out, "kmp", "random", n, patternLen, textR, patR, StringSearch::kmp);

            // Worst-case for Naive
            String[] worst = gen.naiveWorstCase(n, patternLen);
            measureSearch(out, "naive", "worst", n, patternLen, worst[0], worst[1], StringSearch::naive);
            measureSearch(out, "kmp", "worst", n, patternLen, worst[0], worst[1], StringSearch::kmp);

            // Best-case for Naive (immediate mismatch)
            String[] best = gen.naiveBestCase(n, patternLen);
            measureSearch(out, "naive", "best", n, patternLen, best[0], best[1], StringSearch::naive);
            measureSearch(out, "kmp", "best", n, patternLen, best[0], best[1], StringSearch::kmp);
        }
    }

    @FunctionalInterface
    private interface SearchFn {
        List<Integer> apply(String text, String pattern);
    }

    private static void measureSearch(PrintWriter out, String algo, String inputType,
                                      int n, int m, String text, String pattern, SearchFn fn) {
        List<Long> times = new ArrayList<>();
        long lastCounter = 0;
        int matchCount = 0;

        for (int r = 0; r < REPEATS; r++) {
            StringSearch.resetCounter();
            long t0 = System.nanoTime();
            List<Integer> matches = fn.apply(text, pattern);
            long t1 = System.nanoTime();
            times.add((t1 - t0) / 1_000_000L);
            lastCounter = StringSearch.getCounter();
            matchCount = matches.size();
        }
        long medianTime = median(times);
        out.printf(Locale.US, "%s,%s,%d,%d,%d,%d,%d%n",
                algo, inputType, n, m, medianTime, lastCounter, matchCount);
        System.out.printf("Search %-5s %-8s N=%6d  time=%5d ms  counter=%10d  matches=%d%n",
                algo, inputType, n, medianTime, lastCounter, matchCount);
    }

    private static long median(List<Long> values) {
        values.sort(Long::compareTo);
        int mid = values.size() / 2;
        if (values.size() % 2 == 0) {
            return (values.get(mid - 1) + values.get(mid)) / 2;
        }
        return values.get(mid);
    }
}