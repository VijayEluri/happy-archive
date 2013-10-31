package org.yi.happy.archive;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.yi.happy.archive.file_system.FileSystemMemory;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.key.ContentLocatorKey;
import org.yi.happy.archive.key.HashValue;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link BuildImageMain}.
 */
public class BuildImageMainTest {
    /**
     * A sample good run.
     * 
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        FileSystem fs = new FileSystemMemory();
        BlockStore store = new BlockStoreMemory();
        store.put(TestData.KEY_CONTENT.getEncodedBlock());
        CapturePrintStream out = CapturePrintStream.create();
        fs.save("outstanding",
                ByteString.toUtf8(TestData.KEY_CONTENT.getLocatorKey()
                        .toString() + "\n"));
        fs.mkdir("output");

        List<String> args = Arrays.asList("outstanding", "output", "4700");
        new BuildImageMain(store, fs, out, null, args).run();

        assertArrayEquals(TestData.KEY_CONTENT.getBytes(),
                fs.load("output/00000000.dat"));
        assertEquals("1\t1\n", out.toString());
    }

    /**
     * Another sample good run.
     * 
     * @throws IOException
     */
    @Test
    public void test2() throws IOException {
        FileSystem fs = new FileSystemMemory();
        BlockStore store = new BlockStoreMemory();
        store.put(TestData.KEY_CONTENT.getEncodedBlock());
        store.put(TestData.KEY_CONTENT_1.getEncodedBlock());
        CapturePrintStream out = CapturePrintStream.create();
        fs.save("outstanding",
                ByteString.toUtf8(TestData.KEY_CONTENT.getLocatorKey() + "\n"
                        + TestData.KEY_CONTENT_1.getLocatorKey() + "\n"));
        fs.mkdir("output");

        List<String> args = Arrays.asList("outstanding", "output", "4700");
        new BuildImageMain(store, fs, out, null, args).run();

        assertArrayEquals(TestData.KEY_CONTENT.getBytes(),
                fs.load("output/00000000.dat"));
        assertArrayEquals(TestData.KEY_CONTENT_1.getBytes(),
                fs.load("output/00000001.dat"));
        assertEquals("2\t1\n", out.toString());
    }

    /**
     * Broken block in store
     * 
     * @throws IOException
     */
    @Test
    public void testBrokenBlockInStore() throws IOException {
        FileSystem fs = new FileSystemMemory();
        BlockStoreMemory store = new BlockStoreMemory();
        store.put(TestData.KEY_CONTENT.getEncodedBlock());
        store.put(TestData.KEY_CONTENT_1.getEncodedBlock());

        /* put a broken block in the store */
        store.putBroken(new ContentLocatorKey(new HashValue(0x00, 0x00, 0x00,
                0x00)));

        CapturePrintStream out = CapturePrintStream.create();
        fs.save("outstanding",
                ByteString.toUtf8(TestData.KEY_CONTENT.getLocatorKey() + "\n"
                        + TestData.KEY_CONTENT_1.getLocatorKey() + "\n"
                        + "content-hash:00000000\n"));
        fs.mkdir("output");

        List<String> args = Arrays.asList("outstanding", "output", "4700");
        new BuildImageMain(store, fs, out, new NullPrintStream(), args).run();

        assertArrayEquals(TestData.KEY_CONTENT.getBytes(),
                fs.load("output/00000000.dat"));
        assertArrayEquals(TestData.KEY_CONTENT_1.getBytes(),
                fs.load("output/00000001.dat"));
        assertEquals("2\t1\n", out.toString());
    }

}
