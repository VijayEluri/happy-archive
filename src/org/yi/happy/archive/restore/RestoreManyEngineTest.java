package org.yi.happy.archive.restore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.yi.happy.archive.Bytes;
import org.yi.happy.archive.Fragment;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link RestoreManyEngine}.
 */
public class RestoreManyEngineTest {
    private static final TestData C = TestData.KEY_CONTENT;
    private static final Bytes D = new Bytes("hello\n");

    /**
     * A blank restore.
     */
    @Test
    public void testBlank() {
        RestoreManyEngine restore = new RestoreManyEngine();

        assertEquals(keyList(), restore.getNeeded());

        assertTrue(restore.isDone());

        // loop
        restore.start();

        assertEquals(false, restore.findReady());
        // loop done

        assertTrue(restore.isDone());
    }

    /**
     * The first very basic operations, load a single data block.
     * 
     * @throws Exception
     */
    @Test
    public void testFirstVeryBasic() throws Exception {
        RestoreManyEngine restore = new RestoreManyEngine(C.getFullKey());

        assertEquals(keyList(C), restore.getNeeded());

        // loop
        restore.start();

        assertEquals(true, restore.findReady());
        assertEquals(key(C), restore.getKey());
        assertEquals(frag(0, D), restore.step(C.getClearBlock()));

        assertEquals(false, restore.findReady());
        // loop done

        assertTrue(restore.isDone());
    }

    /**
     * The first basic operations, load a single data block, with name.
     * 
     * @throws Exception
     */
    @Test
    public void testFirstBasic() throws Exception {
        RestoreManyEngine restore = new RestoreManyEngine("", C.getFullKey());

        assertEquals(keyList(C), restore.getNeeded());

        // loop
        restore.start();

        assertEquals(true, restore.findReady());
        assertEquals(key(C), restore.getKey());
        assertEquals(frag(0, D), restore.step(C.getClearBlock()));
        assertEquals("", restore.getJobName());
        assertEquals(false, restore.findReady());
        // loop done

        assertTrue(restore.isDone());
    }

    private static final TestData MAP = TestData.KEY_CONTENT_MAP;
    private static final TestData C1 = TestData.KEY_CONTENT_1;
    private static final TestData C2 = TestData.KEY_CONTENT_2;
    private static final Bytes D1 = new Bytes('0', '1', '2', '3', '4');
    private static final Bytes D2 = new Bytes('5', '6', '7', '8', '9');

    /**
     * process a map, with the later block being available before the first.
     * 
     * @throws Exception
     */
    @Test
    public void testOutOfOrderMap() throws Exception {
        RestoreManyEngine restore = new RestoreManyEngine(key(MAP));

        // loop
        restore.start();

        assertEquals(true, restore.findReady());
        assertEquals(MAP.getFullKey(), restore.getKey());
        assertEquals(null, restore.step(block(MAP)));

        assertEquals(true, restore.findReady());
        assertEquals(key(C1), restore.getKey());
        restore.skip();

        assertEquals(true, restore.findReady());
        assertEquals(key(C2), restore.getKey());
        restore.skip();

        assertEquals(false, restore.findReady());
        // loop done

        assertEquals(keyList(C1, C2), restore.getNeeded());

        // loop
        restore.start();

        assertEquals(true, restore.findReady());
        assertEquals(key(C1), restore.getKey());
        restore.skip();

        assertEquals(true, restore.findReady());
        assertEquals(key(C2), restore.getKey());
        assertEquals(frag(5, D2), restore.step(block(C2)));

        assertEquals(false, restore.findReady());
        // loop done

        assertEquals(keyList(C1), restore.getNeeded());

        // loop
        restore.start();

        assertEquals(true, restore.findReady());
        assertEquals(key(C1), restore.getKey());
        assertEquals(frag(0, D1), restore.step(block(C1)));

        assertEquals(false, restore.findReady());
        // loop done

        assertTrue(restore.isDone());
        assertEquals(keyList(), restore.getNeeded());
    }

    /**
     * Restore a map with padding in it.
     * 
     * @throws Exception
     */
    @Test
    public void testMapPad() throws Exception {
        TestData B = TestData.KEY_CONTENT_MAP_PAD;
        RestoreManyEngine restore = new RestoreManyEngine(key(B));

        // loop
        restore.start();

        assertEquals(true, restore.findReady());
        assertEquals(key(B), restore.getKey());
        assertEquals(null, restore.step(block(B)));

        assertEquals(true, restore.findReady());
        assertEquals(key(C1), restore.getKey());
        assertEquals(frag(0, D1), restore.step(block(C1)));

        assertEquals(true, restore.findReady());
        assertEquals(key(C2), restore.getKey());
        assertEquals(frag(10, D2), restore.step(block(C2)));

        assertEquals(false, restore.findReady());
        // loop done

        assertTrue(restore.isDone());
    }

