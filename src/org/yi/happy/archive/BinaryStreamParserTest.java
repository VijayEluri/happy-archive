package org.yi.happy.archive;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

/**
 * check that binary stream parser works.
 */
public class BinaryStreamParserTest {
    private static final String BLOCK = "a: 1\r\nb: 2\r\n\r\nstuff";

    private static final String SEPERATOR = ": ";

    private static final String ENDL = "\r\n";

    private static final byte[] EMPTY = new byte[0];

    /**
     * read the header lines
     * 
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
	ByteArrayInputStream in = new ByteArrayInputStream(BLOCK.getBytes());
	try {
	    BinaryStreamParser p = new BinaryStreamParser();

	    byte[] name = p.readTo(in, ENDL.getBytes());
	    byte[] term = p.getTerminal();

	    assertArrayEquals("a: 1".getBytes(), name);
	    assertArrayEquals(ENDL.getBytes(), term);
	    assertFalse(p.atEnd());

	    name = p.readTo(in, ENDL.getBytes());
	    term = p.getTerminal();

	    assertSame(name, p.getFound());
	    assertArrayEquals("b: 2".getBytes(), name);
	    assertArrayEquals(ENDL.getBytes(), term);
	    assertFalse(p.atEnd());

	    name = p.readTo(in, ENDL.getBytes());
	    term = p.getTerminal();

	    assertSame(name, p.getFound());
	    assertArrayEquals(EMPTY, name);
	    assertArrayEquals(ENDL.getBytes(), term);
	    assertFalse(p.atEnd());
	} finally {
	    in.close();
	}
    }

    /**
     * parse an entire header, check that double terminal matching works.
     * 
     * @throws IOException
     */
    @Test
    public void test2() throws IOException {
	ByteArrayInputStream in = new ByteArrayInputStream(BLOCK.getBytes());
	try {
	    BinaryStreamParser p = new BinaryStreamParser();

	    byte[] name = p.readTo(in, ENDL.getBytes(), SEPERATOR.getBytes());
	    byte[] term = p.getTerminal();

	    assertArrayEquals(name, "a".getBytes());
	    assertArrayEquals(term, SEPERATOR.getBytes());
	    assertFalse(p.atEnd());

	    name = p.readTo(in, ENDL.getBytes(), SEPERATOR.getBytes());
	    term = p.getTerminal();

	    assertArrayEquals(name, "1".getBytes());
	    assertArrayEquals(term, ENDL.getBytes());
	    assertFalse(p.atEnd());
	} finally {
	    in.close();
	}
    }

}
