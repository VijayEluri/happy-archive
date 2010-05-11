package org.yi.happy.archive.key;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.yi.happy.archive.crypto.DigestFactory;
import org.yi.happy.archive.crypto.DigestProvider;

/**
 * tests for {@link FullKeyParse}.
 */
public class KeyParseTest {
    /**
     * parse a full name key
     */
    @Test
    public void testParseFullName() {
        String key = "name-hash:sha256:test";

        FullKey have = FullKeyParse.parseFullKey(key);

        assertTrue(have instanceof NameFullKey);
        NameFullKey want = new NameFullKey(aDigest(), "test");
        assertEquals(want, have);
    }

    private DigestProvider aDigest() {
        return DigestFactory.getProvider("sha256");
    }

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
     * parse a full content key
     */
    @Test
    public void testParseFullContent() {
        FullKey k = FullKeyParse.parseFullKey("content-hash:001122:334455");

        assertEquals("content-hash", k.getType());

        assertTrue(k instanceof ContentFullKey);

        ContentFullKey n = (ContentFullKey) k;

        assertEquals(aHash(), n.getHash());
        assertEquals(aPass(), n.getPass());
    }

    private PassValue aPass() {
        return new PassValue(0x33, 0x44, 0x55);
    }

    /**
     * parse a full content key
     */
    @Test
    public void testParseFullContent2() {
        FullKey k = FullKeyParse.parseFullKey("content-hash:001122:");

        assertEquals("content-hash", k.getType());

        assertTrue(k instanceof ContentFullKey);

        ContentFullKey n = (ContentFullKey) k;

        assertEquals(aHash(), n.getHash());
        assertEquals(new PassValue(), n.getPass());
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
     * parse a full content key
     */
    @Test
    public void testParseFullBlob() {
        FullKey k = FullKeyParse.parseFullKey("blob:001122:334455");

        assertEquals("blob", k.getType());

        assertTrue(k instanceof BlobFullKey);

        BlobFullKey n = (BlobFullKey) k;

        assertEquals(aHash(), n.getHash());
        assertEquals(aPass(), n.getPass());
    }

    /**
     * parse a full content key
     */
    @Test
    public void testParseFullBlob2() {
        FullKey k = FullKeyParse.parseFullKey("blob:001122:");

        assertEquals("blob", k.getType());

        assertTrue(k instanceof BlobFullKey);

        BlobFullKey n = (BlobFullKey) k;

        assertEquals(aHash(), n.getHash());
        assertEquals(new PassValue(), n.getPass());
    }

    /**
     * parse an invalid key
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBad1() {
        FullKeyParse.parseFullKey("content-hash");
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
    public void testBadContentKey1() {
        FullKeyParse.parseFullKey("content-hash:0:0");
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
    public void testBadType1() {
        FullKeyParse.parseFullKey("bad:00");
    }

    /**
     * parse a locator key with an unknown type
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBadType2() {
        LocatorKeyParse.parseLocatorKey("bad:00");
    }
}
