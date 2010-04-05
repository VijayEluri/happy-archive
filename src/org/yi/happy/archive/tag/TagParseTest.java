package org.yi.happy.archive.tag;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.yi.happy.archive.block.parser.Range;
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

    /**
     * in one common usage parsing will be against a fragment, so finding the
     * record divisions will be necessary.
     * 
     * @throws IOException
     */
    @Test
    public void testFindRecordSeparator1() throws IOException {
        byte[] d = TestData.TAG_FILES.getBytes();
        Range r = new Range(0, 448);

        r = TagParse.findRecordSeparator(d, r);

        assertEquals(new Range(223, 1), r);
    }

    /**
     * in one common usage parsing will be against a fragment, so finding the
     * record divisions will be necessary.
     * 
     * @throws IOException
     */
    @Test
    public void testFindRecordSeparator2() throws IOException {
        byte[] d = TestData.TAG_FILES.getBytes();
        Range r = new Range(224, 223);

        r = TagParse.findRecordSeparator(d, r);

        assertEquals(new Range(447, 0), r);
    }

    /**
     * so we should be able to parse against a fragment.
     * 
     * @throws IOException
     */
    @Test
    public void testPraseFragment1() throws IOException {
        byte[] d = TestData.TAG_FILES.getBytes();
        Range r = new Range(0, 224);

        List<Tag> t = TagParse.parse(d, r);

        assertEquals(1, t.size());
    }

    /**
     * so we should be able to parse against a fragment.
     * 
     * @throws IOException
     */
    @Test
    public void testParseFragment2() throws IOException {
        byte[] d = TestData.TAG_FILES.getBytes();
        Range r = new Range(0, d.length);

        List<Tag> t = TagParse.parse(d, r);

        assertEquals(2, t.size());
    }

    /**
     * It would be good to be able to find the part that is full records.
     * 
     * @throws IOException
     */
    @Test
    public void testFindCompleteRecords1() throws IOException {
        byte[] d = TestData.TAG_FILES.getBytes();
        Range r = new Range(0, d.length);

        r = TagParse.findFullRecords(d, r);

        // after the blank line on the second record.
        assertEquals(new Range(0, 448), r);
    }

    /**
     * It would be good to be able to find the part that is full records.
     * 
     * @throws IOException
     */
    @Test
    public void testFindCompleteRecords2() throws IOException {
        byte[] d = TestData.TAG_FILES.getBytes();
        // before the blank line on the second record.
        Range r = new Range(0, 447);

        r = TagParse.findFullRecords(d, r);

        // after the blank line on the first record.
        assertEquals(new Range(0, 224), r);
    }

}
