package org.yi.happy.archive.block.encoder;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;
import org.yi.happy.archive.CipherFactory;
import org.yi.happy.archive.DigestFactory;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.test_data.TestData;

/**
 * tests for block encode
 */
public class BlockEncoderBlobTest {
    /**
     * encode a block using default block encoding
     */
    @Test
    public void testContentEncode() {
	Block in = TestData.CLEAR_CONTENT.getBlock();

	BlockEncoder e = new BlockEncoderBlob(DigestFactory.create("sha-256"),
		CipherFactory.create("rijndael256-256-cbc"));
	BlockEncoderResult r = e.encode(in);
	Block out = r.getBlock();

	Block want = TestData.KEY_BLOB.getBlock();
	Assert.assertArrayEquals(want.asBytes(), out.asBytes());
	Assert.assertEquals(TestData.KEY_BLOB.getFullKey(), r.getKey());
    }

    /**
     * encode a block with non-default settings
     */
    @Test
    public void testEncodeNonDefault() {
	Block in = TestData.CLEAR_CONTENT.getBlock();

	BlockEncoder be = new BlockEncoderBlob(DigestFactory.create("sha-1"),
		CipherFactory.create("aes-192-cbc"));
	BlockEncoderResult r = be.encode(in);
	Block have = r.getBlock();

	TestData d = TestData.KEY_BLOB_SHA1_AES192;
	Block want = d.getBlock();
	assertEquals(d.getFullKey(), r.getKey());
	assertArrayEquals(want.asBytes(), have.asBytes());
    }

    /**
     * encode with all default settings
     */
    @Test
    public void testEncodeDefault() {
	Block in = TestData.CLEAR_CONTENT.getBlock();

	BlockEncoder be = new BlockEncoderBlob(DigestFactory.create("sha-256"),
		CipherFactory.create("aes-128-cbc"));
	BlockEncoderResult r = be.encode(in);
	Block have = r.getBlock();

	TestData d = TestData.KEY_BLOB_AES128;
	Block want = d.getBlock();
	assertEquals(d.getFullKey(), r.getKey());
	assertArrayEquals(want.asBytes(), have.asBytes());
    }
}
