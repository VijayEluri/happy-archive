package org.yi.happy.archive.key;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.yi.happy.archive.test_data.TestData;

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

    /**
     * convert a name locator key to a locator key
     */
    @Test
    public void testToLocatorKey3() {
	Key key = TestData.KEY_NAME.getLocatorKey();
	assertTrue(key instanceof NameLocatorKey);

	LocatorKey have = key.toLocatorKey();
	LocatorKey want = TestData.KEY_NAME.getLocatorKey();
	assertEquals(want, have);
    }
}
