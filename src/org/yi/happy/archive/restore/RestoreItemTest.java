package org.yi.happy.archive.restore;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.test_data.TestData;

public class RestoreItemTest {
    @Test
    public void testLoadData() throws Exception {
        Block block = TestData.KEY_CONTENT.getClearBlock();

        RestoreItem item = RestoreItemFactory.create(null, block);

        assertEquals(true, item.isData());
        assertEquals(false, item.isTodo());

        assertEquals(block, item.getBlock());
    }

    @Test
    public void testLoadMap() throws Exception {
        Block block = TestData.KEY_CONTENT_MAP.getClearBlock();

        RestoreItem item = RestoreItemFactory.create(null, block);

        assertEquals(false, item.isData());
        assertEquals(false, item.isTodo());

        assertEquals(2, item.count());

        assertEquals(TestData.KEY_CONTENT_1.getFullKey(), item.getKey(0));
        assertEquals(0, item.getOffset(0));
        assertEquals(true, item.get(0).isTodo());

        assertEquals(TestData.KEY_CONTENT_2.getFullKey(), item.getKey(1));
        assertEquals(5, item.getOffset(1));
        assertEquals(true, item.get(1).isTodo());

        assertEquals(block, item.getBlock());
    }

    @Test
    public void testLoadList() throws Exception {
        Block block = TestData.KEY_CONTENT_LIST.getClearBlock();

        RestoreItem item = RestoreItemFactory.create(null, block);

        assertEquals(false, item.isData());
        assertEquals(false, item.isTodo());

        assertEquals(2, item.count());

        assertEquals(TestData.KEY_CONTENT_1.getFullKey(), item.getKey(0));
        assertEquals(0, item.getOffset(0));
        assertEquals(true, item.get(0).isTodo());

        assertEquals(TestData.KEY_CONTENT_2.getFullKey(), item.getKey(1));
        assertEquals(-1, item.getOffset(1));
        assertEquals(true, item.get(1).isTodo());

        assertEquals(block, item.getBlock());
    }

    @Test
    public void testLoadSplit() throws Exception {
        Block block = TestData.KEY_NAME_SPLIT.getClearBlock();
        FullKey key = TestData.KEY_NAME_SPLIT.getFullKey();

        RestoreItem item = RestoreItemFactory.create(key, block);

        assertEquals(false, item.isData());
        assertEquals(false, item.isTodo());

        assertEquals(2, item.count());

        assertEquals(TestData.KEY_NAME_SPLIT_1.getFullKey(), item.getKey(0));
        assertEquals(0, item.getOffset(0));
        assertEquals(true, item.get(0).isTodo());

        assertEquals(TestData.KEY_NAME_SPLIT_2.getFullKey(), item.getKey(1));
        assertEquals(-1, item.getOffset(1));
        assertEquals(true, item.get(1).isTodo());

        assertEquals(block, item.getBlock());
    }

    @Test
    public void testLoadIndirect() throws Exception {
        Block block = TestData.KEY_NAME_SPLIT_1.getClearBlock();
        FullKey key = TestData.KEY_NAME_SPLIT_1.getFullKey();

        RestoreItem item = RestoreItemFactory.create(key, block);

        assertEquals(false, item.isData());
        assertEquals(false, item.isTodo());

        assertEquals(1, item.count());

        assertEquals(TestData.KEY_CONTENT_1.getFullKey(), item.getKey(0));
        assertEquals(0, item.getOffset(0));
        assertEquals(true, item.get(0).isTodo());

        assertEquals(block, item.getBlock());
    }

}
