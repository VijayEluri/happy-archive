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

        assertEquals(2, item.count());

        assertEquals(MAP_0.getFullKey(), item.getKey(0));
        assertEquals(0, item.getOffset(0));

        assertEquals(MAP_1.getFullKey(), item.getKey(1));
        assertEquals(5, item.getOffset(1));

        assertEquals(MAP.getClearBlock(), item.getBlock());
    }

    private static final TestData LIST = TestData.KEY_CONTENT_LIST;
    private static final TestData LIST_0 = TestData.KEY_CONTENT_1;
    private static final TestData LIST_1 = TestData.KEY_CONTENT_2;

    /**
     * load a list block and check all the values.
     * 
     * @throws Exception
     */
    @Test
    public void testLoadList() throws Exception {

        RestoreItem item = load(LIST);

        assertEquals(false, item.isData());

        assertEquals(2, item.count());

        assertEquals(LIST_0.getFullKey(), item.getKey(0));
        assertEquals(0, item.getOffset(0));

        assertEquals(LIST_1.getFullKey(), item.getKey(1));
        assertEquals(-1, item.getOffset(1));

        assertEquals(LIST.getClearBlock(), item.getBlock());
    }

    private static final TestData SPLIT = TestData.KEY_NAME_SPLIT;
    private static final TestData SPLIT_0 = TestData.KEY_NAME_SPLIT_1;
    private static final TestData SPLIT_1 = TestData.KEY_NAME_SPLIT_2;

    /**
     * check all the fields of a split block.
     * 
     * @throws Exception
     */
    @Test
    public void testLoadSplit() throws Exception {

        RestoreItem item = load(SPLIT);

        assertEquals(false, item.isData());

        assertEquals(2, item.count());

        assertEquals(SPLIT_0.getFullKey(), item.getKey(0));
        assertEquals(0, item.getOffset(0));

        assertEquals(SPLIT_1.getFullKey(), item.getKey(1));
        assertEquals(-1, item.getOffset(1));

        assertEquals(SPLIT.getClearBlock(), item.getBlock());
    }

    private static final TestData INDIRECT = TestData.KEY_NAME_SPLIT_1;
    private static final TestData INDIRECT_0 = TestData.KEY_CONTENT_1;

    /**
     * check all the fields of an indirect block.
     * 
     * @throws Exception
     */
    @Test
    public void testLoadIndirect() throws Exception {

        RestoreItem item = load(INDIRECT);

        assertEquals(false, item.isData());

        assertEquals(1, item.count());

        assertEquals(INDIRECT_0.getFullKey(), item.getKey(0));
        assertEquals(0, item.getOffset(0));

        assertEquals(INDIRECT.getClearBlock(), item.getBlock());
    }

    private RestoreItem load(TestData data) throws Exception {
        Block block = data.getClearBlock();
        FullKey key = data.getFullKey();

        return RestoreItemFactory.create(key, block);
    }

    /**
     * Work through a data block.
     * 
     * @throws Exception
     */
    @Test
    public void testWorkData() throws Exception {
        RestoreWork work = new RestoreWork(DATA.getFullKey());

        assertEquals(1, work.count());

        assertEquals(0, work.getOffset(0));
        assertEquals(DATA.getFullKey(), work.getKey(0));

        work.replace(0, load(DATA));

        assertEquals(0, work.count());
    }

    /**
     * Work through a list block.
     * 
     * @throws Exception
     */
    @Test
    public void testWorkList() throws Exception {
        RestoreWork work = new RestoreWork(LIST.getFullKey());

        assertEquals(1, work.count());

        assertEquals(0, work.getOffset(0));
        assertEquals(LIST.getFullKey(), work.getKey(0));

        work.replace(0, load(LIST));

        assertEquals(2, work.count());

        assertEquals(0, work.getOffset(0));
        assertEquals(LIST_0.getFullKey(), work.getKey(0));

        assertEquals(-1, work.getOffset(1));
        assertEquals(LIST_1.getFullKey(), work.getKey(1));

        work.replace(0, load(LIST_0));

        assertEquals(1, work.count());

        assertEquals(5, work.getOffset(0));
        assertEquals(LIST_1.getFullKey(), work.getKey(0));

        work.replace(0, load(LIST_1));

        assertEquals(0, work.count());
    }

    /**
     * Attempt to replace an entry with no offset.
     * 
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void testWorkBadReplace() throws Exception {
        RestoreWork work = new RestoreWork(LIST_0.getFullKey());
        work.add(LIST_1.getFullKey(), -1);

        assertEquals(2, work.count());
        assertEquals(-1, work.getOffset(1));

        work.replace(1, load(LIST_1));
    }

    /**
     * Work through a map block.
     * 
     * @throws Exception
     */
    @Test
    public void testWorkMap() throws Exception {
        RestoreWork work = new RestoreWork(MAP.getFullKey());

        assertEquals(1, work.count());

        assertEquals(0, work.getOffset(0));
        assertEquals(MAP.getFullKey(), work.getKey(0));

        work.replace(0, load(MAP));

        assertEquals(2, work.count());

        assertEquals(0, work.getOffset(0));
        assertEquals(MAP_0.getFullKey(), work.getKey(0));

        assertEquals(5, work.getOffset(1));
        assertEquals(MAP_1.getFullKey(), work.getKey(1));

        work.replace(0, load(MAP_0));

        assertEquals(1, work.count());

        assertEquals(5, work.getOffset(0));
        assertEquals(MAP_1.getFullKey(), work.getKey(0));

        work.replace(0, load(MAP_1));

        assertEquals(0, work.count());
    }

    /**
     * Work through a map block, out of order.
     * 
     * @throws Exception
     */
    @Test
    public void testWorkMap2() throws Exception {
        RestoreWork work = new RestoreWork(MAP.getFullKey());

        assertEquals(1, work.count());

        assertEquals(0, work.getOffset(0));
        assertEquals(MAP.getFullKey(), work.getKey(0));

        work.replace(0, load(MAP));

        assertEquals(2, work.count());

        assertEquals(0, work.getOffset(0));
        assertEquals(MAP_0.getFullKey(), work.getKey(0));

        assertEquals(5, work.getOffset(1));
        assertEquals(MAP_1.getFullKey(), work.getKey(1));

        work.replace(1, load(MAP_1));

        assertEquals(1, work.count());

        assertEquals(0, work.getOffset(0));
        assertEquals(MAP_0.getFullKey(), work.getKey(0));

        work.replace(0, load(MAP_0));

        assertEquals(0, work.count());
    }
}
