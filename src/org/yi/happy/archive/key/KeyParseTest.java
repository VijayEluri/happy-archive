package org.yi.happy.archive.key;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class KeyParseTest {
    private KeyParse parse;

    @Before
    public void before() {
        parse = new KeyParse();
    }

    @After
    public void after() {
        parse = null;
    }

    /**
     * parse a full name key
     */
    @Test
    public void testParseFullName() {
        FullKey k = parse.parseFullKey("name-hash:sha-256:test");

        assertEquals("name-hash", k.getType());
        assertTrue(k instanceof NameFullKey);

        NameFullKey n = (NameFullKey) k;

        assertEquals("sha-256", n.getDigest());
        assertEquals("test", n.getName());
    }

    /**
     * parse a name locator
     */
    @Test
    public void testParseNameLocator() {
        Key k = parse.parseKey("name-hash:001122");

        assertEquals("name-hash", k.getType());
        assertTrue(k instanceof LocatorKey);

        LocatorKey n = (LocatorKey) k;

        assertArrayEquals(new byte[] { 0x00, 0x11, 0x22 }, n.getHash());
    }

    /**
     * parse a content locator
     */
    @Test
    public void testParseContentLocator() {
        Key k = parse.parseKey("content-hash:001122");

        assertEquals("content-hash", k.getType());
        assertTrue(k instanceof LocatorKey);

        LocatorKey n = (LocatorKey) k;

        assertArrayEquals(new byte[] { 0x00, 0x11, 0x22 }, n.getHash());
    }

    /**
     * parse a full content key
     */
    @Test
    public void testParseFullContent() {
        Key k = parse.parseKey("content-hash:001122:334455");

        assertEquals("content-hash", k.getType());

        assertTrue(k instanceof ContentFullKey);

        ContentFullKey n = (ContentFullKey) k;

        assertArrayEquals(new byte[] { 0x00, 0x11, 0x22 }, n.getHash());
        assertArrayEquals(new byte[] { 0x33, 0x44, 0x55 }, n.getPass());
    }

    /**
     * parse a full content key
     */
    @Test
    public void testParseFullContent2() {
        Key k = parse.parseKey("content-hash:001122:");

        assertEquals("content-hash", k.getType());

        assertTrue(k instanceof ContentFullKey);

        ContentFullKey n = (ContentFullKey) k;

        assertArrayEquals(new byte[] { 0x00, 0x11, 0x22 }, n.getHash());
        assertArrayEquals(new byte[] {}, n.getPass());
    }

    /**
     * parse a content locator
     */
    @Test
    public void testParseBlobLocator() {
        Key k = parse.parseKey("blob:001122");

        assertEquals("blob", k.getType());
        assertTrue(k instanceof LocatorKey);

        LocatorKey n = (LocatorKey) k;

        assertArrayEquals(new byte[] { 0x00, 0x11, 0x22 }, n.getHash());
    }

    /**
     * parse a full content key
     */
    @Test
    public void testParseFullBlob() {
        Key k = parse.parseKey("blob:001122:334455");

        assertEquals("blob", k.getType());

        assertTrue(k instanceof BlobFullKey);

        BlobFullKey n = (BlobFullKey) k;

        assertArrayEquals(new byte[] { 0x00, 0x11, 0x22 }, n.getHash());
        assertArrayEquals(new byte[] { 0x33, 0x44, 0x55 }, n.getPass());
    }

    /**
     * parse a full content key
     */
    @Test
    public void testParseFullBlob2() {
        Key k = parse.parseKey("blob:001122:");

        assertEquals("blob", k.getType());

        assertTrue(k instanceof BlobFullKey);

        BlobFullKey n = (BlobFullKey) k;

        assertArrayEquals(new byte[] { 0x00, 0x11, 0x22 }, n.getHash());
        assertArrayEquals(new byte[] {}, n.getPass());
    }

    /**
     * parse an invalid key
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBad() {
        parse.parseKey("content-hash");
    }

    /**
     * parse content key with odd hex digits
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBadContentKey() {
        parse.parseKey("content-hash:0:0");
    }

    /**
     * parse content locator with odd hex digits
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBadContentLocator() {
        parse.parseKey("content-hash:0");
    }

    /**
     * parse name locator with odd hex digits
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBadNameLocator() {
        parse.parseKey("name-hash:0");
    }

    /**
     * parse a locator key with an unknown type
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBadType() {
        parse.parseKey("bad:00");
    }

}
