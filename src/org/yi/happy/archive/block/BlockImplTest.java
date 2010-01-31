package org.yi.happy.archive.block;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;
import org.yi.happy.archive.ByteString;

/**
 * tests for Block
 */
public class BlockImplTest {
    /**
     * check that headers are case sensitive
     */
    @Test
    public void testBlockHeader() {
	BlockImpl block = new BlockImpl();
	block.addMeta("aA", "b");

	assertEquals("b", block.getMeta().get("aA"));
	assertEquals(null, block.getMeta().get("Aa"));
    }

    /**
     * check that a block converts to bytes
     */
    @Test
    public void testAsBytes() {
	BlockImpl block = createSampleBlock();

	byte[] have = block.asBytes();

	byte[] want = createSampleBytes();
	assertArrayEquals(want, have);
    }

    /**
     * create the sample block
     * 
     * @return the sample block
     */
    public static BlockImpl createSampleBlock() {
	BlockImpl block = new BlockImpl();

	block.addMeta("a", "c");
	block.addMeta("b", "d");
	block.addMeta("c", "e");
	block.setBody(ByteString.toBytes("body\ndata\n"));

	return block;
    }

    /**
     * create the bytes for the sample block
     * 
     * @return the bytes for the sample block
     */
    public static byte[] createSampleBytes() {
	return ByteString.toBytes("a: c\r\nb: d\r\nc: e\r\n\r\nbody\ndata\n");
    }

    /**
     * check that the block can write out to a stream
     * 
     * @throws IOException
     */
    @Test
    public void testWriteTo() throws IOException {
	ByteArrayOutputStream s = new ByteArrayOutputStream();
	BlockImpl block = createSampleBlock();

	block.writeTo(s);
	s.close();

	byte[] have = s.toByteArray();
	byte[] want = createSampleBytes();
	assertArrayEquals(want, have);
    }
}
