package org.yi.happy.archive;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.yi.happy.annotate.NeedFailureTest;
import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.commandLine.EnvBuilder;
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
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void test1() throws IOException, InterruptedException,
            ExecutionException {
        String mapKey = TestData.KEY_CONTENT_MAP.getLocatorKey().toString();

        FileSystem fs = new FakeFileSystem();
        fs.mkdir("index");
        fs.mkdir("index/onsite");
        fs.save("index/onsite/01", TestData.INDEX_MAP.getBytes());
        fs.save("request", ByteString.toUtf8(mapKey + "\n"));

        ByteArrayOutputStream out0 = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(out0, true, "UTF-8");

        Env env = new EnvBuilder().withIndex("index").addArgument("request")
                .create();
        new IndexSearchMain(fs, out).run(env);

        String o = out0.toString("UTF-8");
        assertEquals("onsite\t01\t00.dat\t" + mapKey + "\n", o);
    }

    /**
     * search for one key with two indexes.
     * 
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void test2() throws IOException, InterruptedException,
            ExecutionException {
        String mapKey = TestData.KEY_CONTENT_MAP.getLocatorKey().toString();

        FileSystem fs = new FakeFileSystem();
        fs.mkdir("index");
        fs.mkdir("index/onsite");
        fs.save("index/onsite/01", TestData.INDEX_MAP.getBytes());
        fs.mkdir("index/offsite");
        fs.save("index/offsite/02", TestData.INDEX_MAP.getBytes());
        fs.save("request", ByteString.toUtf8(mapKey + "\n"));
        ByteArrayOutputStream out0 = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(out0, true, "UTF-8");

        Env env = new EnvBuilder().withIndex("index").addArgument("request")
                .create();
        new IndexSearchMain(fs, out).run(env);

        String o = out0.toString("UTF-8");
        assertEquals("offsite\t02\t00.dat\t" + mapKey + "\n"
                + "onsite\t01\t00.dat\t" + mapKey + "\n", o);
    }

    /**
     * search for two keys with two indexes.
     * 
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void test3() throws IOException, InterruptedException,
            ExecutionException {
        String mapKey = TestData.KEY_CONTENT_MAP.getLocatorKey().toString();
        String partKey = TestData.KEY_CONTENT_1.getLocatorKey().toString();

        FileSystem fs = new FakeFileSystem();
        fs.mkdir("index");
        fs.mkdir("index/onsite");
        fs.save("index/onsite/01", TestData.INDEX_MAP.getBytes());
        fs.mkdir("index/offsite");
        fs.save("index/offsite/02", TestData.INDEX_MAP.getBytes());
        fs.save("request", ByteString.toUtf8(mapKey + "\n" + partKey + "\n"));
        ByteArrayOutputStream out0 = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(out0, true, "UTF-8");

        Env env = new EnvBuilder().withIndex("index").addArgument("request")
                .create();
        new IndexSearchMain(fs, out).run(env);

        String o = out0.toString("UTF-8");
        assertEquals("offsite\t02\t00.dat\t" + mapKey + "\n"
                + "offsite\t02\t01.dat\t" + partKey + "\n"
                + "onsite\t01\t00.dat\t" + mapKey + "\n"
                + "onsite\t01\t01.dat\t" + partKey + "\n", o);
    }

    /**
     * search for two keys with two compressed indexes.
     * 
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void test4() throws IOException, InterruptedException,
            ExecutionException {
        String mapKey = TestData.KEY_CONTENT_MAP.getLocatorKey().toString();
        String partKey = TestData.KEY_CONTENT_1.getLocatorKey().toString();

        FileSystem fs = new FakeFileSystem();
        fs.mkdir("index");
        fs.mkdir("index/onsite");
        fs.save("index/onsite/01.gz", TestData.INDEX_MAP_GZ.getBytes());
        fs.mkdir("index/offsite");
        fs.save("index/offsite/02.gz", TestData.INDEX_MAP_GZ.getBytes());
        fs.save("request", ByteString.toUtf8(mapKey + "\n" + partKey + "\n"));
        ByteArrayOutputStream out0 = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(out0, true, "UTF-8");

        Env env = new EnvBuilder().withIndex("index").addArgument("request")
                .create();
        new IndexSearchMain(fs, out).run(env);

        String o = out0.toString("UTF-8");
        assertEquals("offsite\t02\t00.dat\t" + mapKey + "\n"
                + "offsite\t02\t01.dat\t" + partKey + "\n"
                + "onsite\t01\t00.dat\t" + mapKey + "\n"
                + "onsite\t01\t01.dat\t" + partKey + "\n", o);
    }

    /**
     * If there is a stray file in the index, then it is ignored.
     * 
     * @throws Exception
     */
    @Test
    public void skipFiles() throws Exception {
        String mapKey = TestData.KEY_CONTENT_MAP.getLocatorKey().toString();

        FileSystem fs = new FakeFileSystem();
        fs.mkdir("index");
        fs.save("index/strayfile", new byte[0]);
        fs.mkdir("index/onsite");
        fs.save("index/onsite/01", TestData.INDEX_MAP.getBytes());
        fs.save("request", ByteString.toUtf8(mapKey + "\n"));
        ByteArrayOutputStream out0 = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(out0, true, "UTF-8");

        Env env = new EnvBuilder().withIndex("index").addArgument("request")
                .create();
        new IndexSearchMain(fs, out).run(env);

        String o = out0.toString("UTF-8");
        assertEquals("onsite\t01\t00.dat\t" + mapKey + "\n", o);
    }
}
