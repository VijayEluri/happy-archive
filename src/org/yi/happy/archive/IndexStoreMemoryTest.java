package org.yi.happy.archive;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link IndexStoreFileStore}.
 */
public class IndexStoreMemoryTest {
    /**
     * List the volume indexes. When the store is empty.
     * 
     * @throws Exception
     */
    @Test
    public void testListEmpty() throws Exception {
        String V0 = "onsite";

        IndexStoreMemory fake = new IndexStoreMemory();
        IndexStore index = fake;

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
        String V0 = "onsite";

        IndexStoreMemory fake = new IndexStoreMemory();
        fake.addVolumeSet(V0);
        IndexStore index = fake;

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
        String V0 = "offsite";
        String V00 = "01";
        String V1 = "onsite";
        String V10 = "02";
        TestData I = TestData.INDEX_MAP;

        IndexStoreMemory fake = new IndexStoreMemory();
        fake.addVolume(V0, V00, text(I));
        fake.addVolume(V1, V10, text(I));
        IndexStore index = fake;

        assertEquals(list(V0, V1), index.listVolumeSets());
        assertEquals(list(V00), index.listVolumeNames(V0));
        assertEquals(list(V10), index.listVolumeNames(V1));

        assertEquals(text(I), load(index, V0, V00));
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
