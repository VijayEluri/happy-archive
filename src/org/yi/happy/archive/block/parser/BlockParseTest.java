package org.yi.happy.archive.block.parser;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;
import org.yi.happy.archive.Bytes;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.GenericBlockTest;
import org.yi.happy.archive.test_data.TestData;

/**
 * tests for {@link BlockParse}.
 */
public class BlockParseTest {
    /**
     * parse a content hash block.
     * 
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
	Block block = BlockParse.load(TestData.KEY_CONTENT.getUrl());

	assertEquals("content-hash", block.getMeta().get("key-type"));
    }

    /**
     * A zero byte file is no longer a bad block.
     * 
     * @throws IOException
     */
    @Test
    public void test2() throws IOException {
	Block block = BlockParse.load(TestData.FILE_EMPTY.getUrl());

	assertEquals(Collections.emptyMap(), block.getMeta());
	assertEquals(new Bytes(), block.getBody());
    }

    /**
     * check that loading is working properly
     * 
     * @throws IOException
     */
    @Test
    public void testLoad() throws IOException {
	Block have = BlockParse.load(TestData.OK_SMALL.getUrl());

	Block want = GenericBlockTest.createSampleBlock();
	Assert.assertEquals(want, have);
    }
}
