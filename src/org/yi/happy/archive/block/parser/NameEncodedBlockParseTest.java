package org.yi.happy.archive.block.parser;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;
import org.yi.happy.archive.Bytes;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.GenericBlock;
import org.yi.happy.archive.block.NameEncodedBlock;
import org.yi.happy.archive.test_data.TestData;

/**
 * tests for {@link NameEncodedBlockParse}.
 */
public class NameEncodedBlockParseTest {
    /**
     * parse a version two name encoded block.
     * 
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        TestData d = TestData.KEY_NAME;
        Block b = d.getBlock();

        NameEncodedBlock e = NameEncodedBlockParse.parse(b);

        assertEquals(d.getLocatorKey(), e.getKey());
        assertEquals("sha-256", e.getDigest().getAlgorithm());
        assertEquals("rijndael256-256-cbc", e.getCipher().getAlgorithm());
        assertEquals(new Bytes(0xb7, 0x3a, 0xaf, 0x87, 0x48, 0xcf, 0xe4, 0x8e,
                0xdd, 0x27, 0x0d, 0x01, 0x51, 0x7e, 0xf5, 0x55, 0x6d, 0x03,
                0x24, 0x2a, 0xf4, 0x1c, 0xa5, 0x82, 0x08, 0xc8, 0x40, 0xc8,
                0x2e, 0x78, 0x65, 0x1a), e.getHash());
        assertEquals(b.getBody(), e.getBody());
    }

    /**
     * parse a version two name encoded block.
     * 
     * @throws IOException
     */
    @Test
    public void test2() throws IOException {
        TestData d = TestData.KEY_NAME_MD5_AES192;
        Block b = d.getBlock();

        NameEncodedBlock e = NameEncodedBlockParse.parse(b);

        assertEquals(d.getLocatorKey(), e.getKey());
        assertEquals("md5", e.getDigest().getAlgorithm());
        assertEquals("aes-192-cbc", e.getCipher().getAlgorithm());
        assertEquals(new Bytes(0xc8, 0x36, 0x28, 0x75, 0x34, 0x63, 0x14, 0x54,
                0x6f, 0x8f, 0x50, 0x26, 0xfc, 0xd4, 0x77, 0x38), e.getHash());
        assertEquals(b.getBody(), e.getBody());
    }

    /**
     * try to parse an empty block.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBad() {
        Block b = new GenericBlock();

        NameEncodedBlockParse.parse(b);
    }

    /**
     * try to parse a content block.
     * 
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWrongType() throws IOException {
        TestData d = TestData.KEY_CONTENT;
        Block b = d.getBlock();

        NameEncodedBlockParse.parse(b);
    }

    /**
     * try to parse a truncated name block.
     * 
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testShort() throws IOException {
        TestData d = TestData.BAD_KEY_SHORT_NAME;
        Block b = d.getBlock();

        NameEncodedBlockParse.parse(b);
    }
}
