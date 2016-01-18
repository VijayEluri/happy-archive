package org.yi.happy.archive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.yi.happy.archive.block.encoder.BlockEncoder;
import org.yi.happy.archive.block.encoder.BlockEncoderFactory;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.LocatorKey;
import org.yi.happy.archive.test_data.TestData;

/**
 * tests for {@link KeyOutputStream}.
 */
public class KeyOutputStreamTest {
    private BlockStoreMemory blocks;
    private ClearBlockTargetStore target;
    private BlockEncoder blockEncoder;

    /**
     * set up.
     */
    @Before
    public void before() {
        blocks = new BlockStoreMemory();
        blockEncoder = BlockEncoderFactory.getContentDefault();
        target = new ClearBlockTargetStore(blockEncoder, blocks);
    }

    /**
     * change the set up.
     */
    private void withOldDefaultBlockEncoder() {
        blockEncoder = BlockEncoderFactory.getContentOldDefault();
        target = new ClearBlockTargetStore(blockEncoder, blocks);
    }

    /**
     * tear down.
     */
    @After
    public void after() {
        blocks = null;
        blockEncoder = null;
        target = null;
    }

    /**
     * write data
     * 
     * @throws IOException
     */
    @Test
    public void testWrite() throws IOException {
        withOldDefaultBlockEncoder();
        KeyOutputStream out = new KeyOutputStream(target);

        TestData C = TestData.KEY_CONTENT_40;

        byte[] data = "0123401234".getBytes();
        out.write(data);
        out.write(data, 0, 2);
        out.write(data, 2, 3);
        out.write(data[5]);
        out.write(data, 6, 4);
        out.write(data, 0, 10);
        out.write(data);
        out.close();

        assertEquals(key(C), out.getFullKey());
        assertTrue(blocks.contains(lkey(C)));
    }

    /**
     * write data
     * 
     * @throws IOException
     */
    @Test
    public void testWrite2() throws IOException {
        KeyOutputStream out = new KeyOutputStream(target);

        TestData F = TestData.FILE_CONTENT;
        TestData C = TestData.KEY_CONTENT_AES128;

        out.write(raw(F));
        out.close();

        assertEquals(key(C), out.getFullKey());
        assertTrue(blocks.contains(lkey(C)));
    }

    /**
     * check that write after close does not work
     * 
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testWriteAfterClose() throws IOException {
        KeyOutputStream out = new KeyOutputStream(target);

        TestData F = TestData.FILE_CONTENT;
        byte[] data = raw(F);

        out.close();
        out.write(data);
    }

    /**
     * check that calling close again does not change the full key.
     * 
     * @throws IOException
     *             on error
     */
    @Test
    public void testCloseAgain() throws IOException {
        KeyOutputStream out = new KeyOutputStream(target);

        TestData F = TestData.FILE_CONTENT;
        TestData C = TestData.KEY_CONTENT_AES128;

        out.write(raw(F));
        out.close();

        out.close();

        assertEquals(key(C), out.getFullKey());
    }

    /**
     * make the split size small and write a split block.
     * 
     * @throws IOException
     */
    @Test
    public void testSetSplitSize() throws IOException {
        withOldDefaultBlockEncoder();
        KeyOutputStream out = new KeyOutputStream(target);
        out.setSplitSize(512);

        out.write(repeat((byte) 'a', 2048));
        out.close();

        TestData CM = TestData.KEY_CONTENT_MAP_2048A;
        TestData C = TestData.KEY_CONTENT_512A;

        assertEquals(key(CM), out.getFullKey());
        assertTrue(blocks.contains(lkey(CM)));
        assertTrue(blocks.contains(lkey(C)));
    }

    private LocatorKey lkey(TestData item) {
        return item.getLocatorKey();
    }

    private FullKey key(TestData item) {
        return item.getFullKey();
    }

    private byte[] raw(TestData item) throws IOException {
        return item.getBytes();
    }

    private byte[] repeat(byte b, int n) {
        byte[] data = new byte[n];
        Arrays.fill(data, b);
        return data;
    }
}
