package org.yi.happy.archive.key;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * tests for {@link KeyParse}.
 */
public class KeyParseTest {
    /**
     * parse a full name key
     */
    @Test
    public void testParseFullName() {
        FullKey k = KeyParse.parseFullKey("name-hash:sha-256:test");

        assertEquals("name-hash", k.getType());
        assertTrue(k instanceof NameFullKey);

        NameFullKey n = (NameFullKey) k;

        assertEquals("sha-256", n.getDigest().getAlgorithm());
        assertEquals("test", n.getName());
    }

    /**
     * parse a name locator
     */
    @Test
    public void testParseNameLocator() {
        LocatorKey k = KeyParse.parseLocatorKey("name-hash:001122");

        assertEquals("name-hash", k.getType());

        assertEquals(new HashValue(0x00, 0x11, 0x22), k.getHash());
    }

    /**
     * parse a content locator
     */
    @Test
    public void testParseContentLocator() {
        LocatorKey k = KeyParse.parseLocatorKey("content-hash:001122");

        assertEquals("content-hash", k.getType());

        assertEquals(new HashValue(0x00, 0x11, 0x22), k.getHash());
    }

    /**
     * parse a full content key
     */
    @Test
    public void testParseFullContent() {
        FullKey k = KeyParse.parseFullKey("content-hash:001122:334455");

        assertEquals("content-hash", k.getType());

        assertTrue(k instanceof ContentFullKey);

        ContentFullKey n = (ContentFullKey) k;

        assertEquals(new HashValue(0x00, 0x11, 0x22), n.getHash());
        assertEquals(new PassValue(0x33, 0x44, 0x55), n.getPass());
    }

    /**
     * parse a full content key
     */
    @Test
    public void testParseFullContent2() {
        FullKey k = KeyParse.parseFullKey("content-hash:001122:");

        assertEquals("content-hash", k.getType());

        assertTrue(k instanceof ContentFullKey);

        ContentFullKey n = (ContentFullKey) k;

        assertEquals(new HashValue(0x00, 0x11, 0x22), n.getHash());
        assertEquals(new PassValue(), n.getPass());
    }

    /**
     * parse a content locator
     */
    @Test
    public void testParseBlobLocator() {
        LocatorKey k = KeyParse.parseLocatorKey("blob:001122");

        assertEquals("blob", k.getType());

        assertEquals(new HashValue(0x00, 0x11, 0x22), k.getHash());
    }

    /**
     * parse a full content key
     */
    @Test
    public void testParseFullBlob() {
        FullKey k = KeyParse.parseFullKey("blob:001122:334455");

        assertEquals("blob", k.getType());

        assertTrue(k instanceof BlobFullKey);

        BlobFullKey n = (BlobFullKey) k;

        assertEquals(new HashValue(0x00, 0x11, 0x22), n.getHash());
        assertEquals(new PassValue(0x33, 0x44, 0x55), n.getPass());
    }

    /**
     * parse a full content key
     */
    @Test
    public void testParseFullBlob2() {
        FullKey k = KeyParse.parseFullKey("blob:001122:");

        assertEquals("blob", k.getType());

        assertTrue(k instanceof BlobFullKey);

        BlobFullKey n = (BlobFullKey) k;

        assertEquals(new HashValue(0x00, 0x11, 0x22), n.getHash());
        assertEquals(new PassValue(), n.getPass());
    }

    /**
     * parse an invalid key
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBad1() {
        KeyParse.parseFullKey("content-hash");
    }

    /**
     * parse an invalid key
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBad2() {
        KeyParse.parseLocatorKey("content-hash");
    }

    /**
     * parse content key with odd hex digits
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBadContentKey1() {
        KeyParse.parseFullKey("content-hash:0:0");
    }

    /**
     * parse content key with odd hex digits
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBadContentKey2() {
        KeyParse.parseLocatorKey("content-hash:0:0");
    }

    /**
     * parse content locator with odd hex digits
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBadContentLocator() {
        KeyParse.parseLocatorKey("content-hash:0");
    }

    /**
     * parse name locator with odd hex digits
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBadNameLocator() {
        KeyParse.parseLocatorKey("name-hash:0");
    }

    /**
     * parse a locator key with an unknown type
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBadType1() {
        KeyParse.parseFullKey("bad:00");
    }

    /**
     * parse a locator key with an unknown type
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBadType2() {
        KeyParse.parseLocatorKey("bad:00");
    }

}
