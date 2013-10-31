package org.yi.happy.archive;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.file_system.FileStore;
import org.yi.happy.archive.file_system.FileStoreMemory;
import org.yi.happy.archive.key.ContentLocatorKey;
import org.yi.happy.archive.key.HashValue;
import org.yi.happy.archive.key.LocatorKey;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link BuildImageMain}.
 */
public class BuildImageMainTest {
    private BlockStoreMemory blocks;
    private FileStore files;
    private CapturePrintStream out;
    private CapturePrintStream err;

    /**
     * set up.
     */
    @Before
    public void before() {
        blocks = new BlockStoreMemory();
        files = new FileStoreMemory();
        out = CapturePrintStream.create();
        err = CapturePrintStream.create();
    }

    /**
     * tear down
     */
    @After
    public void after() {
        blocks = null;
        files = null;
        out = null;
        err = null;
    }

    /**
     * one block.
     * 
     * @throws IOException
     */
    @Test
    public void testOneBlock() throws IOException {
        TestData C0 = TestData.KEY_CONTENT;
        String OUT = "output";
        String SIZE_MB = "4700";

        blocks.put(block(C0));
        files.putDir(OUT);

        InputStream in = input(key(C0) + "\n");
        List<String> args = list(OUT, SIZE_MB);
        new BuildImageMain(blocks, files, in, out, err, args).run();

        assertArrayEquals(raw(C0), files.get(OUT + "/" + "00000000.dat"));
        assertEquals("1" + "\t" + "1" + "\n", out.toString());
    }

    /**
     * two blocks.
     * 
     * @throws IOException
     */
    @Test
    public void testTwoBlocks() throws IOException {
        TestData C0 = TestData.KEY_CONTENT;
        TestData C1 = TestData.KEY_CONTENT_1;
        String OUT = "output";
        String SIZE_MB = "4700";

        blocks.put(block(C0));
        blocks.put(block(C1));

        files.putDir(OUT);

        InputStream in = input(key(C0) + "\n" + key(C1) + "\n");

        List<String> args = list(OUT, SIZE_MB);
        new BuildImageMain(blocks, files, in, out, err, args).run();

        assertArrayEquals(raw(C0), files.get(OUT + "/" + "00000000.dat"));
        assertArrayEquals(raw(C1), files.get(OUT + "/" + "00000001.dat"));
        assertEquals("2" + "\t" + "1" + "\n", out.toString());
    }

    /**
     * Broken block in store
     * 
     * @throws IOException
     */
    @Test
    public void testBrokenBlockInStore() throws IOException {
        TestData C0 = TestData.KEY_CONTENT;
        TestData C1 = TestData.KEY_CONTENT_1;
        String OUT = "output";
        String SIZE = "4700";
        LocatorKey K = new ContentLocatorKey(new HashValue(0x00, 0x00, 0x00,
                0x00));

        blocks.put(block(C0));
        blocks.put(block(C1));
        blocks.putBroken(K);

        files.putDir("output");

        InputStream in = input(key(C0) + "\n" + key(C1) + "\n" + key(K) + "\n");

        List<String> args = list(OUT, SIZE);
        new BuildImageMain(blocks, files, in, out, err, args).run();

        assertArrayEquals(raw(C0), files.get(OUT + "/" + "00000000.dat"));
        assertArrayEquals(raw(C1), files.get(OUT + "/" + "00000001.dat"));
        assertEquals("2" + "\t" + "1" + "\n", out.toString());
    }

    private String key(LocatorKey key) {
        return key.toString();
    }

    private static InputStream input(String text) {
        byte[] bytes = ByteString.toUtf8(text);
        return new ByteArrayInputStream(bytes);
    }

    private byte[] raw(TestData item) throws IOException {
        return item.getBytes();
    }

    private String key(TestData item) {
        return item.getLocatorKey().toString();
    }

    private EncodedBlock block(TestData item) throws IOException {
        return item.getEncodedBlock();
    }

    private <T> List<T> list(T... items) {
        return Arrays.asList(items);
    }
}
