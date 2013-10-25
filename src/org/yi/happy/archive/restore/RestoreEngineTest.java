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
 * Tests for {@link RestoreEngine}.
 */
public class RestoreEngineTest {
    private static final TestData C = TestData.KEY_CONTENT;
    private static final Bytes D = new Bytes("hello\n");

    /**
     * The first basic operations, load a single data block.
     * 
     * @throws Exception
     */
    @Test
    public void testFirstBasic() throws Exception {
        RestoreEngine restore = new RestoreEngine(C.getFullKey());

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
        RestoreEngine restore = new RestoreEngine(key(MAP));

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
        RestoreEngine restore = new RestoreEngine(key(B));

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
        RestoreEngine restore = new RestoreEngine(key(B));

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
        RestoreEngine restore = new RestoreEngine(key(B));

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
        RestoreEngine restore = new RestoreEngine(key(B));

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

        RestoreEngine restore = new RestoreEngine(key(B));

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
