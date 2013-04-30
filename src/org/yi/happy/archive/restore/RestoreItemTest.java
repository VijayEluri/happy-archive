package org.yi.happy.archive.restore;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link RestoreItem} and {@link RestoreItemFactory}.
 */
public class RestoreItemTest {
    private static final TestData DATA = TestData.KEY_CONTENT;

    /**
     * Load a data block.
     * 
     * @throws Exception
     */
    @Test
    public void testLoadData() throws Exception {

        RestoreItem item = load(DATA);

        assertEquals(true, item.isData());
        assertEquals(false, item.isTodo());

        assertEquals(0, item.count());

        assertEquals(DATA.getClearBlock(), item.getBlock());
    }

    private static final TestData MAP = TestData.KEY_CONTENT_MAP;
    private static final TestData MAP_0 = TestData.KEY_CONTENT_1;
    private static final TestData MAP_1 = TestData.KEY_CONTENT_2;

    /**
     * Load a map block.
     * 
     * @throws Exception
     */
    @Test
    public void testLoadMap() throws Exception {

        RestoreItem item = load(MAP);

        assertEquals(false, item.isData());
        assertEquals(false, item.isTodo());

        assertEquals(2, item.count());

        assertEquals(MAP_0.getFullKey(), item.getKey(0));
        assertEquals(0, item.getOffset(0));
        assertEquals(true, item.get(0).isTodo());

        assertEquals(MAP_1.getFullKey(), item.getKey(1));
        assertEquals(5, item.getOffset(1));
        assertEquals(true, item.get(1).isTodo());

        assertEquals(MAP.getClearBlock(), item.getBlock());
    }

    @Test
    public void testSetMap() throws Exception {
        RestoreItem item = load(MAP);

        item.set(0, load(MAP_0));
        assertEquals(false, item.get(0).isTodo());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetMapBad() throws Exception {
        RestoreItem item = load(MAP);

        item.setOffset(0, 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetMapBad2() throws Exception {
        RestoreItem item = load(MAP);

        item.set(0, load(MAP_0));
        assertEquals(false, item.get(0).isTodo());

        item.set(0, new RestoreTodo());
    }

    private static final TestData LIST = TestData.KEY_CONTENT_LIST;
    private static final TestData LIST_0 = TestData.KEY_CONTENT_1;
    private static final TestData LIST_1 = TestData.KEY_CONTENT_2;

    @Test
    public void testLoadList() throws Exception {

        RestoreItem item = load(LIST);

        assertEquals(false, item.isData());
        assertEquals(false, item.isTodo());

        assertEquals(2, item.count());

        assertEquals(LIST_0.getFullKey(), item.getKey(0));
        assertEquals(0, item.getOffset(0));
        assertEquals(true, item.get(0).isTodo());

        assertEquals(LIST_1.getFullKey(), item.getKey(1));
        assertEquals(-1, item.getOffset(1));
        assertEquals(true, item.get(1).isTodo());

        assertEquals(LIST.getClearBlock(), item.getBlock());
    }

    @Test
    public void testSetList() throws Exception {
        RestoreItem item = load(LIST);

        item.set(0, load(LIST_0));
        assertEquals(false, item.get(0).isTodo());

        assertEquals(-1, item.getOffset(1));
        item.setOffset(1, 5);
        assertEquals(5, item.getOffset(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testSetListBad() throws Exception {
        RestoreItem item = load(LIST);

        item.setOffset(0, 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetListBad2() throws Exception {
        RestoreItem item = load(LIST);

        item.setOffset(1, 5);
        assertEquals(5, item.getOffset(1));

        item.setOffset(1, 5);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetListBad3() throws Exception {
        RestoreItem item = load(LIST);

        item.set(0, load(LIST_0));
        assertEquals(false, item.get(0).isTodo());

        item.set(0, new RestoreTodo());
    }

    private static final TestData SPLIT = TestData.KEY_NAME_SPLIT;
    private static final TestData SPLIT_0 = TestData.KEY_NAME_SPLIT_1;
    private static final TestData SPLIT_1 = TestData.KEY_NAME_SPLIT_2;

    @Test
    public void testLoadSplit() throws Exception {

        RestoreItem item = load(SPLIT);

        assertEquals(false, item.isData());
        assertEquals(false, item.isTodo());

        assertEquals(2, item.count());

        assertEquals(SPLIT_0.getFullKey(), item.getKey(0));
        assertEquals(0, item.getOffset(0));
        assertEquals(true, item.get(0).isTodo());

        assertEquals(SPLIT_1.getFullKey(), item.getKey(1));
        assertEquals(-1, item.getOffset(1));
        assertEquals(true, item.get(1).isTodo());

        assertEquals(SPLIT.getClearBlock(), item.getBlock());
    }

    @Test
    public void testSetSplit() throws Exception {
        RestoreItem item = load(SPLIT);

        item.set(0, load(SPLIT_0));
        assertEquals(false, item.get(0).isTodo());

        assertEquals(-1, item.getOffset(1));
        item.setOffset(1, 5);
        assertEquals(5, item.getOffset(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testSetSplitBad() throws Exception {
        RestoreItem item = load(SPLIT);

        item.setOffset(0, 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetSplitBad2() throws Exception {
        RestoreItem item = load(SPLIT);

        item.setOffset(1, 5);
        assertEquals(5, item.getOffset(1));
        item.setOffset(1, 5);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetSplitBad3() throws Exception {
        RestoreItem item = load(SPLIT);

        item.set(0, load(SPLIT_0));
        assertEquals(false, item.get(0).isTodo());

        item.set(0, new RestoreTodo());
    }

    private static final TestData INDIRECT = TestData.KEY_NAME_SPLIT_1;
    private static final TestData INDIRECT_0 = TestData.KEY_CONTENT_1;

    @Test
    public void testLoadIndirect() throws Exception {

        RestoreItem item = load(INDIRECT);

        assertEquals(false, item.isData());
        assertEquals(false, item.isTodo());

        assertEquals(1, item.count());

        assertEquals(INDIRECT_0.getFullKey(), item.getKey(0));
        assertEquals(0, item.getOffset(0));
        assertEquals(true, item.get(0).isTodo());

        assertEquals(INDIRECT.getClearBlock(), item.getBlock());
    }

    @Test
    public void testSetIndirect() throws Exception {
        RestoreItem item = load(INDIRECT);

        item.set(0, load(INDIRECT_0));
        assertEquals(false, item.get(0).isTodo());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetIndirectBad() throws Exception {
        RestoreItem item = load(INDIRECT);

        item.setOffset(0, 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetIndirectBad2() throws Exception {
        RestoreItem item = load(INDIRECT);

        item.set(0, load(INDIRECT_0));
        assertEquals(false, item.get(0).isTodo());

        item.set(0, new RestoreTodo());
    }

    private RestoreItem load(TestData data) throws Exception {
        Block block = data.getClearBlock();
        FullKey key = data.getFullKey();

        return RestoreItemFactory.create(key, block);
    }
}