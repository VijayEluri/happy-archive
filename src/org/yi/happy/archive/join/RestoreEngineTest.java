package org.yi.happy.archive.join;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;

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

    @Before
    public void before() {
        log = new LogFragmentHandler();
    }

    @After
    public void after() {
        log = null;
    }

    private static final TestData C = TestData.KEY_CONTENT;

    /**
     * The first basic operations, load a single data block.
     * 
     * @throws Exception
     */
    @Test
    public void testFirstBasic() throws Exception {
        RestoreEngine restore = new RestoreEngine(C.getFullKey(), log);

        assertEquals(Arrays.asList(C.getFullKey()), restore.getNeededNow());

        restore.addBlocks(new MapBuilder<FullKey, Block>().add(C.getFullKey(),
                C.getClearBlock()).create());

        assertEquals(Arrays.asList(new Fragment(0, new Bytes("hello\n"))),
                log.getFragments());
        assertTrue(log.isDone());
    }

    private static final TestData MAP = TestData.KEY_CONTENT_MAP;
    private static final TestData C1 = TestData.KEY_CONTENT_1;
    private static final TestData C2 = TestData.KEY_CONTENT_2;

    @Test
    public void testOutOfOrderMap() throws Exception {
        RestoreEngine restore = new RestoreEngine(MAP.getFullKey(), log);

        restore.addBlocks(new MapBuilder<FullKey, Block>().add(
                MAP.getFullKey(),
                MAP.getClearBlock()).create());

        assertEquals(Collections.emptyList(), log.getFragments());

        assertEquals(Arrays.asList(C1.getFullKey(), C2.getFullKey()),
                restore.getNeededNow());

        restore.addBlocks(new MapBuilder<FullKey, Block>().add(C2.getFullKey(),
                C2.getClearBlock()).create());

        assertEquals(Arrays.asList(new Fragment(5, new Bytes("56789"))),
                log.getFragments());
        assertFalse(log.isDone());

        assertEquals(Arrays.asList(C1.getFullKey()), restore.getNeededNow());

        restore.addBlocks(new MapBuilder<FullKey, Block>().add(C1.getFullKey(),
                C1.getClearBlock()).create());

        assertEquals(Arrays.asList(new Fragment(5, new Bytes("56789")),
                new Fragment(0, new Bytes("01234"))), log.getFragments());
        assertTrue(log.isDone());

        assertEquals(Collections.emptyList(), restore.getNeededNow());
    }
}
