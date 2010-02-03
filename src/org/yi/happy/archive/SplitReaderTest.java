package org.yi.happy.archive;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.test_data.TestData;

/**
 * read blocks from the store, as available or linearly. This is to support
 * reading from a stream, loading a file in random order, or loading a whole set
 * of files.
 * 
 * @author sarah dot a dot happy at gmail dot com
 */
public class SplitReaderTest {
    /**
     * the storage engine that SplitReader wants
     */
    private RetrieveBlockStorage storage;

    /**
     * the real storage engine
     */
    private BlockStore rawStorage;

    /**
     * setup the parts
     */
    @Before
    public void before() {
        rawStorage = new StorageMemory();
        storage = new RetrieveBlockStorage(rawStorage);
    }

    /**
     * tear down the parts
     */
    @After
    public void after() {
        storage = null;
        rawStorage = null;
    }

    /**
     * load a block into the raw storage
     * 
     * @param data
     *            which block to load
     * @throws IOException
     */
    private void loadBlock(TestData data) throws IOException {
	rawStorage.put(data.getEncodedBlock());
    }

    /**
     * check that no blocks get returned if only the base of a map is ready.
     * 
     * @throws IOException
     */
    @Test
    public void testJustBaseOfMap() throws IOException {
        loadBlock(MAP);
        SplitReader r = createMapReader();

        assertNull(r.getAny());
    }

    /**
     * get the reader for reading a map
     * 
     * @return the reader
     */
    private SplitReader createMapReader() {
        return new SplitReader(MAP.getFullKey(), storage);
    }

    /**
     * load the base and the second part and check that we get the second part
     * first, and then load the second and we should get that too.
     * 
     * @throws IOException
     */
    @Test
    public void testMapSecondThenFirst() throws IOException {
        loadBlock(MAP);
        loadBlock(C2);
        SplitReader r = createMapReader();

	SplitReader.GetResult got = r.getAny();
	assertEquals(5, (long) got.getOffset());
	assertArrayEquals("56789".getBytes(), got.getData());

        assertNull(r.getAny());

        loadBlock(C1);
        got = r.getAny();
	assertEquals(0, (long) got.getOffset());
	assertArrayEquals("01234".getBytes(), got.getData());
    }

    /**
     * get first with just the second part and the base loaded should get
     * nothing.
     * 
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testGetFirst1() throws IOException {
        loadBlock(MAP);
        loadBlock(C2);
        SplitReader r = createMapReader();

	r.getFirst();
    }

    /**
     * get first with the base and first loaded should get something.
     * 
     * @throws IOException
     */
    @Test
    public void testGetFirst2() throws IOException {
        loadBlock(MAP);
        loadBlock(C1);
        SplitReader r = createMapReader();

	SplitReader.GetResult got = r.getFirst();
	assertEquals(0, (long) got.getOffset());
	assertArrayEquals("01234".getBytes(), got.getData());
    }

    /**
     * check that the pending list works, just the base nothing ready
     */
    @Test
    public void testPending1() {
        SplitReader r = createMapReader();
        List<FullKey> got = r.getPending();

        Assert.assertEquals(1, got.size());
        Assert.assertEquals(MAP.getFullKey(), got.get(0));
    }

    /**
     * check the pending list is right before reading is attempted, and after as
     * well.
     * 
     * @throws IOException
     */
    @Test
    public void testPending2() throws IOException {
        loadBlock(MAP);
        SplitReader r = createMapReader();

        List<FullKey> got = r.getPending();

        assertEquals(1, got.size());
        assertEquals(MAP.getFullKey(), got.get(0));

        assertNull(r.getAny());
        got = r.getPending();

        assertEquals(2, got.size());
        assertEquals(C1.getFullKey(), got.get(0));
        assertEquals(C2.getFullKey(), got.get(1));
    }

    private static final TestData MAP = TestData.KEY_CONTENT_MAP;

    private static final TestData C1 = TestData.KEY_CONTENT_1;

    private static final TestData C2 = TestData.KEY_CONTENT_2;

    /**
     * check that the completed state is right.
     * 
     * @throws IOException
     */
    @Test
    public void testPendingAfterDone() throws IOException {
        loadBlock(MAP);
        loadBlock(C1);
        loadBlock(C2);

        SplitReader r = createMapReader();
        assertNotNull(r.getAny());
        assertNotNull(r.getAny());
        assertNull(r.getAny());

        assertTrue(r.isDone());

        List<FullKey> got = r.getPending();

        assertEquals(0, got.size());
    }

    /**
     * check that gaps in the map work
     * 
     * @throws IOException
     */
    @Test
    public void testReadMapPad() throws IOException {
	TestData d = TestData.KEY_CONTENT_MAP_PAD;
        loadBlock(d);
        loadBlock(C1);
        loadBlock(C2);

        SplitReader r = createReader(d);

	SplitReader.GetResult got = r.getFirst();
	assertEquals(0, got.getOffset());
	assertArrayEquals("01234".getBytes(), got.getData());

        assertEquals(10, (long) r.getOffset());

        got = r.getFirst();
	assertEquals(10, (long) got.getOffset());
	assertArrayEquals("56789".getBytes(), got.getData());

        assertNull(r.getOffset());
    }

    /**
     * check that overlaps in the map work
     * 
     * @throws IOException
     */
    @Test
    public void testReadMapOverlap() throws IOException {
	TestData d = TestData.KEY_CONTENT_MAP_OVERLAP;
        loadBlock(d);
        loadBlock(C1);
        loadBlock(C2);

        SplitReader r = createReader(d);

	SplitReader.GetResult got = r.getFirst();
	assertEquals(0, (long) got.getOffset());
	assertArrayEquals("01234".getBytes(), got.getData());

        got = r.getFirst();
	assertEquals(3, (long) got.getOffset());
	assertArrayEquals("56789".getBytes(), got.getData());
    }

    /**
     * check that reading a list block works
     * 
     * @throws IOException
     */
    @Test
    public void testReadList() throws IOException {
	TestData d = TestData.KEY_CONTENT_LIST;
        loadBlock(d);
        loadBlock(C1);
        loadBlock(C2);

        SplitReader r = createReader(d);

	SplitReader.GetResult got = r.getFirst();
	assertEquals(0, (long) got.getOffset());
	assertArrayEquals("01234".getBytes(), got.getData());

        got = r.getFirst();
	assertEquals(5, (long) got.getOffset());
	assertArrayEquals("56789".getBytes(), got.getData());
    }

    private SplitReader createReader(TestData d) {
        return new SplitReader(d.getFullKey(), storage);
    }

    /**
     * check that reading a very old split key works
     * 
     * @throws IOException
     */
    @Test
    public void testReadSplit() throws IOException {
	TestData d = TestData.KEY_NAME_SPLIT;
        loadBlock(d);
	loadBlock(TestData.KEY_NAME_SPLIT_1);
        loadBlock(C1);
	loadBlock(TestData.KEY_NAME_SPLIT_2);
        loadBlock(C2);

        SplitReader r = createReader(d);

	SplitReader.GetResult got = r.getFirst();
	assertEquals(0, (long) got.getOffset());
	assertArrayEquals("01234".getBytes(), got.getData());

        got = r.getFirst();
	assertEquals(5, (long) got.getOffset());
	assertArrayEquals("56789".getBytes(), got.getData());
    }

}
