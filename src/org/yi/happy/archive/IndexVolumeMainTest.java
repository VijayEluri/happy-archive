package org.yi.happy.archive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.yi.happy.annotate.NeedFailureTest;
import org.yi.happy.archive.crypto.DigestFactory;
import org.yi.happy.archive.crypto.DigestProvider;
import org.yi.happy.archive.crypto.Digests;
import org.yi.happy.archive.file_system.FileStore;
import org.yi.happy.archive.file_system.FileStoreMemory;
import org.yi.happy.archive.key.LocatorKey;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link IndexVolumeMain}.
 */
@NeedFailureTest
public class IndexVolumeMainTest {
    /**
     * A sample good run.
     * 
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        TestData K0 = TestData.KEY_CONTENT;
        TestData K1 = TestData.KEY_CONTENT_1;
        String NB = "image";
        String N0 = "00.dat";
        String N1 = "01.dat";

        FileStore fs = new FileStoreMemory();

        fs.putDir(NB);
        fs.put(NB + "/" + N0, raw(K0));
        fs.put(NB + "/" + N1, raw(K1));

        CapturePrintStream out = CapturePrintStream.create();

        List<String> args = Arrays.asList(NB);
        new IndexVolumeMain(fs, out, null, args).run();

        StringBuilder sb = new StringBuilder();
        sb.append(N0).append("\t");
        sb.append("plain").append("\t");
        sb.append(key(K0)).append("\t");
        sb.append(hash(K0)).append("\t");
        sb.append(size(K0)).append("\n");

        sb.append(N1).append("\t");
        sb.append("plain").append("\t");
        sb.append(key(K1)).append("\t");
        sb.append(hash(K1)).append("\t");
        sb.append(size(K1)).append("\n");

        assertEquals(sb.toString(), out.toString());
    }

    /**
     * A sample run with a blank block.
     * 
     * @throws IOException
     */
    @Test
    public void test2() throws IOException {
        TestData K0 = TestData.FILE_EMPTY;
        TestData K1 = TestData.KEY_CONTENT_1;
        String NB = "image";
        String N0 = "00.dat";
        String N1 = "01.dat";

        FileStore fs = new FileStoreMemory();

        fs.putDir(NB);
        fs.put(NB + "/" + N0, raw(K0));
        fs.put(NB + "/" + N1, raw(K1));

        CapturePrintStream out = CapturePrintStream.create();
        CapturePrintStream err = CapturePrintStream.create();

        List<String> args = Arrays.asList(NB);
        new IndexVolumeMain(fs, out, err, args).run();

        StringBuilder sb = new StringBuilder();
        sb.append(N1).append("\t");
        sb.append("plain").append("\t");
        sb.append(key(K1)).append("\t");
        sb.append(hash(K1)).append("\t");
        sb.append(size(K1)).append("\n");

        assertEquals(sb.toString(), out.toString());

        assertTrue(err.size() != 0);
    }

    private static LocatorKey key(TestData item) {
        return item.getLocatorKey();
    }

    private static String hash(TestData item) throws IOException {
        DigestProvider sha256 = DigestFactory.getProvider("sha-256");
        return Base16.encode(Digests.digestData(sha256, raw(item)));
    }

    private static String size(TestData item) throws IOException {
        return Integer.toString(raw(item).length);
    }

    private static byte[] raw(TestData item) throws IOException {
        return item.getBytes();
    }
}
