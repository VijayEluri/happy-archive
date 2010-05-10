package org.yi.happy.archive.block.encoder;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.crypto.CipherFactory;
import org.yi.happy.archive.crypto.CipherProvider;
import org.yi.happy.archive.crypto.DigestFactory;
import org.yi.happy.archive.crypto.DigestProvider;
import org.yi.happy.archive.test_data.TestData;

/**
 * tests for block encode
 */
public class BlockEncoderBlobTest {
    /**
     * encode a block using default block encoding
     * 
     * @throws IOException
     */
    @Test
    public void testContentEncode() throws IOException {
        Block in = TestData.CLEAR_CONTENT.getBlock();

        BlockEncoder e = new BlockEncoderBlob(sha256(), rijndael256_256());
        BlockEncoderResult r = e.encode(in);

        Block out = r.getBlock();

        Block want = TestData.KEY_BLOB.getBlock();
        Assert.assertArrayEquals(want.asBytes(), out.asBytes());
        Assert.assertEquals(TestData.KEY_BLOB.getFullKey(), r.getKey());
    }

    /**
     * encode a block with non-default settings
     * 
     * @throws IOException
     */
    @Test
    public void testEncodeNonDefault() throws IOException {
        Block in = TestData.CLEAR_CONTENT.getBlock();
    
        BlockEncoder be = new BlockEncoderBlob(sha1(), aes192());
        BlockEncoderResult r = be.encode(in);
        Block have = r.getBlock();
    
        TestData d = TestData.KEY_BLOB_SHA1_AES192;
        Block want = d.getBlock();
        assertEquals(d.getFullKey(), r.getKey());
        assertArrayEquals(want.asBytes(), have.asBytes());
    }

    /**
     * encode with all default settings (although they are not defaults now).
     * 
     * @throws IOException
     */
    @Test
    public void testEncodeDefault() throws IOException {
        Block in = TestData.CLEAR_CONTENT.getBlock();
    
        BlockEncoder be = new BlockEncoderBlob(sha256(), aes128());
        BlockEncoderResult r = be.encode(in);
        Block have = r.getBlock();
    
        TestData d = TestData.KEY_BLOB_AES128;
        Block want = d.getBlock();
        assertEquals(d.getFullKey(), r.getKey());
        assertArrayEquals(want.asBytes(), have.asBytes());
    }

    private CipherProvider rijndael256_256() {
        return CipherFactory.getProvider("rijndael256-256-cbc");
    }

    private CipherProvider aes192() {
        return CipherFactory.getProvider("aes-192-cbc");
    }

    private DigestProvider sha1() {
        return DigestFactory.getProvider("sha-1");
    }

    private CipherProvider aes128() {
        return CipherFactory.getProvider("aes-128-cbc");
    }

    private DigestProvider sha256() {
        return DigestFactory.getProvider("sha-256");
    }
}
