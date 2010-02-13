package org.yi.happy.archive;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.yi.happy.archive.block.encoder.BlockEncoderFactory;
import org.yi.happy.archive.test_data.TestData;

/**
 * test the functioning of {@link KeyInputStream}
 * 
 * @author sarah dot a dot happy at gmail dot com
 */
public class KeyInputStreamTest {
    /**
     * second content block
     */
    private static final TestData C2 = TestData.KEY_CONTENT_2;

    /**
     * first content block
     */
    private static final TestData C1 = TestData.KEY_CONTENT_1;

    /**
     * map block
     */
    private static final TestData MAP = TestData.KEY_CONTENT_MAP;

    /**
     * raw store
     */
    private BlockStore rawStore;

    /**
     * decoding store
     */
    private RetrieveBlockStorage store;

    /**
     * create objects common to all tests
     */
    @Before
    public void before() {
        rawStore = new StorageMemory();
        store = new RetrieveBlockStorage(rawStore);
    }

    /**
     * clean up objects common to all tests
     */
    @After
    public void after() {
        rawStore = null;
        store = null;
    }

    /**
     * test just reading with everything available
     * 
     * @throws IOException
     */
    @Test
    public void testRead() throws IOException {
	rawStore.put(MAP.getEncodedBlock());
	rawStore.put(C1.getEncodedBlock());
	rawStore.put(C2.getEncodedBlock());

        KeyInputStream in = new KeyInputStream(MAP.getFullKey(), store,
                new NotReadyError());

        assertReadMap(in);
    }

    /**
     * read and check all ten bytes of the map key
     * 
     * @param in
     *            the input stream
     * @throws IOException
     */
    private void assertReadMap(InputStream in) throws IOException {
	assertEquals('0', in.read());
	assertEquals('1', in.read());
	assertEquals('2', in.read());
	assertEquals('3', in.read());
	assertEquals('4', in.read());
	assertEquals('5', in.read());
	assertEquals('6', in.read());
	assertEquals('7', in.read());
	assertEquals('8', in.read());
	assertEquals('9', in.read());
        assertEquals(-1, in.read());
    }

    /**
     * check that the stream will work if not all the blocks are available
     * immediately.
     * 
     * @throws IOException
     */
    @Test
    public void testReadDelay() throws IOException {
	List<TestData> script = new ArrayList<TestData>();
        script.add(null);
        script.add(MAP);
        script.add(null);
        script.add(C1);
        script.add(C2);

        NotReadyHandler handler = new ScriptNotReadyHandler(rawStore, script);

        KeyInputStream in = new KeyInputStream(MAP.getFullKey(), store,
                handler);

        assertReadMap(in);
    }

    /**
     * check that overlapping bytes are dropped.
     * 
     * @throws IOException
     */
    @Test
    public void testReadOverlap() throws IOException {
	TestData d = TestData.KEY_CONTENT_MAP_OVERLAP;
	rawStore.put(d.getEncodedBlock());
	rawStore.put(C1.getEncodedBlock());
	rawStore.put(C2.getEncodedBlock());

        KeyInputStream in = new KeyInputStream(d.getFullKey(), store,
                new NotReadyError());

        byte[] got = new byte[8];
        assertEquals(8, in.read(got));
        byte[] want = ByteString.toBytes("01234789");
        assertArrayEquals(want, got);
        assertEquals(-1, in.read());
    }

    /**
     * check that padding bytes are inserted.
     * 
     * @throws IOException
     */
    @Test
    public void testReadPad() throws IOException {
	TestData d = TestData.KEY_CONTENT_MAP_PAD;
	rawStore.put(d.getEncodedBlock());
	rawStore.put(C1.getEncodedBlock());
	rawStore.put(C2.getEncodedBlock());

        KeyInputStream in = new KeyInputStream(d.getFullKey(), store,
                new NotReadyError());
        byte[] buff = new byte[15];
        assertEquals(15, in.read(buff));
        byte[] want = ByteString.toBytes("01234" + "\0\0\0\0\0" + "56789");
        assertArrayEquals(want, buff);
        assertEquals(-1, in.read());
    }

    /**
     * found a bug where a byte over 0x7f causes the stream to appear to end,
     * check for it.
     * 
     * @throws IOException
     */
    @Test
    public void testReadNegativeByte() throws IOException {
        KeyOutputStream out = new KeyOutputStream(new StoreBlockStorage(
		BlockEncoderFactory.getContentDefault(), rawStore));
        out.write(0xff);
        out.close();

        KeyInputStream in = new KeyInputStream(out.getFullKey(), store,
                new NotReadyError());
        assertEquals(0xff, in.read());
        assertEquals(-1, in.read());
    }
}
