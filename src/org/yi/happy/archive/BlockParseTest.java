package org.yi.happy.archive;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.yi.happy.archive.test_data.TestData;

public class BlockParseTest {
    @Test
    public void test1() {
		Block block = BlockParse.load(TestData.KEY_CONTENT.getUrl());

        assertEquals("content-hash", block.getMeta("key-type"));
    }
}
