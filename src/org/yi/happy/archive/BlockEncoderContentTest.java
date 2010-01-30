package org.yi.happy.archive;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;
import org.yi.happy.archive.test_data.TestData;

public class BlockEncoderContentTest {
    @Test
    public void test() throws IOException {
	Block in = TestData.CLEAR_CONTENT.getBlock();

	BlockEncoder e = new BlockEncoderContent(DigestFactory
		.create("sha-256"), CipherFactory
		.createNamed("rijndael256-256-cbc"));

	BlockEncoderResult out = e.encode(in);

	EncodedBlock want = TestData.KEY_CONTENT.getEncodedBlock();
	assertEquals(TestData.KEY_CONTENT.getFullKey(), out.getKey());
	assertArrayEquals(want.asBytes(), out.getBlock().asBytes());
    }
}
