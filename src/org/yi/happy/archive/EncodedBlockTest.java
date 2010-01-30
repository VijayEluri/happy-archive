package org.yi.happy.archive;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.HexDecode;
import org.yi.happy.archive.key.KeyParse;
import org.yi.happy.archive.key.NameFullKey;
import org.yi.happy.archive.test_data.TestData;

public class EncodedBlockTest {
    @Test
    public void testContent1() {
	TestData d = TestData.KEY_OLD_CONTENT;
	Block b = d.getBlock();

	EncodedBlock e = EncodedBlockFactory.parse(b);

	assertEquals(d.getLocatorKey(), e.getKey());
	assertEquals("rijndael256-256-cbc", e.getCipher());
	assertEquals("sha-256", e.getDigest());
	assertArrayEquals(b.getBody(), e.getBody());
    }

    @Test
    public void testContent2() {
	TestData d = TestData.KEY_CONTENT;
	Block b = d.getBlock();

	EncodedBlock e = EncodedBlockFactory.parse(b);

	assertEquals(d.getLocatorKey(), e.getKey());
	assertEquals("rijndael256-256-cbc", e.getCipher());
	assertEquals("sha-256", e.getDigest());
	assertArrayEquals(b.getBody(), e.getBody());
    }

    @Test
    public void testContent2Aes() {
	TestData d = TestData.KEY_CONTENT_AES128;
	Block b = d.getBlock();

	EncodedBlock e = EncodedBlockFactory.parse(b);

	assertEquals(d.getLocatorKey(), e.getKey());
	assertEquals("aes-128-cbc", e.getCipher());
	assertEquals("sha-256", e.getDigest());
	assertArrayEquals(b.getBody(), e.getBody());
    }

    @Test
    public void testContent2Rijndael() {
	TestData d = TestData.KEY_CONTENT_RIJNDAEL;
	Block b = d.getBlock();

	EncodedBlock e = EncodedBlockFactory.parse(b);

	assertEquals(d.getLocatorKey(), e.getKey());
	assertEquals("rijndael-128-cbc", e.getCipher());
	assertEquals("sha-256", e.getDigest());
	assertArrayEquals(b.getBody(), e.getBody());
    }

    @Test
    public void testBlob() {
	// TODO blob key
    }

    @Test
    public void testName2() {
	TestData d = TestData.KEY_NAME;
	Block b = d.getBlock();

	NameEncodedBlock e = (NameEncodedBlock) EncodedBlockFactory.parse(b);

	assertEquals(d.getLocatorKey(), e.getKey());
	assertEquals("rijndael256-256-cbc", e.getCipher());
	assertArrayEquals(b.getBody(), e.getBody());
	byte[] hash = HexDecode.decode("b73aaf8748cfe48edd270d01517ef5556d0"
		+ "3242af41ca58208c840c82e78651a");
	assertArrayEquals(hash, e.getHash());
    }

    @Test(expected = ShortBodyException.class)
    public void testInvalid() {
	TestData d = TestData.BAD_KEY_SHORT_CONTENT;
	Block b = d.getBlock();
	EncodedBlockFactory.parse(b);
    }

    @Test(expected = VerifyException.class)
    public void testInvalid2() {
	Block b = TestData.OK_SMALL.getBlock();

	EncodedBlockFactory.parse(b);
    }

    @Test(expected = ShortBodyException.class)
    public void testInvalid3() {
	TestData d = TestData.BAD_KEY_SHORT_NAME;
	Block b = d.getBlock();
	EncodedBlockFactory.parse(b);
    }

    /**
     * verify an empty block
     */
    @Test(expected = VerifyException.class)
    public void testVerifyFresh() {
	Block block = new BlockImpl();

	EncodedBlockFactory.parse(block);
    }

    @Test
    public void errorIsVerify() {
	assertTrue(VerifyException.class
		.isAssignableFrom(ShortBodyException.class));
    }

    @Test
    public void testDecodeOldContent() throws IOException {
	EncodedBlock b = TestData.KEY_OLD_CONTENT.getEncodedBlock();

	Block have = b.decode(TestData.KEY_OLD_CONTENT.getFullKey());

	Block want = TestData.CLEAR_CONTENT.getBlock();
	assertEquals(want, have);
    }

    @Test
    public void testDecodeContent() throws IOException {
	EncodedBlock b = TestData.KEY_CONTENT.getEncodedBlock();

	Block have = b.decode(TestData.KEY_CONTENT.getFullKey());

	Block want = TestData.CLEAR_CONTENT.getBlock();
	assertEquals(want, have);
    }

    @Test
    public void testDecodeName() throws IOException {
	EncodedBlock b = TestData.KEY_NAME.getEncodedBlock();

	Block have = b.decode(TestData.KEY_NAME.getFullKey());

	Block want = TestData.CLEAR_CONTENT.getBlock();
	assertEquals(want, have);
    }

    /**
     * decode a new content block with the wrong pass
     * 
     * @throws IOException
     */
    @Test(expected = LoadException.class)
    public void testContentDecodeBadKey() throws IOException {
	EncodedBlock b = TestData.KEY_CONTENT.getEncodedBlock();

	FullKey key = new KeyParse().parseFullKey("content-hash:87c5f6fe"
		+ "4ea801c8eb227b8b218a0659c18ece76b7c200c645ab4364becf"
		+ "68d5:f6bd9f3b01b4ee40f60df2dc622f9d6f3aa38a5673a87e8"
		+ "20b40164e930edead");
	b.decode(key);
    }

    /**
     * decode a new content block with the wrong locator, but right pass, this
     * succeeds.
     * 
     * @throws IOException
     */
    @Test
    public void testContentDecodeBadLocator() throws IOException {
	EncodedBlock b = TestData.KEY_CONTENT.getEncodedBlock();

	FullKey key = new KeyParse().parseFullKey("content-hash:87c5f6fe"
		+ "4ea801c8eb227b8b218a0659c18ece76b7c200c645ab4364becf"
		+ "68df:f6bd9f3b01b4ee40f60df2dc622f9d6f3aa38a5673a87e8"
		+ "20b40164e930edeac");
	Block have = b.decode(key);

	Block want = TestData.CLEAR_CONTENT.getBlock();
	assertEquals(want, have);
    }

    @Test(expected = LoadException.class)
    public void testNameDecodeBad() throws IOException {
	EncodedBlock b = TestData.KEY_NAME.getEncodedBlock();

	NameFullKey k = new NameFullKey("sha-256", "test2");
	b.decode(k);
    }

    @Test
    public void testNameDecode() throws IOException {
	EncodedBlock b = TestData.KEY_NAME.getEncodedBlock();

	Block have = b.decode(TestData.KEY_NAME.getFullKey());

	Block want = TestData.CLEAR_CONTENT.getBlock();
	assertEquals(want, have);
    }
}
