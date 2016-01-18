package org.yi.happy.archive.key;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link PassValue}.
 */
public class PassValueTest {
    /**
     * The pass value can be created with a byte, and renders as that byte in
     * base16.
     */
    @Test
    public void oneByteLong() {
        PassValue v = new PassValue(0x11);

        assertEquals("11", v.toString());
    }

    /**
     * A {@link PassValue} can be created with zero bytes, and renders as an
     * empty string.
     */
    @Test
    public void zeroByteLong() {
        PassValue v = new PassValue();

        assertEquals("", v.toString());
    }
}
