package org.yi.happy.archive;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.Assert;
import org.junit.Test;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.HexDecode;
import org.yi.happy.archive.key.KeyParse;
import org.yi.happy.archive.key.NameFullKey;
import org.yi.happy.archive.test_data.TestData;

/**
 * tests on BlockUtil
 */
public class BlockUtilTest {
    /**
     * parse an empty block
     */
    @Test(expected = LoadException.class)
    public void testLoadEmpty() {
	TestUtil.loadBlock(TestData.BAD_EMPTY);
    }

    /**
     * verify a non-encrypted block
     */
    @Test(expected = VerifyException.class)
    public void testVerifyClear() {
	Block block = TestUtil.loadBlock(TestData.OK_SMALL);

	BlockUtil.verify(block);
    }

    /**
     * verify a version 1 content hash block
     */
    @Test
    public void testVerifyOldContent() {
	Block block = TestUtil.loadBlock(TestData.KEY_OLD_CONTENT);

	BlockUtil.verify(block);
    }

    /**
     * verify a version 2 content hash block
     */
    @Test
    public void testVerifyContent() {
	Block block = TestUtil.loadBlock(TestData.KEY_CONTENT);

	BlockUtil.verify(block);

	// passed
    }

    /**
     * verify a version 2 name key block
     */
    @Test
    public void testVerifyName() {
	Block block = TestUtil.loadBlock(TestData.KEY_NAME);

	BlockUtil.verify(block);

	// passed
    }

    /**
     * verify a cut off content hash block
     */
    @Test(expected = VerifyException.class)
    public void testVerifyBadContent() {
	Block block = TestUtil.loadBlock(TestData.BAD_KEY_SHORT_CONTENT);

	BlockUtil.verify(block);
    }

    /**
     * verify a cut off name hash block
     */
    @Test(expected = VerifyException.class)
    public void testVerifyBadName() {
	Block block = TestUtil.loadBlock(TestData.BAD_KEY_SHORT_NAME);

	BlockUtil.verify(block);
    }

    /**
     * decrypt an old content block
     */
    @Test
    public void testDecodeOldContent() {
	Block in = TestUtil.loadBlock(TestData.KEY_OLD_CONTENT);

	Block out = BlockUtil.decode(in, TestData.KEY_OLD_CONTENT.getFullKey());

	Block want = TestUtil.loadBlock(TestData.CLEAR_CONTENT);
	Assert.assertEquals(want, out);
    }

    /**
     * decode a name key block
     */
    @Test
    public void testDecodeName() {
	Block in = TestUtil.loadBlock(TestData.KEY_NAME);

	Block out = BlockUtil.decode(in, TestData.KEY_NAME.getFullKey());

	Block want = TestUtil.loadBlock(TestData.CLEAR_CONTENT);
	Assert.assertEquals(want, out);
    }

    /**
     * decode a new content block
     */
    @Test
    public void testDecodeContent() {
	Block in = TestUtil.loadBlock(TestData.KEY_CONTENT);

	Block out = BlockUtil.decode(in, TestData.KEY_CONTENT.getFullKey());

	Block want = TestUtil.loadBlock(TestData.CLEAR_CONTENT);
	Assert.assertEquals(want, out);
    }

    /**
     * decode a new content block with the wrong pass
     */
    @Test(expected = LoadException.class)
    public void testContentDecodeBadKey() {
	Block in = TestUtil.loadBlock(TestData.KEY_CONTENT);
	FullKey key = new KeyParse().parseFullKey("content-hash:87c5f6fe"
		+ "4ea801c8eb227b8b218a0659c18ece76b7c200c645ab4364becf"
		+ "68d5:f6bd9f3b01b4ee40f60df2dc622f9d6f3aa38a5673a87e8"
		+ "20b40164e930edead");
	BlockUtil.decode(in, key);
    }

