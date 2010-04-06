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
        Key k = KeyParse.parseKey("name-hash:001122");

        assertEquals("name-hash", k.getType());
        assertTrue(k instanceof LocatorKey);

        LocatorKey n = (LocatorKey) k;

        assertEquals(new HashValue(0x00, 0x11, 0x22), n.getHash());
    }

    /**
     * parse a content locator
     */
    @Test
    public void testParseContentLocator() {
        Key k = KeyParse.parseKey("content-hash:001122");

        assertEquals("content-hash", k.getType());
        assertTrue(k instanceof LocatorKey);

        LocatorKey n = (LocatorKey) k;

        assertEquals(new HashValue(0x00, 0x11, 0x22), n.getHash());
    }

    /**
     * parse a full content key
     */
    @Test
    public void testParseFullContent() {
        Key k = KeyParse.parseKey("content-hash:001122:334455");

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
        Key k = KeyParse.parseKey("content-hash:001122:");

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
        Key k = KeyParse.parseKey("blob:001122");

        assertEquals("blob", k.getType());
        assertTrue(k instanceof LocatorKey);

        LocatorKey n = (LocatorKey) k;

        assertEquals(new HashValue(0x00, 0x11, 0x22), n.getHash());
    }

    /**
     * parse a full content key
     */
    @Test
    public void testParseFullBlob() {
        Key k = KeyParse.parseKey("blob:001122:334455");

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
        Key k = KeyParse.parseKey("blob:001122:");

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
    public void testBad() {
        KeyParse.parseKey("content-hash");
    }

    /**
     * parse content key with odd hex digits
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBadContentKey() {
        KeyParse.parseKey("content-hash:0:0");
    }

    /**
     * parse content locator with odd hex digits
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBadContentLocator() {
        KeyParse.parseKey("content-hash:0");
    }

    /**
     * parse name locator with odd hex digits
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBadNameLocator() {
        KeyParse.parseKey("name-hash:0");
    }

    /**
     * parse a locator key with an unknown type
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBadType() {
        KeyParse.parseKey("bad:00");
    }

}
