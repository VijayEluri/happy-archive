package org.yi.happy.archive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;
import org.yi.happy.archive.block.encoder.BlockEncoder;
import org.yi.happy.archive.block.encoder.BlockEncoderFactory;
import org.yi.happy.archive.test_data.TestData;

/**
 * tests for {@link KeyOutputStream}.
 */
public class KeyOutputStreamTest {
    /**
     * write data
     * 
     * @throws IOException
     */
    @Test
    public void testWrite() throws IOException {
        BlockEncoder e = BlockEncoderFactory.getContentDefault();
        StorageMemory s = new StorageMemory();
        KeyOutputStream out = new KeyOutputStream(new StoreBlockStorage(e, s));

        byte[] data = "0123401234".getBytes();
        out.write(data);
        out.write(data, 0, 2);
        out.write(data, 2, 3);
        out.write(data, 5, 5);
        out.write(data, 0, 10);
        out.write(data);
        out.close();

        TestData d = TestData.KEY_CONTENT_40;
        assertEquals(d.getFullKey(), out.getFullKey());
        assertTrue(s.contains(d.getLocatorKey()));
    }

    /**
     * write data
     * 
     * @throws IOException
     */
    @Test
    public void testWrite2() throws IOException {
        BlockEncoder e = BlockEncoderFactory.getContentDefault();
        StorageMemory s = new StorageMemory();
        KeyOutputStream out = new KeyOutputStream(new StoreBlockStorage(e, s));

        out.write("hello\n".getBytes());
        out.close();

        TestData d = TestData.KEY_CONTENT;
        assertEquals(d.getFullKey(), out.getFullKey());
        assertTrue(s.contains(d.getLocatorKey()));
    }

    /**
     * check that write after close does not work
     * 
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testWriteAfterClose() throws IOException {
        BlockEncoder e = BlockEncoderFactory.getContentDefault();
        StorageMemory s = new StorageMemory();
        KeyOutputStream out = new KeyOutputStream(new StoreBlockStorage(e, s));

        out.close();
        out.write("hi".getBytes());
    }

    /**
     * check that calling close again does not change the full key.
     * 
     * @throws IOException
     *             on error
     */
    @Test
    public void testCloseAgain() throws IOException {
        BlockEncoder e = BlockEncoderFactory.getContentDefault();
        StorageMemory s = new StorageMemory();
        KeyOutputStream out = new KeyOutputStream(new StoreBlockStorage(e, s));

        out.write("hello\n".getBytes());
        out.close();

        out.close();

        assertEquals(TestData.KEY_CONTENT.getFullKey(), out.getFullKey());
    }

    /**
     * make the split size small and write a split block.
     * 
     * @throws IOException
     */
    @Test
    public void testSetSplitSize() throws IOException {
        BlockEncoder e = BlockEncoderFactory.getContentDefault();
        StorageMemory s = new StorageMemory();
        KeyOutputStream out = new KeyOutputStream(new StoreBlockStorage(e, s));

        out.setSplitSize(512);
        byte[] data = new byte[2048];
        Arrays.fill(data, (byte) 'a');
        out.write(data);
        out.close();

        TestData d = TestData.KEY_CONTENT_MAP_2048A;
        assertEquals(d.getFullKey(), out.getFullKey());
        assertTrue(s.contains(d.getLocatorKey()));

        assertTrue(s.contains(TestData.KEY_CONTENT_512A.getLocatorKey()));
    }

}
