package org.yi.happy.archive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.yi.happy.archive.file_system.FileSystemMemory;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link StoreStreamPutMain}.
 */
public class StoreStreamPutMainTest {
    /**
     * an expected good store.
     * 
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        FileSystem fs = new FileSystemMemory();
        InputStream in = new ByteArrayInputStream(ByteString.toBytes("hello\n"));
        CapturePrintStream out = CapturePrintStream.create();
        BlockStore store = new BlockStoreMemory();

        new StoreStreamPutMain(store, fs, in, out).run();

        assertEquals(TestData.KEY_CONTENT_AES128.getFullKey() + "\n", out
                .toString());

        assertTrue(store.contains(TestData.KEY_CONTENT_AES128.getLocatorKey()));
    }
}
