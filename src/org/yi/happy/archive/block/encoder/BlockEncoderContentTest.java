package org.yi.happy.archive.block.encoder;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.crypto.CipherFactory;
import org.yi.happy.archive.crypto.DigestFactory;
import org.yi.happy.archive.test_data.TestData;

public class BlockEncoderContentTest {
    @Test
    public void test() throws IOException {
	Block in = TestData.CLEAR_CONTENT.getBlock();

	BlockEncoder e = new BlockEncoderContent(DigestFactory
		.getProvider("sha-256"), CipherFactory
		.getProvider("rijndael256-256-cbc"));

	BlockEncoderResult out = e.encode(in);

	EncodedBlock want = TestData.KEY_CONTENT.getEncodedBlock();
	assertEquals(TestData.KEY_CONTENT.getFullKey(), out.getKey());
	assertArrayEquals(want.asBytes(), out.getBlock().asBytes());
    }
}
