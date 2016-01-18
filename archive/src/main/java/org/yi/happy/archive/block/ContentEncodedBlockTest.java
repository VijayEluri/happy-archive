package org.yi.happy.archive.block;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.junit.Test;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.FullKeyParse;
import org.yi.happy.archive.test_data.TestData;

/**
 * tests for {@link ContentEncodedBlock}.
 */
public class ContentEncodedBlockTest {
    /**
     * decode an old content encoded block.
     * 
     * @throws IOException
     */
    @Test
    public void testDecodeOldContent() throws IOException {
        EncodedBlock b = TestData.KEY_OLD_CONTENT.getEncodedBlock();

        Block have = b.decode(TestData.KEY_OLD_CONTENT.getFullKey());

        Block want = TestData.CLEAR_CONTENT.getBlock();
        assertEquals(want, have);
    }

    /**
     * decode a content encoded block.
     * 
     * @throws IOException
     */
    @Test
    public void testDecodeContent() throws IOException {
        EncodedBlock b = TestData.KEY_CONTENT.getEncodedBlock();

        Block have = b.decode(TestData.KEY_CONTENT.getFullKey());

        Block want = TestData.CLEAR_CONTENT.getBlock();
        assertEquals(want, have);
    }

    /**
     * decode a new content block with a key with the wrong encryption key, the
     * resulting block will be able to be parsed but will be nonsense.
     * 
     * @throws IOException
     */
    @Test
    public void testContentDecodeBadKey() throws IOException {
        EncodedBlock b = TestData.KEY_CONTENT.getEncodedBlock();

        FullKey key = FullKeyParse.parseFullKey("content-hash:87c5f6fe"
                + "4ea801c8eb227b8b218a0659c18ece76b7c200c645ab4364becf"
                + "68d5:f6bd9f3b01b4ee40f60df2dc622f9d6f3aa38a5673a87e8"
                + "20b40164e930edead");
        Block c = b.decode(key);

        assertFalse(TestData.CLEAR_CONTENT.getBlock().equals(c));
    }

    /**
     * decode a new content block with an encryption key of the wrong length,
     * this will fail to decode.
     * 
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testContentDecodeBadKey2() throws IOException {
        EncodedBlock b = TestData.KEY_CONTENT.getEncodedBlock();
        FullKey key = FullKeyParse.parseFullKey("content-hash:87c5f6fe"
                + "4ea801c8eb227b8b218a0659c18ece76b7c200c645ab4364becf"
                + "68d5:f6bd9f3b01b4ee40f60df2dc622f9d6f");

        b.decode(key);
    }

    /**
     * decode a new content block with the wrong locator, but right pass, this
     * fails since the key is for another block.
     * 
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testContentDecodeBadLocator() throws IOException {
        EncodedBlock b = TestData.KEY_CONTENT.getEncodedBlock();

        FullKey key = FullKeyParse.parseFullKey("content-hash:87c5f6fe"
                + "4ea801c8eb227b8b218a0659c18ece76b7c200c645ab4364becf"
                + "68df:f6bd9f3b01b4ee40f60df2dc622f9d6f3aa38a5673a87e8"
                + "20b40164e930edeac");
        b.decode(key);
    }

}
