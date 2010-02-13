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
        rawStorage.put(MAP.getEncodedBlock());
        SplitReader r = new SplitReader(MAP.getFullKey(), storage);

	Fragment got = r.getAny();

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
        rawStorage.put(MAP.getEncodedBlock());
        rawStorage.put(C2.getEncodedBlock());
        SplitReader r = new SplitReader(MAP.getFullKey(), storage);

	Fragment got = r.getAny();

	assertEquals(new Fragment(5, D2), got);

	got = r.getAny();

	assertEquals(null, got);

        rawStorage.put(C1.getEncodedBlock());
        got = r.getAny();

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
        rawStorage.put(MAP.getEncodedBlock());
        rawStorage.put(C2.getEncodedBlock());
        SplitReader r = new SplitReader(MAP.getFullKey(), storage);

	Fragment got = r.getFirst();

	assertEquals(null, got);
    }

    /**
     * get first with the base and first loaded should get something.
     * 
     * @throws IOException
     */
    @Test
    public void testGetFirst2() throws IOException {
        rawStorage.put(MAP.getEncodedBlock());
        rawStorage.put(C1.getEncodedBlock());
        SplitReader r = new SplitReader(MAP.getFullKey(), storage);

	Fragment got = r.getFirst();

	assertEquals(new Fragment(0, D1), got);
    }

    /**
     * check that the pending list works, just the base nothing ready
     */
    @Test
    public void testPending1() {
        SplitReader r = new SplitReader(MAP.getFullKey(), storage);

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
        rawStorage.put(MAP.getEncodedBlock());
        SplitReader r = new SplitReader(MAP.getFullKey(), storage);

	assertEquals(Arrays.asList(MAP.getFullKey()), r.getPending());

        assertNull(r.getAny());

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
        rawStorage.put(MAP.getEncodedBlock());
        rawStorage.put(C1.getEncodedBlock());
        rawStorage.put(C2.getEncodedBlock());

        SplitReader r = new SplitReader(MAP.getFullKey(), storage);
	Fragment got = r.getAny();

	assertNotNull(got);

	got = r.getAny();

	assertNotNull(got);

	got = r.getAny();

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
        rawStorage.put(d.getEncodedBlock());
        rawStorage.put(C1.getEncodedBlock());
        rawStorage.put(C2.getEncodedBlock());

        SplitReader r = new SplitReader(d.getFullKey(), storage);

	Fragment got = r.getFirst();
	assertEquals(new Fragment(0, D1), got);

	assertEquals((Long) 10L, r.getOffset());

        got = r.getFirst();
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
        rawStorage.put(d.getEncodedBlock());
        rawStorage.put(C1.getEncodedBlock());
        rawStorage.put(C2.getEncodedBlock());

        SplitReader r = new SplitReader(d.getFullKey(), storage);

	Fragment got = r.getFirst();
	assertEquals(new Fragment(0, D1), got);

        got = r.getFirst();
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
        rawStorage.put(d.getEncodedBlock());
        rawStorage.put(C1.getEncodedBlock());
        rawStorage.put(C2.getEncodedBlock());

        SplitReader r = new SplitReader(d.getFullKey(), storage);
	Fragment got;

	got = r.getFirst();

	assertEquals(new Fragment(0, D1), got);

        got = r.getFirst();

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
        rawStorage.put(d.getEncodedBlock());
	rawStorage.put(TestData.KEY_NAME_SPLIT_1.getEncodedBlock());
        rawStorage.put(C1.getEncodedBlock());
	rawStorage.put(TestData.KEY_NAME_SPLIT_2.getEncodedBlock());
        rawStorage.put(C2.getEncodedBlock());
        SplitReader r = new SplitReader(d.getFullKey(), storage);
	Fragment got;

	got = r.getFirst();

	assertEquals(new Fragment(0, D1), got);

        got = r.getFirst();

	assertEquals(new Fragment(5, D2), got);
    }

    /**
     * check that reading with the wrong key will throw an {@link IOException}.
     * 
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testBad() throws IOException {
	rawStorage.put(C1.getEncodedBlock());

	ContentFullKey k = (ContentFullKey) C1.getFullKey();
	k = new ContentFullKey(k.getHash(), new Bytes());

	SplitReader r = new SplitReader(k, storage);

	r.getAny();
    }

    /**
     * check that reading with the wrong key will throw an {@link IOException}.
     * 
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testBad2() throws IOException {
	rawStorage.put(C1.getEncodedBlock());

	ContentFullKey k = (ContentFullKey) C1.getFullKey();
	{
	    byte[] b = k.getPass().toByteArray();
	    b[0] ^= 0x01;
	    k = new ContentFullKey(k.getHash(), new Bytes(b));
	}

	SplitReader r = new SplitReader(k, storage);

	r.getAny();
    }
}
