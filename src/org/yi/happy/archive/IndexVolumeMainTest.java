package org.yi.happy.archive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;

import org.junit.Test;
import org.yi.happy.annotate.NeedFailureTest;
import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.commandLine.EnvBuilder;
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

        Env env = new EnvBuilder().addArgument("image").create();
        new IndexVolumeMain(fs, out, null).run(env);

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

    /**
     * A sample run with a blank block.
     * 
     * @throws IOException
     */
    @Test
    public void test2() throws IOException {
        FileSystem fs = new FakeFileSystem();

        fs.mkdir("image");
        fs.save("image/00.dat", TestData.FILE_EMPTY.getBytes());
        fs.save("image/01.dat", TestData.KEY_CONTENT_1.getBytes());

        StringWriter out = new StringWriter();

        ByteArrayOutputStream err = new ByteArrayOutputStream();

        Env env = new EnvBuilder().addArgument("image").create();
        new IndexVolumeMain(fs, out, new PrintStream(err)).run(env);

        StringBuilder sb = new StringBuilder();
        sb.append("01.dat\tplain\t");
        sb.append(TestData.KEY_CONTENT_1.getLocatorKey());
        sb.append("\t");
        sb.append(Base16.encode(Digests.digestData(DigestFactory
                .getProvider("sha-256"), TestData.KEY_CONTENT_1.getBytes())));
        sb.append("\t");
        sb.append(Integer.toString(TestData.KEY_CONTENT_1.getBytes().length));
        sb.append("\n");

        assertEquals(sb.toString(), out.toString());

        assertTrue(err.size() != 0);
    }
}
