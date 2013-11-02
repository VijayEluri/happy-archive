package org.yi.happy.archive.index;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.yi.happy.archive.ByteString;
import org.yi.happy.archive.FileStore;
import org.yi.happy.archive.FileStoreMemory;
import org.yi.happy.archive.Streams;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link IndexStoreFileStore}.
 */
public class IndexStoreFileStoreTest {
    /**
     * List the volume indexes. When the store is empty.
     * 
     * @throws Exception
     */
    @Test
    public void testListEmpty() throws Exception {
        String V = "index";
        String V0 = "onsite";

        FileStore fs = new FileStoreMemory();
        fs.putDir(V);

        IndexStore index = new IndexStoreFileStore(fs, V);

        assertEquals(list(), index.listVolumeSets());

        assertEquals(list(), index.listVolumeNames(V0));
    }

    /**
     * List the volume indexes. When the store has one index set, and the set is
     * empty.
     * 
     * @throws Exception
     */
    @Test
    public void testListEmptySet() throws Exception {
        String V = "index";
        String V0 = "onsite";

        FileStore fs = new FileStoreMemory();
        fs.putDir(V);
        fs.putDir(V + "/" + V0);

        IndexStore index = new IndexStoreFileStore(fs, V);

        assertEquals(list(V0), index.listVolumeSets());
        assertEquals(list(), index.listVolumeNames(V0));
    }

    /**
     * List the volume indexes. When the store has two index sets each with one
     * volume index.
     * 
     * @throws Exception
     */
    @Test
    public void testList() throws Exception {
        String V = "index";
        String V0 = "offsite";
        String V00 = "01";
        String V1 = "onsite";
        String V10 = "02";
        TestData I = TestData.INDEX_MAP;

        FileStore fs = new FileStoreMemory();
        fs.putDir(V);
        fs.putDir(V + "/" + V0);
        fs.put(V + "/" + V0 + "/" + V00, raw(I));
        fs.putDir(V + "/" + V1);
        fs.put(V + "/" + V1 + "/" + V10, raw(I));

        IndexStore index = new IndexStoreFileStore(fs, V);

        assertEquals(list(V0, V1), index.listVolumeSets());
        assertEquals(list(V00), index.listVolumeNames(V0));
        assertEquals(list(V10), index.listVolumeNames(V1));

        assertEquals(text(I), load(index, V0, V00));
    }

    /**
     * A compressed index should be listed without any extra extensions, and
     * should be loaded the same way as any other.
     * 
     * @throws Exception
     */
    @Test
    public void testCompress() throws Exception {
        String V = "index";
        String V0 = "onsite";
        String V00 = "01";
        String V01 = "03";
        String V01Z = "03.gz";
        TestData I = TestData.INDEX_MAP;
        TestData IZ = TestData.INDEX_MAP_GZ;

        FileStore fs = new FileStoreMemory();
        fs.putDir(V);
        fs.putDir(V + "/" + V0);
        fs.put(V + "/" + V0 + "/" + V00, raw(I));
        fs.putDir(V + "/" + V0);
        fs.put(V + "/" + V0 + "/" + V01Z, raw(IZ));

        IndexStore index = new IndexStoreFileStore(fs, V);

        assertEquals(list(V00, V01), index.listVolumeNames(V0));
        assertEquals(text(I), load(index, V0, V00));
        assertEquals(text(I), load(index, V0, V01));
    }

    /**
     * If there is a stray file in the index, then it is ignored.
     * 
     * @throws Exception
     */
    @Test
    public void testStrayFile() throws Exception {
        String V = "index";
        String V0 = "onsite";
        String V00 = "01";
        String V1 = "strayfile";
        TestData I = TestData.INDEX_MAP;

        FileStore fs = new FileStoreMemory();
        fs.putDir(V);
        fs.putDir(V + "/" + V0);
        fs.put(V + "/" + V0 + "/" + V00, raw(I));
        fs.put(V + "/" + V1, raw(I));

        IndexStore index = new IndexStoreFileStore(fs, V);

        assertEquals(list(V0), index.listVolumeSets());
        assertEquals(list(V00), index.listVolumeNames(V0));
    }

    private String load(IndexStore index, String volumeSet, String volumeName)
            throws IOException {
        Reader in = index.open(volumeSet, volumeName);
        try {
            return Streams.load(in);
        } finally {
            in.close();
        }
    }

    private String text(TestData item) throws IOException {
        return ByteString.fromUtf8(raw(item));
    }

    private byte[] raw(TestData item) throws IOException {
        return item.getBytes();
    }

    private static <T> List<T> list(T... items) {
        return Arrays.asList(items);
    }
}
