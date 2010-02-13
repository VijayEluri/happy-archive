package org.yi.happy.archive.block.parser;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.yi.happy.archive.block.BlobEncodedBlock;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.ContentEncodedBlock;
import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.block.GenericBlock;
import org.yi.happy.archive.block.NameEncodedBlock;
import org.yi.happy.archive.test_data.TestData;

public class EncodedBlockParseTest {
    @Test
    public void testContent() throws IOException {
	TestData d = TestData.KEY_CONTENT;
	Block b = d.getBlock();
	
	EncodedBlock e = EncodedBlockParse.parse(b);
	
	assertTrue(e instanceof ContentEncodedBlock);
    }

    @Test
    public void testBlob() throws IOException {
	TestData d = TestData.KEY_BLOB;
	Block b = d.getBlock();

	EncodedBlock e = EncodedBlockParse.parse(b);

	assertTrue(e instanceof BlobEncodedBlock);
    }

    @Test
    public void testName() throws IOException {
	TestData d = TestData.KEY_NAME;
	Block b = d.getBlock();

	EncodedBlock e = EncodedBlockParse.parse(b);

	assertTrue(e instanceof NameEncodedBlock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlank() {
	Block b = new GenericBlock();

	EncodedBlockParse.parse(b);
    }

}
