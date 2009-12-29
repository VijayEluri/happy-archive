package org.yi.happy.archive;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;
import org.yi.happy.archive.test_data.TestData;

/**
 * tests for block encode
 */
public class BlockEncodeTest {
    /**
     * encode a block using default block encoding
     */
    @Test
    public void testContentEncode() {
        Block in = TestUtil.loadClear();

        BlockEncoderImpl e = new BlockEncoderImpl();
        e.setDigestName("sha-256");
        e.setCipherName("rijndael256-256-cbc");
        BlockEncoderResult r = e.encode(in);
        Block out = r.getBlock();

		Block want = TestUtil.loadBlock(TestData.KEY_BLOB);
        Assert.assertArrayEquals(want.asBytes(), out.asBytes());
		Assert.assertEquals(TestData.KEY_BLOB.getFullKey(), r.getKey());
    }

    /**
     * setting a bad cipher should fail right away
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBadCipher() {
        BlockEncoderImpl e = new BlockEncoderImpl();
        e.setCipherName("bad");
    }

    /**
     * setting a bad digest should fail right away
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBadDigest() {
        BlockEncoderImpl e = new BlockEncoderImpl();
        e.setDigestName("bad");
    }

    /**
     * setting a bad digest should not change the active one
     */
    @Test
    public void testKeepDigest() {
        BlockEncoderImpl be = new BlockEncoderImpl();
        be.setDigestName("sha-1");
        Assert.assertEquals("sha-1", be.getDigestName());

        try {
            be.setDigestName("bad");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // swallow
        }

        Assert.assertEquals("sha-1", be.getDigestName());
    }

    /**
     * setting a bad cipher should not change the active one
     */
    @Test
    public void testKeepCipher() {
        BlockEncoderImpl be = new BlockEncoderImpl();
        be.setCipherName("aes-192-cbc");
        Assert.assertEquals("aes-192-cbc", be.getCipherName());

        try {
            be.setDigestName("bad");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // swallow
        }

        Assert.assertEquals("aes-192-cbc", be.getCipherName());
    }

    /**
     * encode a block with non-default settings
     */
    @Test
    public void testEncodeNonDefault() {
		Block in = TestUtil.loadClear();

        BlockEncoderImpl be = new BlockEncoderImpl();
        be.setCipherName("aes-192-cbc");
        be.setDigestName("sha-1");
        BlockEncoderResult r = be.encode(in);
        Block have = r.getBlock();

		TestData d = TestData.KEY_BLOB_SHA1_AES192;
		Block want = TestUtil.loadBlock(d);
        assertEquals(d.getFullKey(), r.getKey());
        assertArrayEquals(want.asBytes(), have.asBytes());
    }

    /**
     * encode with all default settings
     */
    @Test
    public void testEncodeDefault() {
		Block in = TestUtil.loadClear();

        BlockEncoderImpl be = new BlockEncoderImpl();
        BlockEncoderResult r = be.encode(in);
        Block have = r.getBlock();

		TestData d = TestData.KEY_BLOB_AES128;
		Block want = TestUtil.loadBlock(d);
        assertEquals(d.getFullKey(), r.getKey());
        assertArrayEquals(want.asBytes(), have.asBytes());
    }
}
