package org.yi.happy.archive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for {@link Bytes}
 */
public class BytesTest {
    /**
     * the comparison is based on unsigned bytes.
     */
    @Test
    public void testCompare() {
        Bytes a = new Bytes(0x00);
        Bytes b = new Bytes(0xff);

        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(a) > 0);
    }

    /**
     * the comparison goes from left to right.
     */
    @Test
    public void testCompare4() {
        Bytes a = new Bytes(0x00, 0xff);
        Bytes b = new Bytes(0xff, 0x00);

        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(a) > 0);
    }

    /**
     * shorter values are lower if the prefix matches.
     */
    @Test
    public void testCompare2() {
        Bytes a = new Bytes();
        Bytes b = new Bytes(0xff);

        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(a) > 0);
    }

    /**
     * shorter values are lower if the prefix matches.
     */
    @Test
    public void testCompare5() {
        Bytes a = new Bytes(0xff);
        Bytes b = new Bytes(0xff, 0xff);

        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(a) > 0);
    }

    /**
     * the same length with the same bytes is equal.
     */
    @Test
    public void testCompare3() {
        Bytes a = new Bytes(0xff);
        Bytes b = new Bytes(0xff);

        assertTrue(a.compareTo(b) == 0);
        assertEquals(a, b);
    }
}
