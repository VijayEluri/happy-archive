package org.yi.happy.archive.block;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.yi.happy.annotate.SmellsMessy;
import org.yi.happy.archive.Base16;
import org.yi.happy.archive.ByteString;
import org.yi.happy.archive.Bytes;
import org.yi.happy.archive.crypto.CipherFactory;
import org.yi.happy.archive.crypto.CipherProvider;
import org.yi.happy.archive.crypto.DigestFactory;
import org.yi.happy.archive.crypto.DigestProvider;
import org.yi.happy.archive.key.BlobLocatorKey;

/**
 * tests for {@link BlobEncodedBlock}.
 */
public class BlobEncodedBlockTest {
    private static final DigestProvider SHA256 = DigestFactory
            .getProvider("sha-256");

    private static final CipherProvider NULL = CipherFactory
            .getProvider("null");

    private static final Bytes TEST = new Bytes('t', 'e', 's', 't');

    private static final String HASH = "321f47896a9ae31c24d307120f273668"
            + "6d3258d55a4c3890a35ec594049bd49d";

    private static final String BAD_HASH = "321f47896a9ae31c24d307120f273668"
            + "6d3258d55a4c3890a35ec594049bd49e";

    /**
     * Create with minimal detail.
     */
    @Test
    public void test1() {
        Block block = new BlobEncodedBlock(SHA256, NULL, TEST);

        Map<String, String> want = new HashMap<String, String>();
        want.put("key-type", "blob");
        want.put("key", HASH);
        want.put("cipher", "null");
        want.put("digest", "sha-256");
        want.put("size", "4");
        assertEquals(want, block.getMeta());
    }

    /**
     * Create with full detail.
     */
    @Test
    @SmellsMessy
    public void test2() {
        Block block = new BlobEncodedBlock(new BlobLocatorKey(new Bytes(Base16
                .decode(HASH))), SHA256, NULL, TEST);

        Map<String, String> want = new HashMap<String, String>();
        want.put("key-type", "blob");
        want.put("key", HASH);
        want.put("cipher", "null");
        want.put("digest", "sha-256");
        want.put("size", "4");
        assertEquals(want, block.getMeta());
    }

    /**
     * convert to bytes.
     */
    @Test
    public void testAsBytes() {
        Block block = new BlobEncodedBlock(SHA256, NULL, TEST);

        byte[] expect = ByteString.toUtf8("key-type: blob\r\n" + "key: " + HASH
                + "\r\n" + "digest: sha-256\r\n" + "cipher: null\r\n"
                + "size: 4\r\n" + "\r\n" + "test");
        assertArrayEquals(expect, block.asBytes());
    }

    /**
     * Create with inconsistant details.
     */
    @Test(expected = IllegalArgumentException.class)
    @SmellsMessy
    public void test3() {
        new BlobEncodedBlock(new BlobLocatorKey(new Bytes(Base16
                .decode(BAD_HASH))), SHA256, NULL, TEST);
    }
}
