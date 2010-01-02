package org.yi.happy.archive.key;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * tests for ContentFullKey
 */
public class ContentFullKeyTest {
    /**
     * create a good key
     */
    @Test
    public void testGood() {
	byte[] hash = { 0x00 };
	byte[] pass = { 0x11 };

	ContentFullKey key = new ContentFullKey(hash, pass);

	assertEquals("content-hash:00:11", key.toString());
    }

    /**
     * create a key with an empty hash
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBad1() {
	byte[] hash = {};
	byte[] pass = { 0x11 };

	new ContentFullKey(hash, pass);
    }

    /**
     * create a key with an empty pass
     */
    @Test
    public void testGood2() {
	byte[] hash = { 0x00 };
	byte[] pass = {};

	new ContentFullKey(hash, pass);
    }
}
