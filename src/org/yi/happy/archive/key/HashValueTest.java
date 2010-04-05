package org.yi.happy.archive.key;

import org.junit.Test;

/**
 * Tests for {@link HashValue}.
 */
public class HashValueTest {
    /**
     * create a key with an empty hash
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBad1() {
        new HashValue();
    }

}
