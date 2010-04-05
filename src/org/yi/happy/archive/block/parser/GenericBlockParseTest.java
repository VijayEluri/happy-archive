package org.yi.happy.archive.block.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Test;
import org.yi.happy.archive.ByteString;
import org.yi.happy.archive.Bytes;
import org.yi.happy.archive.block.GenericBlock;
import org.yi.happy.archive.block.GenericBlockTest;

/**
 * Tests for {@link GenericBlockParse}.
 */
public class GenericBlockParseTest {
    /**
     * parse the sample block.
     */
    @Test
    public void testSampleBlock() {
        GenericBlockParse p = new GenericBlockParse();

        GenericBlock have = p.parse(GenericBlockTest.createSampleBytes());

        assertEquals(GenericBlockTest.createSampleBlock(), have);
    }

    /**
     * parse a block with mangled newlines.
     */
    @Test
    public void testMangledBlock() {
        GenericBlockParse p = new GenericBlockParse();

        GenericBlock have = p.parse(ByteString.toBytes("a\nb: c\r\rbody\n"));

        assertArrayEquals(ByteString.toBytes("a: \r\nb: c\r\n\r\nbody\n"), have
                .asBytes());
    }

    /**
     * parse a zero byte array.
     */
    @Test
    public void testEmptyBlock() {
        GenericBlockParse p = new GenericBlockParse();

        GenericBlock have = p.parse(new byte[0]);

        assertEquals(Collections.emptyMap(), have.getMeta());
        assertEquals(new Bytes(), have.getBody());
    }
}