    /**
     * Restore a map with an overlap in it.
     * 
     * @throws Exception
     */
    @Test
    public void testMapOverlap() throws Exception {
        TestData B = TestData.KEY_CONTENT_MAP_OVERLAP;
        RestoreManyEngine restore = new RestoreManyEngine(key(B));

        // loop
        restore.start();

        assertEquals(true, restore.findReady());
        assertEquals(key(B), restore.getKey());
        assertEquals(null, restore.step(block(B)));

        assertEquals(true, restore.findReady());
        assertEquals(key(C1), restore.getKey());
        assertEquals(frag(0, D1), restore.step(block(C1)));

        assertEquals(true, restore.findReady());
        assertEquals(key(C2), restore.getKey());
        assertEquals(frag(3, D2), restore.step(block(C2)));

        assertEquals(false, restore.findReady());
        // loop done

        assertTrue(restore.isDone());
    }

    /**
     * Restore a list.
     * 
     * @throws Exception
     */
    @Test
    public void testList() throws Exception {
        TestData B = TestData.KEY_CONTENT_LIST;
        RestoreManyEngine restore = new RestoreManyEngine(key(B));

        // loop
        restore.start();

        assertTrue(restore.findReady());
        assertEquals(key(B), restore.getKey());
        assertEquals(null, restore.step(block(B)));

        assertTrue(restore.findReady());
        assertEquals(key(C1), restore.getKey());
        assertEquals(frag(0, D1), restore.step(block(C1)));

        assertTrue(restore.findReady());
        assertEquals(key(C2), restore.getKey());
        assertEquals(frag(5, D2), restore.step(block(C2)));

        assertFalse(restore.findReady());
        // loop done

        assertTrue(restore.isDone());
    }

    /**
     * Restore a list, checking that it will not process blocks out of order.
     * 
     * @throws Exception
     */
    @Test
    public void testListOrder() throws Exception {
        TestData B = TestData.KEY_CONTENT_LIST;
        RestoreManyEngine restore = new RestoreManyEngine(key(B));

        /*
         * B is available
         */

        // loop
        restore.start();

        assertEquals(true, restore.findReady());
        assertEquals(key(B), restore.getKey());
        assertEquals(null, restore.step(block(B)));

        assertEquals(true, restore.findReady());
        assertEquals(key(C1), restore.getKey());
        restore.skip();

        assertEquals(false, restore.findReady());
        // loop done

        assertEquals(keyList(C1, C2), restore.getNeeded());

        /*
         * C2 is available, but not C1.
         */

        // loop
        restore.start();

        assertEquals(true, restore.findReady());
        assertEquals(key(C1), restore.getKey());
        restore.skip();

        assertEquals(false, restore.findReady());
        // loop done

        assertEquals(keyList(C1, C2), restore.getNeeded());

        /*
         * this time C1 is available and C2 is not
         */

        // loop
        restore.start();

        assertEquals(true, restore.findReady());
        assertEquals(key(C1), restore.getKey());
        assertEquals(frag(0, D1), restore.step(block(C1)));

        assertEquals(true, restore.findReady());
        assertEquals(key(C2), restore.getKey());
        restore.skip();

        assertEquals(false, restore.findReady());
        // loop done

        assertEquals(keyList(C2), restore.getNeeded());

        // loop
        restore.start();

        assertEquals(true, restore.findReady());
        assertEquals(key(C2), restore.getKey());
        assertEquals(frag(5, D2), restore.step(block(C2)));

        assertEquals(false, restore.findReady());
        // loop done

        assertTrue(restore.isDone());
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

        RestoreManyEngine restore = new RestoreManyEngine(key(B));

        /*
         * the top block is available.
         */

        // loop
        restore.start();

        assertEquals(true, restore.findReady());
        assertEquals(key(B), restore.getKey());
        assertEquals(null, restore.step(block(B)));

        assertEquals(true, restore.findReady());
        assertEquals(key(B1), restore.getKey());
        restore.skip();

        assertEquals(false, restore.findReady());
        // loop done

        assertEquals(keyList(B1, B2), restore.getNeeded());

        /*
         * all the requested blocks are available
         */

        // loop
        restore.start();

        assertEquals(true, restore.findReady());
        assertEquals(key(B1), restore.getKey());
        assertEquals(null, restore.step(block(B1)));

        assertEquals(true, restore.findReady());
        assertEquals(key(C1), restore.getKey());
        restore.skip();

        assertEquals(false, restore.findReady());
        // loop done

        assertEquals(keyList(C1, B2), restore.getNeeded());

        /*
         * all the requested blocks are available
         */

        // loop
        restore.start();

        assertEquals(true, restore.findReady());
        assertEquals(key(C1), restore.getKey());
        assertEquals(frag(0, D1), restore.step(block(C1)));

        assertEquals(true, restore.findReady());
        assertEquals(key(B2), restore.getKey());
        assertEquals(null, restore.step(block(B2)));

        assertEquals(true, restore.findReady());
        assertEquals(key(C2), restore.getKey());
        restore.skip();

        assertEquals(false, restore.findReady());
        // loop done

        assertEquals(keyList(C2), restore.getNeeded());

        /*
         * all the requested blocks are available
         */

        // loop
        restore.start();

        assertEquals(true, restore.findReady());
        assertEquals(key(C2), restore.getKey());
        assertEquals(frag(5, D2), restore.step(block(C2)));

        assertEquals(false, restore.findReady());
        // loop done

        assertEquals(keyList(), restore.getNeeded());

        assertTrue(restore.isDone());
    }