    /**
     * decode a new content block with the wrong locator, but right pass, this
     * succeeds.
     */
    @Test()
    public void testContentDecodeBadLocator() {
	Block in = TestUtil.loadBlock(TestData.KEY_CONTENT);

	FullKey key = new KeyParse().parseFullKey("content-hash:87c5f6fe"
		+ "4ea801c8eb227b8b218a0659c18ece76b7c200c645ab4364becf"
		+ "68df:f6bd9f3b01b4ee40f60df2dc622f9d6f3aa38a5673a87e8"
		+ "20b40164e930edeac");
	Block out = BlockUtil.decode(in, key);

	Block want = TestUtil.loadBlock(TestData.CLEAR_CONTENT);
	Assert.assertEquals(want, out);
    }

    /**
     * decode a name key block with the wrong key
     */
    @Test(expected = LoadException.class)
    public void testNameDecodeBad() {
	Block in = TestUtil.loadBlock(TestData.KEY_NAME);
	BlockUtil.decode(in, new NameFullKey("sha-256", "blah2"));
    }

    /**
     * decode a name key block
     */
    @Test
    public void testNameDecode2() {
	TestData data = TestData.KEY_NAME_MD5_AES192;
	Block in = TestUtil.loadBlock(data);

	Block out = BlockUtil.decode(in, data.getFullKey());

	Block want = TestUtil.loadBlock(TestData.CLEAR_CONTENT);
	Assert.assertEquals(want, out);
    }

    /**
     * check that expanding a key actually works, make a key longer than the
     * hash.
     * 
     * @throws NoSuchAlgorithmException
     */
    @Test
    public void testMakeKey1() throws NoSuchAlgorithmException {
	MessageDigest digest = MessageDigest.getInstance("sha-256");
	byte[] data = "hi".getBytes();
	int size = 36;

	byte[] got = BlockUtil.expandKey(digest, data, size);

	byte[] want = HexDecode.decode("e6908025cd50ce380feecfeaedb70ba2c2f"
		+ "701cc5e314b7b70ef5e1c04b0ec5838543551");
	Assert.assertArrayEquals(want, got);
    }

    /**
     * expand an empty string to a key shorter than the digest.
     * 
     * @throws NoSuchAlgorithmException
     */
    @Test
    public void testMakeKey2() throws NoSuchAlgorithmException {
	MessageDigest digest = MessageDigest.getInstance("sha-256");
	byte[] data = new byte[0];
	int size = 16;

	byte[] got = BlockUtil.expandKey(digest, data, size);

	byte[] want = HexDecode.decode("6e340b9cffb37a989ca544e6bb780a2c");
	Assert.assertArrayEquals(want, got);
    }

    /**
     * expand some data to the size of the digest.
     * 
     * @throws NoSuchAlgorithmException
     */
    @Test
    public void testMakeKey3() throws NoSuchAlgorithmException {
	MessageDigest digest = MessageDigest.getInstance("sha-256");
	byte[] data = "hi/1".getBytes();
	int size = 32;

	byte[] got = BlockUtil.expandKey(digest, data, size);

	byte[] want = HexDecode.decode("fe9682fa0c996d8e0a9b24cd5990ffb"
		+ "ea3476d5d14847826e6af0a481b83cd75");
	Assert.assertArrayEquals(want, got);
    }

    /**
     * expand some data to the size of the digest.
     * 
     * @throws NoSuchAlgorithmException
     */
    @Test
    public void testMakeKey4() throws NoSuchAlgorithmException {
	MessageDigest digest = MessageDigest.getInstance("sha-256");
	byte[] data = "hi".getBytes();
	int size = 32;

	byte[] got = BlockUtil.expandKey(digest, data, size);

	byte[] want = HexDecode.decode("e6908025cd50ce380feecfeaedb70ba2c2f"
		+ "701cc5e314b7b70ef5e1c04b0ec58");
	Assert.assertArrayEquals(want, got);
    }

    /**
     * verify an empty block
     */
    @Test(expected = VerifyException.class)
    public void testVerifyFresh() {
	Block block = new BlockImpl();

	BlockUtil.verify(block);
    }

    /**
     * check that loading is working properly
     */
    @Test
    public void testLoad() {
	Block have = BlockParse.load(TestData.OK_SMALL.getUrl());

	Block want = BlockImplTest.createSampleBlock();
	Assert.assertEquals(want, have);
    }
}
