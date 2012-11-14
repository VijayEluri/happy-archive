package org.yi.happy.archive;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.yi.happy.archive.file_system.FakeFileSystem;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link StoreRemoveMain}.
 */
public class StoreRemoveMainTest {
    /**
     * check an expected good case.
     * 
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        FileSystem fs = new FakeFileSystem();
        BlockStore store = new StorageMemory();
        store.put(TestData.KEY_CONTENT.getEncodedBlock());
        store.put(TestData.KEY_CONTENT_1.getEncodedBlock());
        store.put(TestData.KEY_CONTENT_2.getEncodedBlock());
        fs.save("flush.lst", ByteString.toUtf8(""
                + TestData.KEY_CONTENT_1.getLocatorKey() + "\n"
                + TestData.KEY_CONTENT_2.getLocatorKey() + "\n"));

        List<String> args = Arrays.asList("flush.lst");
        new StoreRemoveMain(store, fs, args).run();

        assertTrue(store.contains(TestData.KEY_CONTENT.getLocatorKey()));
        assertFalse(store.contains(TestData.KEY_CONTENT_1.getLocatorKey()));
        assertFalse(store.contains(TestData.KEY_CONTENT_2.getLocatorKey()));
    }

}