    /**
     * load two keys.
     * 
     * @throws Exception
     */
    public void testListAndSplit() throws Exception {
        TestData L = TestData.KEY_CONTENT_LIST;
        TestData S = TestData.KEY_NAME_SPLIT;
        TestData S1 = TestData.KEY_NAME_SPLIT_1;
        TestData S2 = TestData.KEY_NAME_SPLIT_2;

        RestoreManyEngine restore = new RestoreManyEngine();
        restore.add(name(L), key(L));
        restore.add(name(S), key(S));

        assertEquals(keyList(L, S), restore.getNeeded());

        /*
         * L and S available
         */
        
        // loop
        restore.start();
        
        assertTrue(restore.findReady());
        assertEquals(key(L), restore.getKey());
        assertEquals(null, restore.step(block(L)));
        
        assertTrue(restore.findReady());
        assertEquals(key(C1), restore.getKey());
        restore.skip();
        
        assertTrue(restore.findReady());
        assertEquals(key(S), restore.getKey());
        assertEquals(null, restore.step(block(S)));
        
        assertTrue(restore.findReady());
        assertEquals(key(S1), restore.getKey());
        restore.skip();
        
        assertFalse(restore.findReady());
        // loop done
        
        assertEquals(keyList(C1, C2, S1, S2), restore.getNeeded());
        
        /*
         * C1, C2, S1, S2 available.
         */
        
        // loop
        restore.start();
        
        assertTrue(restore.findReady());
        assertEquals(key(C1), restore.getKey());
        assertEquals(frag(0, D1), restore.step(block(C1)));
        assertEquals(name(L), restore.getJobName());

        assertTrue(restore.findReady());
        assertEquals(key(C2), restore.getKey());
        assertEquals(frag(5, D2), restore.step(block(C2)));
        assertEquals(name(L), restore.getJobName());

        assertTrue(restore.findReady());
        assertEquals(key(S1), restore.getKey());
        assertEquals(null, restore.step(block(S1)));

        assertTrue(restore.findReady());
        assertEquals(key(C1), restore.getKey());
        assertEquals(frag(0, D1), restore.step(block(C1)));
        assertEquals(name(S), restore.getJobName());

        assertTrue(restore.findReady());
        assertEquals(key(S2), restore.getKey());
        assertEquals(null, restore.step(block(S2)));

        assertTrue(restore.findReady());
        assertEquals(key(C2), restore.getKey());
        assertEquals(frag(0, D2), restore.step(block(C2)));
        assertEquals(name(S), restore.getJobName());

        assertFalse(restore.findReady());
        // loop done

        assertTrue(restore.isDone());
    }

    /**
     * get the blocks of multiple keys in order.
     * 
     * @throws Exception
     */
    public void testInOrder() throws Exception {
        String J1 = "1";
        String J2 = "2";
        TestData M = TestData.KEY_CONTENT_MAP;

        RestoreManyEngine restore = new RestoreManyEngine();
        restore.add(J1, key(M));
        restore.add(J2, key(M));

        /*
         * M, C2 available
         */

        // loop
        restore.start();

        assertTrue(restore.findReady());
        assertEquals(key(M), restore.getKey());
        assertEquals(null, restore.step(block(M)));

        assertTrue(restore.findReady());
        assertEquals(key(C1), restore.getKey());
        restore.skipJob();

        assertTrue(restore.findReady());
        assertEquals(key(M), restore.getKey());
        assertEquals(null, restore.step(block(M)));

        assertTrue(restore.findReady());
        assertEquals(key(C1), restore.getKey());
        restore.skipJob();

        assertFalse(restore.findReady());
        // loop done
    }

    private static String name(TestData item) {
        return item.name();
    }

    private static FullKey key(TestData item) {
        return item.getFullKey();
    }

    private static List<FullKey> keyList(TestData... items) {
        List<FullKey> list = new ArrayList<FullKey>();
        for (TestData item : items) {
            list.add(key(item));
        }
        return list;
    }

    private static Fragment frag(int offset, Bytes data) {
        return new Fragment(offset, data);
    }

    private static Block block(TestData item) throws IOException {
        return item.getClearBlock();
    }
}
