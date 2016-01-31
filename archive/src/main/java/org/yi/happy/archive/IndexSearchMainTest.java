package org.yi.happy.archive;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.yi.happy.archive.index.IndexSearch;
import org.yi.happy.archive.index.IndexStoreMemory;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link IndexSearchMain}.
 */
public class IndexSearchMainTest {
    private IndexStoreMemory indexStore;
    private IndexSearch indexSearch;
    private CapturePrintStream out;

    /**
     * set up.
     */
    @Before
    public void before() {
        indexStore = new IndexStoreMemory();
        indexSearch = new IndexSearch(indexStore);

        out = CapturePrintStream.create();
    }

    /**
     * tear down
     */
    @After
    public void after() {
        indexStore = null;
        indexSearch = null;

        out = null;
    }

    /**
     * search for one key with one index.
     * 
     * @throws Exception
     */
    @Test
    public void testOneIndex() throws Exception {
        String V0 = "onsite";
        String V00 = "01";
        TestData I = TestData.INDEX_MAP;
        TestData K0 = TestData.KEY_CONTENT_MAP;
        String N0 = "00.dat";

        indexStore.addVolume(V0, V00, text(I));

        InputStream in = input(key(K0) + "\n");

        InputStream oldIn = System.in;
        System.setIn(in);
        try {
            new IndexSearchMain(out, null, indexSearch).run();
        } finally {
            System.setIn(oldIn);
        }

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

    private static InputStream input(String text) {
        byte[] bytes = ByteString.toUtf8(text);
        return new ByteArrayInputStream(bytes);
    }

    private String text(TestData item) throws IOException {
        return ByteString.fromUtf8(raw(item));
    }
}
