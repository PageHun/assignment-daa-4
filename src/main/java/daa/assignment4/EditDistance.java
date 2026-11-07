package daa.assignment4;

public class EditDistance {

    public static long counter = 0;

    public static void resetCounter() {
        counter = 0;
    }

    public static long getCounter() {
        return counter;
    }

    public static int recursive(String a, String b) {
        return recursiveHelper(a, b, a.length(), b.length());
    }

    private static int recursiveHelper(String a, String b, int i, int j) {
        counter++;
        if (i == 0) return j;
        if (j == 0) return i;
        if (a.charAt(i - 1) == b.charAt(j - 1)) {
            return recursiveHelper(a, b, i - 1, j - 1);
        }
        int insert = recursiveHelper(a, b, i, j - 1);
        int delete = recursiveHelper(a, b, i - 1, j);
        int replace = recursiveHelper(a, b, i - 1, j - 1);
        return 1 + Math.min(insert, Math.min(delete, replace));
    }

    public static int table(String a, String b) {
        int n = a.length();
        int m = b.length();
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
            counter++;
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
            counter++;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                counter++;
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j],
                            Math.min(dp[i][j - 1], dp[i - 1][j - 1]));
                }
            }
        }
        return dp[n][m];
    }

    public static int twoRows(String a, String b) {
        int n = a.length();
        int m = b.length();

        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];

        for (int j = 0; j <= m; j++) {
            prev[j] = j;
            counter++;
        }

        for (int i = 1; i <= n; i++) {
            curr[0] = i;
            counter++;
            for (int j = 1; j <= m; j++) {
                counter++;
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    curr[j] = prev[j - 1];
                } else {
                    curr[j] = 1 + Math.min(prev[j], Math.min(curr[j - 1], prev[j - 1]));
                }
            }
            int[] tmp = prev;
            prev = curr;
            curr = tmp;
        }
        return prev[m];
    }
}