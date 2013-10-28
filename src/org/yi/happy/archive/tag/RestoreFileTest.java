package org.yi.happy.archive.tag;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;
import org.yi.happy.archive.MapClearBlockSource;
import org.yi.happy.archive.file_system.FakeFileSystem;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link RestoreFile}.
 */
public class RestoreFileTest {
    /**
     * restore a file with all the blocks initially available.
     * 
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        FileSystem fs = new FakeFileSystem();
        MapClearBlockSource store = new MapClearBlockSource();
        store.put(TestData.KEY_CONTENT);

        RestoreFile f = new RestoreFile(TestData.KEY_CONTENT.getFullKey(),
                store, "test.dat", fs);
        f.step();

        assertEquals(true, f.isDone());
        assertArrayEquals(TestData.FILE_CONTENT.getBytes(), fs.load("test.dat"));
    }

    /**
     * restore a file with no blocks initially available.
     * 
     * @throws IOException
     */
    @Test
    public void test2() throws IOException {
        FileSystem fs = new FakeFileSystem();
        MapClearBlockSource store = new MapClearBlockSource();

        RestoreFile f = new RestoreFile(TestData.KEY_CONTENT.getFullKey(),
                store, "test.dat", fs);
        f.step();

        assertEquals(0, f.getProgress());
        assertEquals(false, f.isDone());
        assertEquals(Arrays.asList(TestData.KEY_CONTENT.getFullKey()), f
                .getPending());

        store.put(TestData.KEY_CONTENT);
        f.step();

        assertEquals(1, f.getProgress());
        assertEquals(true, f.isDone());
        assertEquals(Arrays.asList(), f.getPending());
    }

}
