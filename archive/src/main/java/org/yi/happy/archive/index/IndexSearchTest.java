package org.yi.happy.archive.index;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.yi.happy.archive.ByteString;
import org.yi.happy.archive.key.LocatorKey;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link IndexSearch}.
 */
public class IndexSearchTest {
    private IndexStoreMemory indexes;
    private Set<IndexSearchResult> results;
    private IndexSearch.Handler capture;
    private IndexSearch indexSearch;

    /**
     * set up.
     */
    @Before
    public void before() {
        indexes = new IndexStoreMemory();
        results = new HashSet<IndexSearchResult>();
        capture = new IndexSearch.Handler() {
            @Override
            public void gotResult(String volumeSet, String volumeName,
                    IndexEntry result) {
                results.add(new IndexSearchResult(volumeSet, volumeName, result
                        .getName(), result.getKey()));
            }

            @Override
            public void gotException(String volumeSet, String volumeName,
                    Throwable cause) {
                fail();
            }
        };
        indexSearch = new IndexSearch(indexes);
    }

    /**
     * tear down.
     */
    @After
    public void after() {
        indexSearch = null;
        capture = null;
        results = null;
        indexes = null;
    }

    /**
     * When everything is blank there are no results.
     * 
     * @throws Exception
     */
    @Test
    public void testBlank() throws Exception {
        Set<LocatorKey> keys = new HashSet<LocatorKey>();
        indexSearch.search(keys, capture);
        
        Set<IndexSearchResult> want;
        want = new HashSet<IndexSearchResult>();
        assertEquals(want, results);
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

        indexes.addVolume(V0, V00, text(I));

        Set<LocatorKey> keys = new HashSet<LocatorKey>();
        keys.add(key(K0));

        indexSearch.search(keys, capture);

        Set<IndexSearchResult> want = new HashSet<IndexSearchResult>();
        want.add(new IndexSearchResult(V0, V00, N0, key(K0)));
        assertEquals(want, results);
    }

    /**
     * search for one key with two indexes.
     * 
     * @throws Exception
     */
    @Test
    public void testTwoIndex() throws Exception {
        String V0 = "offsite";
        String V00 = "02";
        String V1 = "onsite";
        String V10 = "01";
        TestData I = TestData.INDEX_MAP;
        TestData K0 = TestData.KEY_CONTENT_MAP;
        String N0 = "00.dat";

        indexes.addVolume(V0, V00, text(I));
        indexes.addVolume(V1, V10, text(I));

        Set<LocatorKey> keys = new HashSet<LocatorKey>();
        keys.add(key(K0));

        indexSearch.search(keys, capture);

        Set<IndexSearchResult> want = new HashSet<IndexSearchResult>();
        want.add(new IndexSearchResult(V0, V00, N0, key(K0)));
        want.add(new IndexSearchResult(V1, V10, N0, key(K0)));
        assertEquals(want, results);
    }

    /**
     * search for two keys with two indexes.
     * 
     * @throws Exception
     */
    @Test
    public void testMultipleKeys() throws Exception {
        String V0 = "offsite";
        String V00 = "02";
        String V1 = "onsite";
        String V10 = "01";
        TestData I = TestData.INDEX_MAP;
        TestData K0 = TestData.KEY_CONTENT_MAP;
        String N0 = "00.dat";
        TestData K1 = TestData.KEY_CONTENT_1;
        String N1 = "01.dat";

        indexes.addVolume(V0, V00, text(I));
        indexes.addVolume(V1, V10, text(I));

        Set<LocatorKey> keys = new HashSet<LocatorKey>();
        keys.add(key(K0));
        keys.add(key(K1));

        indexSearch.search(keys, capture);

        Set<IndexSearchResult> want = new HashSet<IndexSearchResult>();
        want.add(new IndexSearchResult(V0, V00, N0, key(K0)));
        want.add(new IndexSearchResult(V0, V00, N1, key(K1)));
        want.add(new IndexSearchResult(V1, V10, N0, key(K0)));
        want.add(new IndexSearchResult(V1, V10, N1, key(K1)));
        assertEquals(want, results);
    }

    private static LocatorKey key(TestData item) {
        return item.getLocatorKey();
    }

    private static byte[] raw(TestData item) throws IOException {
        return item.getBytes();
    }

    private String text(TestData item) throws IOException {
        return ByteString.fromUtf8(raw(item));
    }
}
