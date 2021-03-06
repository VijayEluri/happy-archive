package org.yi.happy.archive;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.junit.Test;
import org.yi.happy.archive.test_data.TestData;

/**
 * tests for {@link VolumeGetMain}.
 */
public class VolumeGetMainTest {
    /**
     * load a file from a volume.
     * 
     * @throws UnsupportedOperationException
     * @throws IOException
     */
    @Test
    public void test1() throws UnsupportedOperationException, IOException {
        FileStore files = new FileStoreMemory();
        files.putDir("/media");
        files.put("/media/00.dat", TestData.KEY_CONTENT_MAP.getBytes());
        InputStream in = new ByteArrayInputStream("00.dat\n".getBytes("UTF-8"));
        BlockStore blocks = new BlockStoreMemory();

        new VolumeGetMain(blocks, files, in, null, Arrays.asList("/media"))
                .run();

        assertEquals(TestData.KEY_CONTENT_MAP.getEncodedBlock(),
                blocks.get(TestData.KEY_CONTENT_MAP.getLocatorKey()));
    }
}
