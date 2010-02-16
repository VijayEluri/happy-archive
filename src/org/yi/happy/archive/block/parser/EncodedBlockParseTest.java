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

/**
 * Tests for {@link EncodedBlockParse}.
 */
public class EncodedBlockParseTest {
    /**
     * parse a content encoded block.
     * 
     * @throws IOException
     */
    @Test
    public void testContent() throws IOException {
	TestData d = TestData.KEY_CONTENT;
	Block b = d.getBlock();
	
	EncodedBlock e = EncodedBlockParse.parse(b);
	
	assertTrue(e instanceof ContentEncodedBlock);
    }

    /**
     * parse a blob encoded block.
     * 
     * @throws IOException
     */
    @Test
    public void testBlob() throws IOException {
	TestData d = TestData.KEY_BLOB;
	Block b = d.getBlock();

	EncodedBlock e = EncodedBlockParse.parse(b);

	assertTrue(e instanceof BlobEncodedBlock);
    }

    /**
     * parse a name encoded block.
     * 
     * @throws IOException
     */
    @Test
    public void testName() throws IOException {
	TestData d = TestData.KEY_NAME;
	Block b = d.getBlock();

	EncodedBlock e = EncodedBlockParse.parse(b);

	assertTrue(e instanceof NameEncodedBlock);
    }

    /**
     * try to parse a block that is not an encoded block.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBlank() {
	Block b = new GenericBlock();

	EncodedBlockParse.parse(b);
    }

}
