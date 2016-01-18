package org.yi.happy.archive.block;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;
import org.yi.happy.archive.crypto.DigestFactory;
import org.yi.happy.archive.key.NameFullKey;
import org.yi.happy.archive.test_data.TestData;

/**
 * tests for {@link NameEncodedBlock}.
 */
public class NameEncodedBlockTest {
    /**
     * decode a name encoded block.
     * 
     * @throws IOException
     */
    @Test
    public void testDecodeName() throws IOException {
        EncodedBlock b = TestData.KEY_NAME.getEncodedBlock();

        Block have = b.decode(TestData.KEY_NAME.getFullKey());

        Block want = TestData.CLEAR_CONTENT.getBlock();
        assertEquals(want, have);
    }

    /**
     * Decode a name encoded block using the wrong key, with the new tolerant
     * parser the resulting block will parse but will be nonsense.
     * 
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNameDecodeBad() throws IOException {
        EncodedBlock b = TestData.KEY_NAME.getEncodedBlock();

        NameFullKey k = new NameFullKey(DigestFactory.getProvider("sha-256"),
                "test2");
        b.decode(k);
    }

    /**
     * given a name encoded block in memory; when I decode it with the right
     * key; then it matches the clear version of the block.
     * 
     * @throws IOException
     */
    @Test
    public void testNameDecode() throws IOException {
        EncodedBlock b = TestData.KEY_NAME.getEncodedBlock();

        Block have = b.decode(TestData.KEY_NAME.getFullKey());

        Block want = TestData.CLEAR_CONTENT.getBlock();
        assertEquals(want, have);
    }
}
