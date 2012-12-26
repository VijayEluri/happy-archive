package org.yi.happy.archive.restore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.yi.happy.archive.Bytes;
import org.yi.happy.archive.Fragment;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link RestoreEngine}.
 */
public class RestoreEngineTest {
    private LogFragmentHandler log;

    /**
     * set up
     */
    @Before
    public void before() {
        log = new LogFragmentHandler();
    }

    /**
     * tear down
     */
    @After
    public void after() {
        log = null;
    }

    private static final TestData C = TestData.KEY_CONTENT;
    private static final Bytes D = new Bytes("hello\n");

    /**
     * The first basic operations, load a single data block.
     * 
     * @throws Exception
     */
    @Test
    public void testFirstBasic() throws Exception {
        RestoreEngine restore = new RestoreEngine(C.getFullKey(), log);

        assertEquals(keyList(C), restore.getNeededNow());

        restore.addBlocks(blockMap(C));

        assertEquals(list(frag(0, D)), log.getFragments());
        assertTrue(log.isDone());
    }

    private static final TestData MAP = TestData.KEY_CONTENT_MAP;
    private static final TestData C1 = TestData.KEY_CONTENT_1;
    private static final TestData C2 = TestData.KEY_CONTENT_2;
    private static final Bytes D1 = new Bytes('0', '1', '2', '3', '4');
    private static final Bytes D2 = new Bytes('5', '6', '7', '8', '9');

    /**
     * process a map, with the later blocks being available before the first.
     * 
     * @throws Exception
     */
    @Test
    public void testOutOfOrderMap() throws Exception {
        RestoreEngine restore = new RestoreEngine(MAP.getFullKey(), log);

        restore.addBlocks(blockMap(MAP));

        assertEquals(list(), log.getFragments());

        assertEquals(keyList(C1, C2), restore.getNeededNow());

        restore.addBlocks(blockMap(C2));

        assertEquals(list(frag(5, D2)), log.getFragments());
        assertFalse(log.isDone());

        assertEquals(keyList(C1), restore.getNeededNow());

        restore.addBlocks(blockMap(C1));

        assertEquals(list(frag(5, D2), frag(0, D1)), log.getFragments());
        assertTrue(log.isDone());

        assertEquals(keyList(), restore.getNeededNow());
    }

    /**
     * Restore a map with padding in it.
     * 
     * @throws Exception
     */
    @Test
    public void testMapPad() throws Exception {
        TestData B = TestData.KEY_CONTENT_MAP_PAD;
        RestoreEngine restore = new RestoreEngine(key(B), log);

        restore.addBlocks(blockMap(B, C1, C2));

        assertEquals(list(frag(0, D1), frag(10, D2)), log.getFragments());
        assertTrue(log.isDone());
    }

    /**
     * Restore a map with an overlap in it.
     * 
     * @throws Exception
     */
    @Test
    public void testMapOverlap() throws Exception {
        TestData B = TestData.KEY_CONTENT_MAP_OVERLAP;
        RestoreEngine restore = new RestoreEngine(key(B), log);

        restore.addBlocks(blockMap(B, C1, C2));
        assertEquals(list(frag(0, D1), frag(3, D2)), log.getFragments());
        assertTrue(log.isDone());
    }

    /**
     * Restore a list.
     * 
     * @throws Exception
     */
    @Test
    public void testList() throws Exception {
        TestData B = TestData.KEY_CONTENT_LIST;
        RestoreEngine restore = new RestoreEngine(key(B), log);

        restore.addBlocks(blockMap(B, C1, C2));
        assertEquals(list(frag(0, D1), frag(5, D2)), log.getFragments());
        assertTrue(log.isDone());
    }

    /**
     * Restore a list, checking that it will not process blocks out of order.
     * 
     * @throws Exception
     */
    @Test
    public void testListOrder() throws Exception {
        TestData B = TestData.KEY_CONTENT_LIST;
        RestoreEngine restore = new RestoreEngine(key(B), log);

        restore.addBlocks(blockMap(B));

        assertEquals(list(), log.getFragments());
        assertEquals(keyList(C1), restore.getNeededNow());
        assertEquals(keyList(C2), restore.getNeededLater());

        restore.addBlocks(blockMap(C2));

        assertEquals(list(), log.getFragments());
        assertEquals(keyList(C1), restore.getNeededNow());
        assertEquals(keyList(C2), restore.getNeededLater());

        restore.addBlocks(blockMap(C1));

        assertEquals(list(frag(0, D1)), log.getFragments());
        assertEquals(keyList(C2), restore.getNeededNow());
        assertEquals(keyList(), restore.getNeededLater());

        restore.addBlocks(blockMap(C2));

        assertEquals(list(frag(0, D1), frag(5, D2)), log.getFragments());
        assertTrue(log.isDone());
    }

    /**
     * Restore a split block.
     * 
     * @throws Exception
     */
    @Test
    public void testSplit() throws Exception {
        TestData B = TestData.KEY_NAME_SPLIT;
        TestData B1 = TestData.KEY_NAME_SPLIT_1;
        TestData B2 = TestData.KEY_NAME_SPLIT_2;

        RestoreEngine restore = new RestoreEngine(key(B), log);

        restore.addBlocks(blockMap(B));

        assertEquals(keyList(B1), restore.getNeededNow());
        assertEquals(keyList(B2), restore.getNeededLater());

        restore.addBlocks(blockMap(B1, B2));

        assertEquals(keyList(C1), restore.getNeededNow());
        assertEquals(keyList(B2), restore.getNeededLater());

        restore.addBlocks(blockMap(C1, B2));

        assertEquals(keyList(C2), restore.getNeededNow());
        assertEquals(keyList(), restore.getNeededLater());

        restore.addBlocks(blockMap(C2));

        assertEquals(keyList(), restore.getNeededNow());
        assertEquals(keyList(), restore.getNeededLater());
    }

    private FullKey key(TestData item) {
        return item.getFullKey();
    }

    private List<FullKey> keyList(TestData... items) {
        List<FullKey> list = new ArrayList<FullKey>();
        for (TestData item : items) {
            list.add(key(item));
        }
        return list;
    }

    private <T> List<T> list(T... items) {
        return Arrays.asList(items);
    }

    private Fragment frag(int offset, Bytes data) {
        return new Fragment(offset, data);
    }

    private Map<FullKey, Block> blockMap(TestData... blocks) throws IOException {
        MapBuilder<FullKey, Block> b = new MapBuilder<FullKey, Block>();
        for (TestData block : blocks) {
            b.add(block.getFullKey(), block.getClearBlock());
        }
        return b.create();
    }
}
