package org.yi.happy.archive;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;
import org.yi.happy.archive.file_system.FakeFileSystem;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link FileStoreListMain}.
 */
public class FileStoreListMainTest {
    /**
     * A good run.
     * 
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        FileSystem fs = new FakeFileSystem();
        String base = "store";
        BlockStore store = new FileBlockStore(fs, base);
        store.put(TestData.KEY_CONTENT.getEncodedBlock());
        store.put(TestData.KEY_CONTENT_1.getEncodedBlock());
        store.put(TestData.KEY_CONTENT_2.getEncodedBlock());

        StringWriter out = new StringWriter();

        new FileStoreListMain(fs, out).run("store");

        String want = TestData.KEY_CONTENT.getLocatorKey() + "\n"
                + TestData.KEY_CONTENT_2.getLocatorKey() + "\n"
                + TestData.KEY_CONTENT_1.getLocatorKey() + "\n";

        assertEquals(want, out.toString());
    }

}
