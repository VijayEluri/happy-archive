package org.yi.happy.archive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.yi.happy.archive.key.ContentFullKey;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.test_data.TestData;

/**
 * read blocks from the store, as available or linearly. This is to support
 * reading from a stream, loading a file in random order, or loading a whole set
 * of files.
 */
public class SplitReaderTest {
    /**
     * the storage engine that SplitReader wants
     */
    private SimpleRetrieveBlock store;

    /**
     * setup the parts
     */
    @Before
    public void before() {
        store = new SimpleRetrieveBlock();
    }

    /**
     * tear down the parts
     */
    @After
    public void after() {
        store = null;
    }

    private static final TestData MAP = TestData.KEY_CONTENT_MAP;

    private static final TestData C1 = TestData.KEY_CONTENT_1;

    private static final Bytes D1 = new Bytes('0', '1', '2', '3', '4');

    private static final TestData C2 = TestData.KEY_CONTENT_2;

    private static final Bytes D2 = new Bytes('5', '6', '7', '8', '9');

    /**
     * check that no blocks get returned if only the base of a map is ready.
     * 
     * @throws IOException
     */
    @Test
    public void testJustBaseOfMap() throws IOException {
        store.put(MAP);
        SplitReader r = new SplitReader(MAP.getFullKey(), store);

        Fragment got = r.fetchAny();

        assertEquals(null, got);
    }

    /**
     * load the base and the second part and check that we get the second part
     * first, and then load the second and we should get that too.
     * 
     * @throws IOException
     */
    @Test
    public void testMapSecondThenFirst() throws IOException {
        store.put(MAP);
        store.put(C2);
        SplitReader r = new SplitReader(MAP.getFullKey(), store);

        Fragment got = r.fetchAny();

        assertEquals(new Fragment(5, D2), got);

        got = r.fetchAny();

        assertEquals(null, got);

        store.put(C1);
        got = r.fetchAny();

        assertEquals(new Fragment(0, D1), got);
    }

    /**
     * get first with just the second part and the base loaded should get
     * nothing.
     * 
     * @throws IOException
     */
    @Test
    public void testGetFirst1() throws IOException {
        store.put(MAP);
        store.put(C2);
        SplitReader r = new SplitReader(MAP.getFullKey(), store);

        Fragment got = r.fetchFirst();

        assertEquals(null, got);
    }

    /**
     * get first with the base and first loaded should get something.
     * 
     * @throws IOException
     */
    @Test
    public void testGetFirst2() throws IOException {
        store.put(MAP);
        store.put(C1);
        SplitReader r = new SplitReader(MAP.getFullKey(), store);

        Fragment got = r.fetchFirst();

        assertEquals(new Fragment(0, D1), got);
    }

    /**
     * check that the pending list works, just the base nothing ready
     */
    @Test
    public void testPending1() {
        SplitReader r = new SplitReader(MAP.getFullKey(), store);

        List<FullKey> got = r.getPending();

        Assert.assertEquals(Arrays.asList(MAP.getFullKey()), got);
    }

    /**
     * check the pending list is right before reading is attempted, and after as
     * well.
     * 
     * @throws IOException
     */
    @Test
    public void testPending2() throws IOException {
        store.put(MAP);
        SplitReader r = new SplitReader(MAP.getFullKey(), store);

        assertEquals(Arrays.asList(MAP.getFullKey()), r.getPending());

        assertNull(r.fetchAny());

        assertEquals(Arrays.asList(C1.getFullKey(), C2.getFullKey()), r
                .getPending());
    }

    /**
     * check that the completed state is right.
     * 
     * @throws IOException
     */
    @Test
    public void testPendingAfterDone() throws IOException {
        store.put(MAP);
        store.put(C1);
        store.put(C2);

        SplitReader r = new SplitReader(MAP.getFullKey(), store);
        Fragment got = r.fetchAny();

        assertNotNull(got);

        got = r.fetchAny();

        assertNotNull(got);

        got = r.fetchAny();

        assertNull(got);

        assertEquals(true, r.isDone());

        assertEquals(Collections.emptyList(), r.getPending());
    }

    /**
     * check that gaps in the map work
     * 
     * @throws IOException
     */
    @Test
    public void testReadMapPad() throws IOException {
        TestData d = TestData.KEY_CONTENT_MAP_PAD;
        store.put(d);
        store.put(C1);
        store.put(C2);

        SplitReader r = new SplitReader(d.getFullKey(), store);

        Fragment got = r.fetchFirst();
        assertEquals(new Fragment(0, D1), got);

        assertEquals((Long) 10L, r.getOffset());

        got = r.fetchFirst();
        assertEquals(new Fragment(10, D2), got);

        assertEquals(null, r.getOffset());
    }

    /**
     * check that overlaps in the map work
     * 
     * @throws IOException
     */
    @Test
    public void testReadMapOverlap() throws IOException {
        TestData d = TestData.KEY_CONTENT_MAP_OVERLAP;
        store.put(d);
        store.put(C1);
        store.put(C2);

        SplitReader r = new SplitReader(d.getFullKey(), store);

        Fragment got = r.fetchFirst();
        assertEquals(new Fragment(0, D1), got);

        got = r.fetchFirst();
        assertEquals(new Fragment(3, D2), got);
    }

    /**
     * check that reading a list block works
     * 
     * @throws IOException
     */
    @Test
    public void testReadList() throws IOException {
        TestData d = TestData.KEY_CONTENT_LIST;
        store.put(d);
        store.put(C1);
        store.put(C2);

        SplitReader r = new SplitReader(d.getFullKey(), store);
        Fragment got;

        got = r.fetchFirst();

        assertEquals(new Fragment(0, D1), got);

        got = r.fetchFirst();

        assertEquals(new Fragment(5, D2), got);
    }

    /**
     * check that reading a very old split key works
     * 
     * @throws IOException
     */
    @Test
    public void testReadSplit() throws IOException {
        TestData d = TestData.KEY_NAME_SPLIT;
        store.put(d);
        store.put(TestData.KEY_NAME_SPLIT_1);
        store.put(C1);
        store.put(TestData.KEY_NAME_SPLIT_2);
        store.put(C2);
        SplitReader r = new SplitReader(d.getFullKey(), store);
        Fragment got;

        got = r.fetchFirst();

        assertEquals(new Fragment(0, D1), got);

        got = r.fetchFirst();

        assertEquals(new Fragment(5, D2), got);
    }

    /**
     * check that reading with the wrong key will throw an {@link IOException}.
     * 
     * @throws IOException
     */
    @Test(expected = DecodeException.class)
    public void testBad() throws IOException {
        store.put(C1);

        ContentFullKey k = (ContentFullKey) C1.getFullKey();
        k = new ContentFullKey(k.getHash(), new Bytes());

        SplitReader r = new SplitReader(k, store);

        r.fetchAny();
    }

    /**
     * check that reading with the wrong key will throw an {@link IOException}.
     * 
     * @throws IOException
     */
    @Test(expected = DecodeException.class)
    public void testBad2() throws IOException {
        store.put(C1);

        ContentFullKey k = (ContentFullKey) C1.getFullKey();
        {
            byte[] b = k.getPass().toByteArray();
            b[0] ^= 0x01;
            k = new ContentFullKey(k.getHash(), new Bytes(b));
        }

        SplitReader r = new SplitReader(k, store);

        r.fetchAny();
    }
}
