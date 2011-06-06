package org.yi.happy.archive.tag;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.yi.happy.archive.test_data.TestData;

/**
 * tests for {@link TagParse}.
 */
public class TagParseTest {
    /**
     * parse the entire block.
     * 
     * @throws IOException
     */
    @Test
    public void testFullParse1() throws IOException {
        byte[] d = TestData.TAG_FILE.getBytes();

        List<Tag> t = TagParse.parse(d);

        assertEquals(1, t.size());
        assertEquals("hello.txt", t.get(0).get("name"));
        assertEquals("file", t.get(0).get("type"));
        assertEquals(TestData.KEY_CONTENT.getFullKey().toString(), t.get(0)
                .get("data"));
    }

    /**
     * parse the entire block.
     * 
     * @throws IOException
     */
    @Test
    public void testFullParse2() throws IOException {
        byte[] d = TestData.TAG_FILES.getBytes();

        List<Tag> t = TagParse.parse(d);

        assertEquals(2, t.size());
    }

}
