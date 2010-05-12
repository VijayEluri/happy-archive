package org.yi.happy.archive;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;
import org.yi.happy.annotate.NeedFailureTest;
import org.yi.happy.archive.file_system.FakeFileSystem;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link IndexSearchMain}.
 */
@NeedFailureTest
public class IndexSearchMainTest {
    /**
     * search for one key with one index.
     * 
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        String mapKey = TestData.KEY_CONTENT_MAP.getLocatorKey().toString();

        FileSystem fs = new FakeFileSystem();
        fs.mkdir("index");
        fs.mkdir("index/onsite");
        fs.save("index/onsite/01", TestData.INDEX_MAP.getBytes());
        fs.save("request", ByteString.toUtf8(mapKey + "\n"));
        StringWriter out = new StringWriter();

        new IndexSearchMain(fs, out).run("index", "request");

        String o = out.toString();
        assertEquals("onsite\t01\t00.dat\t" + mapKey + "\n", o);
    }

    /**
     * search for one key with two indexes.
     * 
     * @throws IOException
     */
    @Test
    public void test2() throws IOException {
        String mapKey = TestData.KEY_CONTENT_MAP.getLocatorKey().toString();

        FileSystem fs = new FakeFileSystem();
        fs.mkdir("index");
        fs.mkdir("index/onsite");
        fs.save("index/onsite/01", TestData.INDEX_MAP.getBytes());
        fs.mkdir("index/offsite");
        fs.save("index/offsite/02", TestData.INDEX_MAP.getBytes());
        fs.save("request", ByteString.toUtf8(mapKey + "\n"));
        StringWriter out = new StringWriter();

        new IndexSearchMain(fs, out).run("index", "request");

        String o = out.toString();
        assertEquals("offsite\t02\t00.dat\t" + mapKey + "\n"
                + "onsite\t01\t00.dat\t" + mapKey + "\n", o);
    }

    /**
     * search for two keys with two indexes.
     * 
     * @throws IOException
     */
    @Test
    public void test3() throws IOException {
        String mapKey = TestData.KEY_CONTENT_MAP.getLocatorKey().toString();
        String partKey = TestData.KEY_CONTENT_1.getLocatorKey().toString();

        FileSystem fs = new FakeFileSystem();
        fs.mkdir("index");
        fs.mkdir("index/onsite");
        fs.save("index/onsite/01", TestData.INDEX_MAP.getBytes());
        fs.mkdir("index/offsite");
        fs.save("index/offsite/02", TestData.INDEX_MAP.getBytes());
        fs.save("request", ByteString.toUtf8(mapKey + "\n" + partKey + "\n"));
        StringWriter out = new StringWriter();

        new IndexSearchMain(fs, out).run("index", "request");

        String o = out.toString();
        assertEquals("offsite\t02\t00.dat\t" + mapKey + "\n"
                + "offsite\t02\t01.dat\t" + partKey + "\n"
                + "onsite\t01\t00.dat\t" + mapKey + "\n"
                + "onsite\t01\t01.dat\t" + partKey + "\n", o);
    }

    /**
     * search for two keys with two compressed indexes.
     * 
     * @throws IOException
     */
    @Test
    public void test4() throws IOException {
        String mapKey = TestData.KEY_CONTENT_MAP.getLocatorKey().toString();
        String partKey = TestData.KEY_CONTENT_1.getLocatorKey().toString();

        FileSystem fs = new FakeFileSystem();
        fs.mkdir("index");
        fs.mkdir("index/onsite");
        fs.save("index/onsite/01.gz", TestData.INDEX_MAP_GZ.getBytes());
        fs.mkdir("index/offsite");
        fs.save("index/offsite/02.gz", TestData.INDEX_MAP_GZ.getBytes());
        fs.save("request", ByteString.toUtf8(mapKey + "\n" + partKey + "\n"));
        StringWriter out = new StringWriter();

        new IndexSearchMain(fs, out).run("index", "request");

        String o = out.toString();
        assertEquals("offsite\t02\t00.dat\t" + mapKey + "\n"
                + "offsite\t02\t01.dat\t" + partKey + "\n"
                + "onsite\t01\t00.dat\t" + mapKey + "\n"
                + "onsite\t01\t01.dat\t" + partKey + "\n", o);
    }
}
