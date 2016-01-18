package org.yi.happy.archive;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link String}.
 */
public class StringTest {
    /**
     * check the case of formatted hex digits.
     */
    @Test
    public void testFormat1() {
        String out = String.format("%08x", 0xabcdef01);

        assertEquals("abcdef01", out);
    }

    /**
     * check the case of formatted hex digits, with padding.
     */
    @Test
    public void testFormat2() {
        String out = String.format("%08x", 0xabcdef);

        assertEquals("00abcdef", out);
    }
}
