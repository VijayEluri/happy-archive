package org.yi.happy.archive;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.yi.happy.annotate.NeedFailureTest;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link IndexSearchMain}.
 */
@NeedFailureTest
public class IndexSearchMainTest {
    private IndexStoreMemory indexStore;
    private IndexSearch indexSearch;
    private CapturePrintStream out;
    private CapturePrintStream err;

    /**
     * set up.
     */
    @Before
    public void before() {
        indexStore = new IndexStoreMemory();
        indexSearch = new IndexSearch(indexStore);

        out = CapturePrintStream.create();
        err = CapturePrintStream.create();
    }

    /**
     * tear down
     */
    @After
    public void after() {
        indexStore = null;
        indexSearch = null;

        out = null;
        err = null;
    }

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
        String V0 = "onsite";
        String V00 = "01";
        TestData I = TestData.INDEX_MAP;
        TestData K0 = TestData.KEY_CONTENT_MAP;
        String N0 = "00.dat";

        indexStore.addVolume(V0, V00, text(I));

        InputStream in = input(key(K0) + "\n");

        new IndexSearchMain(in, out, null, indexSearch).run();

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
        String V0 = "offsite";
        String V00 = "02";
        String V1 = "onsite";
        String V10 = "01";
        TestData I = TestData.INDEX_MAP;
        TestData K0 = TestData.KEY_CONTENT_MAP;
        String N0 = "00.dat";

        indexStore.addVolume(V0, V00, text(I));
        indexStore.addVolume(V1, V10, text(I));

        InputStream in = input(key(K0) + "\n");

        new IndexSearchMain(in, out, err, indexSearch).run();

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
        String V0 = "offsite";
        String V00 = "02";
        String V1 = "onsite";
        String V10 = "01";
        TestData I = TestData.INDEX_MAP;
        TestData K0 = TestData.KEY_CONTENT_MAP;
        String N0 = "00.dat";
        TestData K1 = TestData.KEY_CONTENT_1;
        String N1 = "01.dat";

        indexStore.addVolume(V0, V00, text(I));
        indexStore.addVolume(V1, V10, text(I));

        InputStream in = input(key(K0) + "\n" + key(K1) + "\n");

        new IndexSearchMain(in, out, err, indexSearch).run();

        StringBuilder sb = new StringBuilder();
        sb.append(V0 + "\t" + V00 + "\t" + N0 + "\t" + key(K0) + "\n");
        sb.append(V0 + "\t" + V00 + "\t" + N1 + "\t" + key(K1) + "\n");
        sb.append(V1 + "\t" + V10 + "\t" + N0 + "\t" + key(K0) + "\n");
        sb.append(V1 + "\t" + V10 + "\t" + N1 + "\t" + key(K1) + "\n");
        assertEquals(sb.toString(), out.toString());
    }

    private static String key(TestData item) {
        return item.getLocatorKey().toString();
    }

    private static byte[] raw(TestData item) throws IOException {
        return item.getBytes();
    }

    private static InputStream input(String text) {
        byte[] bytes = ByteString.toUtf8(text);
        return new ByteArrayInputStream(bytes);
    }

    private String text(TestData item) throws IOException {
        return ByteString.fromUtf8(raw(item));
    }
}
