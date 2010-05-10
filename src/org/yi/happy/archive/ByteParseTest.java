package org.yi.happy.archive;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link ByteParse}.
 */
public class ByteParseTest {
    /**
     * Check that the constants are indeed the documented byte and character
     * values.
     */
    @Test
    public void testConstants() {
        assertEquals(0x0d, ByteParse.CR);
        assertEquals('\r', ByteParse.CR);

        assertEquals(0x0a, ByteParse.LF);
        assertEquals('\n', ByteParse.LF);
    }
}
