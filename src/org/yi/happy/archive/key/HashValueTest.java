package org.yi.happy.archive.key;

import org.junit.Test;

/**
 * Tests for {@link HashValue}.
 */
public class HashValueTest {
    /**
     * having nothing, when creating a hash value with no bytes, then an error
     * is raised.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBad1() {
        new HashValue();
    }

}
