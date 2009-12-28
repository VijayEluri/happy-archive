package org.yi.happy.archive;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.yi.happy.archive.key.HexDecode;
import org.yi.happy.archive.test_data.TestData;

public class EncodedBlockTest {
    @Test
    public void testContent1() {
		TestData d = TestData.KEY_OLD_CONTENT;
        Block b = TestUtil.loadBlock(d);

        EncodedBlock e = EncodedBlockFactory.parse(b);

        assertEquals(d.getLocatorKey(), e.getKey());
        assertEquals("rijndael256-256-cbc", e.getCipher());
        assertEquals("sha-256", e.getDigest());
        assertArrayEquals(b.getBody(), e.getBody());
    }

    @Test
    public void testContent2() {
		TestData d = TestData.KEY_CONTENT;
        Block b = TestUtil.loadBlock(d);

        EncodedBlock e = EncodedBlockFactory.parse(b);

        assertEquals(d.getLocatorKey(), e.getKey());
        assertEquals("rijndael256-256-cbc", e.getCipher());
        assertEquals("sha-256", e.getDigest());
        assertArrayEquals(b.getBody(), e.getBody());
    }

    @Test
    public void testContent2Aes() {
		TestData d = TestData.KEY_CONTENT_AES128;
        Block b = TestUtil.loadBlock(d);

        EncodedBlock e = EncodedBlockFactory.parse(b);

        assertEquals(d.getLocatorKey(), e.getKey());
        assertEquals("aes-128-cbc", e.getCipher());
        assertEquals("sha-256", e.getDigest());
        assertArrayEquals(b.getBody(), e.getBody());
    }

    @Test
    public void testContent2Rijndael() {
		TestData d = TestData.KEY_CONTENT_RIJNDAEL;
        Block b = TestUtil.loadBlock(d);

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
        Block b = TestUtil.loadBlock(d);

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
        Block b = TestUtil.loadBlock(d);
        EncodedBlockFactory.parse(b);
    }

    @Test
    public void errorIsVerify() {
        assertTrue(VerifyException.class
                .isAssignableFrom(ShortBodyException.class));
    }
}
