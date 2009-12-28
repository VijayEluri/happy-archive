package org.yi.happy.archive.key;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * tests for ContentLocatorKey
 */
public class ContentLocatorKeyTest {
    /**
     * a good locator
     */
    @Test
    public void testGood1() {
        byte[] hash = { 0 };
        LocatorKey key = new ContentLocatorKey(hash);
        assertEquals("content-hash", key.getType());
        assertArrayEquals(hash, key.getHash());
        assertEquals("content-hash:00", key.toString());
    }

}
