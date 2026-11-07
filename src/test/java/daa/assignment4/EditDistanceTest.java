package daa.assignment4;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class EditDistanceTest {

    @Test
    @DisplayName("Reference values from assignment section 7")
    void referenceValues() {
        assertEquals(3, EditDistance.recursive("kitten", "sitting"));
        assertEquals(3, EditDistance.table("kitten", "sitting"));
        assertEquals(3, EditDistance.twoRows("kitten", "sitting"));

        assertEquals(2, EditDistance.recursive("flaw", "lawn"));
        assertEquals(2, EditDistance.table("flaw", "lawn"));
        assertEquals(2, EditDistance.twoRows("flaw", "lawn"));

        assertEquals(5, EditDistance.recursive("intention", "execution"));
        assertEquals(5, EditDistance.table("intention", "execution"));
        assertEquals(5, EditDistance.twoRows("intention", "execution"));
    }

    @Test
    @DisplayName("Empty strings")
    void emptyStrings() {
        assertEquals(3, EditDistance.recursive("", "abc"));
        assertEquals(3, EditDistance.table("", "abc"));
        assertEquals(3, EditDistance.twoRows("", "abc"));

        assertEquals(3, EditDistance.recursive("abc", ""));
        assertEquals(3, EditDistance.table("abc", ""));
        assertEquals(3, EditDistance.twoRows("abc", ""));

        assertEquals(0, EditDistance.recursive("", ""));
        assertEquals(0, EditDistance.table("", ""));
        assertEquals(0, EditDistance.twoRows("", ""));

        assertEquals(0, EditDistance.recursive("abc", "abc"));
        assertEquals(0, EditDistance.table("abc", "abc"));
        assertEquals(0, EditDistance.twoRows("abc", "abc"));
    }

    @Test
    @DisplayName("All three versions agree on 100 random short pairs")
    void threeVersionsAgree() {
        Random rnd = new Random(12345);
        for (int i = 0; i < 100; i++) {
            int lenA = rnd.nextInt(8); // 0..7
            int lenB = rnd.nextInt(8);
            String a = randomString(rnd, lenA);
            String b = randomString(rnd, lenB);

            int r = EditDistance.recursive(a, b);
            int t = EditDistance.table(a, b);
            int w = EditDistance.twoRows(a, b);

            assertEquals(r, t, "recursive vs table on \"" + a + "\" / \"" + b + "\"");
            assertEquals(r, w, "recursive vs twoRows on \"" + a + "\" / \"" + b + "\"");
        }
    }

    private static String randomString(Random rnd, int len) {
        char[] buf = new char[len];
        for (int i = 0; i < len; i++) {
            buf[i] = (char) ('a' + rnd.nextInt(6)); // small alphabet
        }
        return new String(buf);
    }
}
