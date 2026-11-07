package daa.assignment4;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class StringSearchTest {

    @Test
    @DisplayName("LPS reference values from assignment section 7")
    void lpsReference() {
        assertArrayEquals(new int[]{0, 1, 2, 0, 1, 2, 3, 3},
                StringSearch.lps("AAACAAAA"));
        assertArrayEquals(new int[]{0, 0, 1, 2, 0, 1, 2, 3, 4},
                StringSearch.lps("ABABCABAB"));
    }

    @Test
    @DisplayName("Search reference matches (including overlapping)")
    void searchReference() {
        assertEquals(List.of(10),
                StringSearch.naive("ABABDABACDABABCABAB", "ABABCABAB"));
        assertEquals(List.of(10),
                StringSearch.kmp("ABABDABACDABABCABAB", "ABABCABAB"));

        assertEquals(List.of(0, 1, 2, 3),
                StringSearch.naive("AAAAA", "AA"));
        assertEquals(List.of(0, 1, 2, 3),
                StringSearch.kmp("AAAAA", "AA"));

        assertEquals(List.of(0, 2, 4),
                StringSearch.naive("ABABABA", "ABA"));
        assertEquals(List.of(0, 2, 4),
                StringSearch.kmp("ABABABA", "ABA"));
    }

    @Test
    @DisplayName("Empty pattern throws IllegalArgumentException")
    void emptyPatternThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> StringSearch.naive("abc", ""));
        assertThrows(IllegalArgumentException.class,
                () -> StringSearch.kmp("abc", ""));
    }

    @Test
    @DisplayName("Pattern longer than text returns empty list")
    void patternLongerThanText() {
        assertTrue(StringSearch.naive("ab", "abc").isEmpty());
        assertTrue(StringSearch.kmp("ab", "abc").isEmpty());
        assertTrue(StringSearch.naive("", "a").isEmpty());
        assertTrue(StringSearch.kmp("", "a").isEmpty());
    }

    @Test
    @DisplayName("Naive and KMP return the same list on 100 random texts")
    void naiveAndKmpAgree() {
        Random rnd = new Random(98765);
        for (int i = 0; i < 100; i++) {
            int textLen = 20 + rnd.nextInt(40);
            int patLen = 1 + rnd.nextInt(8);
            String text = randomString(rnd, textLen);
            String pattern = randomString(rnd, patLen);

            List<Integer> naiveRes = StringSearch.naive(text, pattern);
            List<Integer> kmpRes = StringSearch.kmp(text, pattern);

            assertEquals(naiveRes, kmpRes,
                    "Mismatch on text=\"" + text + "\" pattern=\"" + pattern + "\"");
        }
    }

    @Test
    @DisplayName("No matches when pattern is absent")
    void noMatches() {
        assertTrue(StringSearch.naive("abcdefgh", "xyz").isEmpty());
        assertTrue(StringSearch.kmp("abcdefgh", "xyz").isEmpty());
    }

    private static String randomString(Random rnd, int len) {
        char[] buf = new char[len];
        for (int i = 0; i < len; i++) {
            buf[i] = (char) ('a' + rnd.nextInt(4));
        }
        return new String(buf);
    }
}
