package org.yi.happy.archive.join;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
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

        assertEquals(Arrays.asList(C.getFullKey()), restore.getNeededNow());

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

        assertEquals(Collections.emptyList(), log.getFragments());

        assertEquals(Arrays.asList(C1.getFullKey(), C2.getFullKey()),
                restore.getNeededNow());

        restore.addBlocks(blockMap(C2));

        assertEquals(Arrays.asList(new Fragment(5, D2)), log.getFragments());
        assertFalse(log.isDone());

        assertEquals(Arrays.asList(C1.getFullKey()), restore.getNeededNow());

        restore.addBlocks(blockMap(C1));

        assertEquals(list(frag(5, D2), frag(0, D1)), log.getFragments());
        assertTrue(log.isDone());

        assertEquals(Collections.emptyList(), restore.getNeededNow());
    }

    private static final TestData MAP_PAD = TestData.KEY_CONTENT_MAP_PAD;

    /**
     * Restore a map with padding in it.
     * 
     * @throws Exception
     */
    @Test
    public void testMapPad() throws Exception {
        RestoreEngine restore = new RestoreEngine(MAP_PAD.getFullKey(), log);

        restore.addBlocks(blockMap(MAP_PAD, C1, C2));

        assertEquals(list(frag(0, D1), frag(10, D2)), log.getFragments());
        assertTrue(log.isDone());
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
