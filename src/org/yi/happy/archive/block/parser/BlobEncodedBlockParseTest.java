package org.yi.happy.archive.block.parser;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;
import org.yi.happy.archive.block.BlobEncodedBlock;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.GenericBlock;
import org.yi.happy.archive.test_data.TestData;

/**
 * tests for {@link BlobEncodedBlockParse}.
 */
public class BlobEncodedBlockParseTest {
    /**
     * parse a blob block.
     * 
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
	TestData d = TestData.KEY_BLOB;
	Block b = d.getBlock();

	BlobEncodedBlock e = BlobEncodedBlockParse.parse(b);

	assertEquals(d.getLocatorKey(), e.getKey());
	assertEquals("rijndael256-256-cbc", e.getCipher().getAlgorithm());
	assertEquals("sha-256", e.getDigest().getAlgorithm());
	assertEquals(b.getBody(), e.getBody());
    }

    /**
     * parse a blob block.
     * 
     * @throws IOException
     */
    @Test
    public void test2() throws IOException {
	TestData d = TestData.KEY_BLOB_AES128;
	Block b = d.getBlock();

	BlobEncodedBlock e = BlobEncodedBlockParse.parse(b);

	assertEquals(d.getLocatorKey(), e.getKey());
	assertEquals("aes-128-cbc", e.getCipher().getAlgorithm());
	assertEquals("sha-256", e.getDigest().getAlgorithm());
	assertEquals(b.getBody(), e.getBody());
    }

    /**
     * parse a blob block.
     * 
     * @throws IOException
     */
    @Test
    public void test3() throws IOException {
	TestData d = TestData.KEY_BLOB_SHA1_AES192;
	Block b = d.getBlock();

	BlobEncodedBlock e = BlobEncodedBlockParse.parse(b);

	assertEquals(d.getLocatorKey(), e.getKey());
	assertEquals("aes-192-cbc", e.getCipher().getAlgorithm());
	assertEquals("sha-1", e.getDigest().getAlgorithm());
	assertEquals(b.getBody(), e.getBody());
    }

    /**
     * try to parse a content block as a blob block.
     * 
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNotBlob() throws IOException {
	TestData d = TestData.KEY_CONTENT;
	Block b = d.getBlock();

	BlobEncodedBlockParse.parse(b);
    }

    /**
     * try to parse a blank block as a blob block.
     * 
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBlank() throws IOException {
	Block b = new GenericBlock();

	BlobEncodedBlockParse.parse(b);
    }
}
