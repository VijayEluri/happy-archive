package org.yi.happy.archive.key;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link LocatorKeyParse}.
 */
public class LocatorKeyParseTest {
    /**
     * parse a name locator
     */
    @Test
    public void testParseNameLocator() {
        LocatorKey k = LocatorKeyParse.parseLocatorKey("name-hash:001122");

        assertEquals("name-hash", k.getType());

        assertEquals(aHash(), k.getHash());
    }

    private HashValue aHash() {
        return new HashValue(0x00, 0x11, 0x22);
    }

    /**
     * parse a content locator
     */
    @Test
    public void testParseContentLocator() {
        LocatorKey k = LocatorKeyParse.parseLocatorKey("content-hash:001122");

        assertEquals("content-hash", k.getType());

        assertEquals(aHash(), k.getHash());
    }

    /**
     * parse a content locator
     */
    @Test
    public void testParseBlobLocator() {
        LocatorKey k = LocatorKeyParse.parseLocatorKey("blob:001122");

        assertEquals("blob", k.getType());

        assertEquals(aHash(), k.getHash());
    }

    /**
     * parse an invalid key
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBad2() {
        LocatorKeyParse.parseLocatorKey("content-hash");
    }

    /**
     * parse content key with odd hex digits
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBadContentKey2() {
        LocatorKeyParse.parseLocatorKey("content-hash:0:0");
    }

    /**
     * parse content locator with odd hex digits
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBadContentLocator() {
        LocatorKeyParse.parseLocatorKey("content-hash:0");
    }

    /**
     * parse name locator with odd hex digits
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBadNameLocator() {
        LocatorKeyParse.parseLocatorKey("name-hash:0");
    }

    /**
     * parse a locator key with an unknown type
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBadType2() {
        LocatorKeyParse.parseLocatorKey("bad:00");
    }

}
