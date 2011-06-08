package org.yi.happy.archive;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import org.junit.Test;
import org.yi.happy.archive.file_system.FakeFileSystem;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.tag.Tag;
import org.yi.happy.archive.tag.TagParse;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link FileStoreTagPutMain}.
 */
public class FileStoreTagPutMainTest {
    /**
     * store a named file
     * 
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        FileSystem fs = new FakeFileSystem();
        fs.save("test.txt", TestData.FILE_CONTENT.getBytes());

        ByteArrayOutputStream out0 = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(out0);

        new FileStoreTagPutMain(fs, out).run("--store", "store", "test.txt");

        out.flush();

        List<Tag> tags = TagParse.parse(out0.toByteArray());
        assertEquals(1, tags.size());
        assertEquals("test.txt", tags.get(0).get("name"));
        assertEquals("file", tags.get(0).get("type"));
        assertEquals(TestData.KEY_CONTENT_AES128.getFullKey().toString(), tags
                .get(0)
                .get("data"));
    }

    /**
     * check for the size and hash part of the tag.
     * 
     * @throws IOException
     */
    @Test
    public void test2() throws IOException {
        FileSystem fs = new FakeFileSystem();
        fs.save("test.txt", TestData.FILE_CONTENT.getBytes());

        ByteArrayOutputStream out0 = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(out0);

        new FileStoreTagPutMain(fs, out).run("--store", "store", "test.txt");

        out.flush();

        List<Tag> tags = TagParse.parse(out0.toByteArray());
        assertEquals(1, tags.size());
        assertEquals("6", tags.get(0).get("size"));
        assertEquals(
                "sha-256:5891b5b522d5df086d0ff0b110fbd9d21bb4fc7163af34d08286a2e846f6be03",
                tags.get(0).get("hash"));
    }

    /*
     * TODO include modification time in the tag.
     * 
     * This would require a time source for the fake file system, and tracking
     * of modification times in the fake file system.
     */
}
