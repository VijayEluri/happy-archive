package org.yi.happy.archive;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.yi.happy.archive.tag.Tag;
import org.yi.happy.archive.tag.TagIterator;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link StoreTagPutMain}.
 */
public class StoreTagPutMainTest {
    /**
     * store a named file
     * 
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        FileStore files = new FileStoreMemory();
        files.put("test.txt", TestData.FILE_CONTENT.getBytes());
        BlockStore store = new BlockStoreMemory();

        CapturePrintStream out = CapturePrintStream.create();

        List<String> args = Arrays.asList("test.txt");
        new StoreTagPutMain(store, files, out, args).run();

        out.flush();

        List<Tag> tags = tags(out);
        assertEquals(1, tags.size());
        assertEquals("test.txt", tags.get(0).get("name"));
        assertEquals("file", tags.get(0).get("type"));
        assertEquals(TestData.KEY_CONTENT_AES128.getFullKey().toString(), tags
                .get(0).get("data"));
    }

    /**
     * check for the size and hash part of the tag.
     * 
     * @throws IOException
     */
    @Test
    public void test2() throws IOException {
        FileStore files = new FileStoreMemory();
        files.put("test.txt", TestData.FILE_CONTENT.getBytes());
        BlockStore store = new BlockStoreMemory();

        CapturePrintStream out = CapturePrintStream.create();

        List<String> args = Arrays.asList("test.txt");
        new StoreTagPutMain(store, files, out, args).run();

        out.flush();

        List<Tag> tags = tags(out);
        assertEquals(1, tags.size());
        assertEquals("6", tags.get(0).get("size"));
        assertEquals("sha-256:5891b5b522d5df086d0ff0b110fbd9d2"
                + "1bb4fc7163af34d08286a2e846f6be03", tags.get(0).get("hash"));
    }

    private static List<Tag> tags(CapturePrintStream out) {
        Reader in = new StringReader(out.toString());
        List<Tag> tags = new ArrayList<Tag>();
        for (Tag tag : new TagIterator(in)) {
            tags.add(tag);
        }
        return tags;
    }

    /*
     * TODO include modification time in the tag.
     * 
     * This would require a time source for the fake file system, and tracking
     * of modification times in the fake file system.
     */
}
