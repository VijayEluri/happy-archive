package org.yi.happy.archive.key;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * tests for {@link NameLocatorKey}
 */
public class NameLocatorKeyTest {
    /**
     * a good locator
     */
    @Test
    public void testGood2() {
	byte[] hash = { 0 };
	LocatorKey key = new NameLocatorKey(hash);
	assertEquals("name-hash", key.getType());
	assertArrayEquals(hash, key.getHash());
    }

    /**
     * a bad locator
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBad1() {
	byte[] hash = {};
	new NameLocatorKey(hash);
    }

}
