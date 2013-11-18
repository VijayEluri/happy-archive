package org.yi.happy.archive.index;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;
import org.yi.happy.archive.Base16;
import org.yi.happy.archive.CapturePrintStream;
import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.crypto.DigestFactory;
import org.yi.happy.archive.crypto.DigestProvider;
import org.yi.happy.archive.crypto.Digests;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link IndexWriter}.
 */
public class IndexWriterTest {

    /**
     * simple test.
     * 
     * @throws IOException
     */
    @Test
    public void test() throws IOException {
        CapturePrintStream out = CapturePrintStream.create();
        IndexWriter index = new IndexWriter(out);

        index.write("00.dat", "plain", "test:00", "00", "1");

        assertEquals("00.dat\tplain\ttest:00\t00\t1\n", out.toString());
    }

    /**
     * @throws IOException
     * 
     */
    @Test
    public void testBlock() throws IOException {
        CapturePrintStream out = CapturePrintStream.create();
        IndexWriter index = new IndexWriter(out);

        EncodedBlock block = TestData.KEY_CONTENT.getEncodedBlock();
        index.write("00.dat", "plain", block);

        StringBuilder want = new StringBuilder();
        want.append("00.dat").append("\t");
        want.append("plain").append("\t");
        want.append(block.getKey()).append("\t");
        want.append(hash(block.asBytes())).append("\t");
        want.append(block.asBytes().length).append("\n");
        assertEquals(want.toString(), out.toString());
    }

    private static String hash(byte[] bytes) {
        DigestProvider digest = DigestFactory.getProvider("sha-256");
        bytes = Digests.digestData(digest, bytes);
        return Base16.encode(bytes);
    }
}
