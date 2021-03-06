package org.yi.happy.archive.block.parser;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.ContentEncodedBlock;
import org.yi.happy.archive.block.GenericBlock;
import org.yi.happy.archive.test_data.TestData;

/**
 * tests for {@link ContentEncodedBlockParse}.
 */
public class ContentEncodedBlockParseTest {
    /**
     * parse a version two content encoded block.
     * 
     * @throws IOException
     */
    @Test
    public void testCurrent() throws IOException {
        TestData d = TestData.KEY_CONTENT;
        Block b = d.getBlock();

        ContentEncodedBlock e = ContentEncodedBlockParse.parse(b);

        assertEquals(d.getLocatorKey(), e.getKey());
        assertEquals("rijndael256-256-cbc", e.getCipher().getAlgorithm());
        assertEquals("sha-256", e.getDigest().getAlgorithm());
        assertEquals(b.getBody(), e.getBody());
    }

    /**
     * parse a version one content encoded block.
     * 
     * @throws IOException
     */
    @Test
    public void testOld() throws IOException {
        TestData d = TestData.KEY_OLD_CONTENT;
        Block b = d.getBlock();

        ContentEncodedBlock e = ContentEncodedBlockParse.parse(b);

        assertEquals(d.getLocatorKey(), e.getKey());
        assertEquals("rijndael256-256-cbc", e.getCipher().getAlgorithm());
        assertEquals("sha-256", e.getDigest().getAlgorithm());
        assertEquals(b.getBody(), e.getBody());
    }

    /**
     * parse a version two content encoded block.
     * 
     * @throws IOException
     */
    @Test
    public void testContent2Aes() throws IOException {
        TestData d = TestData.KEY_CONTENT_AES128;
        Block b = d.getBlock();

        ContentEncodedBlock e = ContentEncodedBlockParse.parse(b);

        assertEquals(d.getLocatorKey(), e.getKey());
        assertEquals("aes-128-cbc", e.getCipher().getAlgorithm());
        assertEquals("sha-256", e.getDigest().getAlgorithm());
        assertEquals(b.getBody(), e.getBody());
    }

    /**
     * parse a version two content encoded block.
     * 
     * @throws IOException
     */
    @Test
    public void testContent2Rijndael() throws IOException {
        TestData d = TestData.KEY_CONTENT_RIJNDAEL;
        Block b = d.getBlock();

        ContentEncodedBlock e = ContentEncodedBlockParse.parse(b);

        assertEquals(d.getLocatorKey(), e.getKey());
        assertEquals("rijndael-128-cbc", e.getCipher().getAlgorithm());
        assertEquals("sha-256", e.getDigest().getAlgorithm());
        assertEquals(b.getBody(), e.getBody());
    }

    /**
     * parse a truncated content encoded block.
     * 
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalid() throws IOException {
        TestData d = TestData.BAD_KEY_SHORT_CONTENT;
        Block b = d.getBlock();

        ContentEncodedBlockParse.parse(b);
    }

    /**
     * parse a blank block.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBlank() {
        Block b = new GenericBlock();

        ContentEncodedBlockParse.parse(b);
    }

    /**
     * parse a block that is not a content encoded block.
     * 
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNotContent() throws IOException {
        TestData d = TestData.KEY_BLOB;
        Block b = d.getBlock();

        ContentEncodedBlockParse.parse(b);
    }

}
