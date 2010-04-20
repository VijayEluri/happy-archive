package org.yi.happy.archive;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;
import org.yi.happy.annotate.NeedFailureTest;
import org.yi.happy.archive.crypto.DigestFactory;
import org.yi.happy.archive.crypto.Digests;
import org.yi.happy.archive.file_system.FakeFileSystem;
import org.yi.happy.archive.file_system.FileSystem;
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
        FileSystem fs = new FakeFileSystem();

        fs.mkdir("image");
        fs.save("image/00.dat", TestData.KEY_CONTENT.getBytes());
        fs.save("image/01.dat", TestData.KEY_CONTENT_1.getBytes());

        StringWriter out = new StringWriter();

        new IndexVolumeMain(fs, out).run("image");

        StringBuilder sb = new StringBuilder();
        sb.append("00.dat\tplain\t");
        sb.append(TestData.KEY_CONTENT.getLocatorKey());
        sb.append("\t");
        sb.append(Base16.encode(Digests.digestData(DigestFactory
                .getProvider("sha-256"), TestData.KEY_CONTENT.getBytes())));
        sb.append("\t");
        sb.append(Integer.toString(TestData.KEY_CONTENT.getBytes().length));
        sb.append("\n");

        sb.append("01.dat\tplain\t");
        sb.append(TestData.KEY_CONTENT_1.getLocatorKey());
        sb.append("\t");
        sb.append(Base16.encode(Digests.digestData(DigestFactory
                .getProvider("sha-256"), TestData.KEY_CONTENT_1.getBytes())));
        sb.append("\t");
        sb.append(Integer.toString(TestData.KEY_CONTENT_1.getBytes().length));
        sb.append("\n");

        assertEquals(sb.toString(), out.toString());
    }
}
