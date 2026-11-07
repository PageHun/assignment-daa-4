package daa.assignment4;

import java.util.ArrayList;
import java.util.List;

public class StringSearch {

    public static long counter = 0;

    public static void resetCounter() {
        counter = 0;
    }

    public static long getCounter() {
        return counter;
    }

    public static List<Integer> naive(String text, String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            throw new IllegalArgumentException("Pattern must not be empty");
        }
        List<Integer> result = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();
        if (m > n) {
            return result;
        }

        for (int i = 0; i <= n - m; i++) {
            int j = 0;
            while (j < m) {
                counter++;
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    break;
                }
                j++;
            }
            if (j == m) {
                result.add(i);
            }
        }
        return result;
    }

    public static int[] lps(String p) {
        int m = p.length();
        int[] lps = new int[m];
        int len = 0;
        int i = 1;
        while (i < m) {
            counter++;
            if (p.charAt(i) == p.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

    public static List<Integer> kmp(String text, String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            throw new IllegalArgumentException("Pattern must not be empty");
        }
        List<Integer> result = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();
        if (m > n) {
            return result;
        }

        int[] lpsArr = lps(pattern);

        int i = 0;
        int j = 0;
        while (i < n) {
            counter++;
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
                if (j == m) {
                    result.add(i - j);
                    j = lpsArr[j - 1];
                }
            } else {
                if (j != 0) {
                    j = lpsArr[j - 1];
                } else {
                    i++;
                }
            }
        }
        return result;
    }
}
