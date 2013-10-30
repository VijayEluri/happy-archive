package org.yi.happy.archive;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testOneIndex() throws IOException, InterruptedException,
            ExecutionException {
        String V = "index";
        String V0 = "onsite";
        String V00 = "01";
        String R = "request";
        TestData I = TestData.INDEX_MAP;
        TestData K0 = TestData.KEY_CONTENT_MAP;
        String N0 = "00.dat";

        FileSystem fs = new FakeFileSystem();
        fs.mkdir(V);
        fs.mkdir(V + "/" + V0);
        fs.save(V + "/" + V0 + "/" + V00, raw(I));
        fs.save(R, raw(key(K0) + "\n"));
        CapturePrintStream out = CapturePrintStream.create();

        IndexStore index = new IndexStoreFileSystem(fs, V);
        IndexSearch indexSearch = new IndexSearch(index);
        List<String> args = Arrays.asList(R);
        new IndexSearchMain(fs, out, indexSearch, args).run();

        StringBuilder sb = new StringBuilder();
        sb.append(V0 + "\t" + V00 + "\t" + N0 + "\t" + key(K0) + "\n");
        assertEquals(sb.toString(), out.toString());
    }

    /**
     * search for one key with two indexes.
     * 
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testTwoIndex() throws IOException, InterruptedException,
            ExecutionException {
        String V = "index";
        String V0 = "offsite";
        String V00 = "02";
        String V1 = "onsite";
        String V10 = "01";
        String R = "request";
        TestData I = TestData.INDEX_MAP;
        TestData K0 = TestData.KEY_CONTENT_MAP;
        String N0 = "00.dat";

        FileSystem fs = new FakeFileSystem();
        fs.mkdir(V);
        fs.mkdir(V + "/" + V1);
        fs.save(V + "/" + V1 + "/" + V10, raw(I));
        fs.mkdir(V + "/" + V0);
        fs.save(V + "/" + V0 + "/" + V00, raw(I));
        fs.save(R, raw(key(K0) + "\n"));
        CapturePrintStream out = CapturePrintStream.create();

        IndexStore index = new IndexStoreFileSystem(fs, V);
        IndexSearch indexSearch = new IndexSearch(index);
        List<String> args = Arrays.asList(R);
        new IndexSearchMain(fs, out, indexSearch, args).run();

        StringBuilder sb = new StringBuilder();
        sb.append(V0 + "\t" + V00 + "\t" + N0 + "\t" + key(K0) + "\n");
        sb.append(V1 + "\t" + V10 + "\t" + N0 + "\t" + key(K0) + "\n");
        assertEquals(sb.toString(), out.toString());
    }

    /**
     * search for two keys with two indexes.
     * 
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testMultipleKeys() throws IOException, InterruptedException,
            ExecutionException {
        String V = "index";
        String V0 = "offsite";
        String V00 = "02";
        String V1 = "onsite";
        String V10 = "01";
        String R = "request";
        TestData I = TestData.INDEX_MAP;
        TestData K0 = TestData.KEY_CONTENT_MAP;
        String N0 = "00.dat";
        TestData K1 = TestData.KEY_CONTENT_1;
        String N1 = "01.dat";

        FileSystem fs = new FakeFileSystem();
        fs.mkdir(V);
        fs.mkdir(V + "/" + V1);
        fs.save(V + "/" + V1 + "/" + V10, raw(I));
        fs.mkdir(V + "/" + V0);
        fs.save(V + "/" + V0 + "/" + V00, raw(I));
        fs.save(R, raw(key(K0) + "\n" + key(K1) + "\n"));
        CapturePrintStream out = CapturePrintStream.create();

        IndexStore index = new IndexStoreFileSystem(fs, V);
        IndexSearch indexSearch = new IndexSearch(index);
        List<String> args = Arrays.asList(R);
        new IndexSearchMain(fs, out, indexSearch, args).run();

        StringBuilder sb = new StringBuilder();
        sb.append(V0 + "\t" + V00 + "\t" + N0 + "\t" + key(K0) + "\n");
        sb.append(V0 + "\t" + V00 + "\t" + N1 + "\t" + key(K1) + "\n");
        sb.append(V1 + "\t" + V10 + "\t" + N0 + "\t" + key(K0) + "\n");
        sb.append(V1 + "\t" + V10 + "\t" + N1 + "\t" + key(K1) + "\n");
        assertEquals(sb.toString(), out.toString());
    }

    /**
     * search for two keys with two compressed indexes.
     * 
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testCompressedIndex() throws IOException, InterruptedException,
            ExecutionException {
        String V = "index";
        String V0 = "offsite";
        String V00 = "02";
        String V00Z = "02.gz";
        String V1 = "onsite";
        String V10 = "01";
        String V10Z = "01.gz";
        String R = "request";
        TestData IZ = TestData.INDEX_MAP_GZ;
        TestData K0 = TestData.KEY_CONTENT_MAP;
        String N0 = "00.dat";
        TestData K1 = TestData.KEY_CONTENT_1;
        String N1 = "01.dat";

        FileSystem fs = new FakeFileSystem();
        fs.mkdir(V);
        fs.mkdir(V + "/" + V0);
        fs.save(V + "/" + V0 + "/" + V00Z, raw(IZ));
        fs.mkdir(V + "/" + V1);
        fs.save(V + "/" + V1 + "/" + V10Z, raw(IZ));
        fs.save(R, raw(key(K0) + "\n" + key(K1) + "\n"));
        CapturePrintStream out = CapturePrintStream.create();

        IndexStore index = new IndexStoreFileSystem(fs, V);
        IndexSearch indexSearch = new IndexSearch(index);
        List<String> args = Arrays.asList("request");
        new IndexSearchMain(fs, out, indexSearch, args).run();

        StringBuilder sb = new StringBuilder();
        sb.append(V0 + "\t" + V00 + "\t" + N0 + "\t" + key(K0) + "\n");
        sb.append(V0 + "\t" + V00 + "\t" + N1 + "\t" + key(K1) + "\n");
        sb.append(V1 + "\t" + V10 + "\t" + N0 + "\t" + key(K0) + "\n");
        sb.append(V1 + "\t" + V10 + "\t" + N1 + "\t" + key(K1) + "\n");
        assertEquals(sb.toString(), out.toString());
    }

    /**
     * If there is a stray file in the index, then it is ignored.
     * 
     * @throws Exception
     */
    @Test
    public void skipFiles() throws Exception {
        String V = "index";
        String V0 = "onsite";
        String V00 = "01";
        String F0 = "strayfile";
        String R = "request";
        TestData I = TestData.INDEX_MAP;
        TestData K0 = TestData.KEY_CONTENT_MAP;
        String N0 = "00.dat";

        FileSystem fs = new FakeFileSystem();
        fs.mkdir(V);
        fs.mkdir(V + "/" + V0);
        fs.save(V + "/" + V0 + "/" + V00, raw(I));
        fs.save(V + "/" + F0, new byte[0]);
        fs.save(R, raw(key(K0) + "\n"));
        CapturePrintStream out = CapturePrintStream.create();

        IndexStore index = new IndexStoreFileSystem(fs, V);
        IndexSearch indexSearch = new IndexSearch(index);
        List<String> args = Arrays.asList(R);
        new IndexSearchMain(fs, out, indexSearch, args).run();

        StringBuilder sb = new StringBuilder();
        sb.append(V0 + "\t" + V00 + "\t" + N0 + "\t" + key(K0) + "\n");
        assertEquals(sb.toString(), out.toString());
    }

    private static String key(TestData item) {
        return item.getLocatorKey().toString();
    }

    private static byte[] raw(TestData item) throws IOException {
        return item.getBytes();
    }

    private static byte[] raw(String item) throws IOException {
        return ByteString.toUtf8(item);
    }
}
