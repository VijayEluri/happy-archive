package org.yi.happy.archive.key;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.yi.happy.archive.Bytes;

/**
 * tests for ContentFullKey
 */
public class ContentFullKeyTest {
    /**
     * create a good key
     */
    @Test
    public void testGood() {
	Bytes hash = new Bytes(0x00);
	Bytes pass = new Bytes(0x11);

	ContentFullKey key = new ContentFullKey(hash, pass);

	assertEquals("content-hash:00:11", key.toString());
    }

    /**
     * create a key with an empty hash
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBad1() {
	Bytes hash = new Bytes();
	Bytes pass = new Bytes(0x11);

	new ContentFullKey(hash, pass);
    }

    /**
     * create a key with an empty pass
     */
    @Test
    public void testGood2() {
	Bytes hash = new Bytes(0x00);
	Bytes pass = new Bytes();

	new ContentFullKey(hash, pass);
    }

    /**
     * convert a content key to a locator key
     */
    @Test
    public void testToLocatorKey1() {
	ContentFullKey in = new ContentFullKey(new Bytes(0x00, 0x11, 0x22),
		new Bytes(0x33, 0x44, 0x55));

	Key have = in.toLocatorKey();

	Key want = new ContentLocatorKey(new Bytes(0x00, 0x11, 0x22));
	assertEquals(want, have);
    }
}
