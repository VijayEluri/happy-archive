package org.yi.happy.archive;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.Assert;
import org.junit.Test;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.GenericBlockTest;
import org.yi.happy.archive.key.HexDecode;
import org.yi.happy.archive.test_data.TestData;

/**
 * tests on BlockUtil
 */
public class BlockUtilTest {
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
     * check that loading is working properly
     */
    @Test
    public void testLoad() {
	Block have = BlockParse.load(TestData.OK_SMALL.getUrl());

	Block want = GenericBlockTest.createSampleBlock();
	Assert.assertEquals(want, have);
    }
}
