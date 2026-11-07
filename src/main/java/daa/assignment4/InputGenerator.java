package daa.assignment4;

import java.util.Random;

public class InputGenerator {

    private final Random rnd;

    public InputGenerator(long seed) {
        this.rnd = new Random(seed);
    }

    public String random(int n) {
        char[] buf = new char[n];
        for (int i = 0; i < n; i++) {
            buf[i] = (char) ('a' + rnd.nextInt(26));
        }
        return new String(buf);
    }

    public String allSame(int n, char c) {
        return String.valueOf(c).repeat(n);
    }

    public String[] naiveWorstCase(int textLen, int patternLen) {
        String text = allSame(textLen, 'a');
        String pattern = allSame(patternLen - 1, 'a') + "b";
        return new String[]{text, pattern};
    }

    public String[] naiveBestCase(int textLen, int patternLen) {
        String text = allSame(textLen, 'a');
        String pattern = "b" + allSame(patternLen - 1, 'a');
        return new String[]{text, pattern};
    }

    public String periodic(int n, String period) {
        StringBuilder sb = new StringBuilder(n);
        while (sb.length() < n) {
            sb.append(period);
        }
        return sb.substring(0, n);
    }

    public String[] editDistanceMax(int n) {
        return new String[]{random(n), random(n)};
    }

    public String[] editDistanceZero(int n) {
        String s = random(n);
        return new String[]{s, s};
    }

    public String[] editDistanceSmall(int n) {
        String a = random(n);
        char[] b = a.toCharArray();
        if (n > 0) {
            int pos = rnd.nextInt(n);
            b[pos] = (char) ('a' + (b[pos] - 'a' + 1) % 26);
        }
        return new String[]{a, new String(b)};
    }
}
