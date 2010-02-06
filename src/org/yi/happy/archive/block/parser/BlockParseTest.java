package org.yi.happy.archive.block.parser;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.GenericBlockTest;
import org.yi.happy.archive.test_data.TestData;

public class BlockParseTest {
    @Test
    public void test1() {
	Block block = BlockParse.load(TestData.KEY_CONTENT.getUrl());

	assertEquals("content-hash", block.getMeta().get("key-type"));
    }

    @Test(expected = LoadException.class)
    public void test2() {
	BlockParse.load(TestData.BAD_EMPTY.getUrl());
    }

    /**
     * check that loading is working properly
     */
    @Test
    public void testLoad() {
	Block have = BlockParse.load(TestData.OK_SMALL.getUrl());

	Block want = GenericBlockTest.createSampleBlock();
	Assert.assertEquals(want, have);
    }

}
