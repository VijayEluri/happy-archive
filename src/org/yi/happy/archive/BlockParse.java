package org.yi.happy.archive;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.util.Arrays;

import org.yi.happy.annotate.ShouldThrowChecked;

public class BlockParse {

    /**
     * load a block into memory
     * 
     * @param in
     *            the stream to load
     * @return the loaded block
     * @throws LoadException
     *             on load errors
     */
    public static Block load(InputStream in) throws LoadException {
	try {
	    if (!(in instanceof BufferedInputStream)) {
		in = new BufferedInputStream(in);
	    }

	    BlockImpl block = new BlockImpl();

	    BinaryStreamParser p = new BinaryStreamParser();

	    int offset = 0;
	    // read headers
	    while (true) {
		// read: ( key ": " | endl )
		byte[] key = p.readTo(in, SEPERATOR, ENDL);
		offset += key.length;
		if (p.atEnd()) {
		    throw new ParseException("unexpected end", offset);
		}
		if (Arrays.equals(p.getTerminal(), ENDL)) {
		    if (key.length == 0) {
			break;
		    }
		    throw new ParseException("unexpected end of line", offset);
		}
		offset += p.getTerminal().length;

		// read: value endl
		byte[] value = p.readTo(in, ENDL);
		offset += value.length;
		if (p.atEnd()) {
		    throw new ParseException("unexpected end", offset);
		}
		offset += p.getTerminal().length;

		String key0 = ByteString.toString(key);
		String value0 = ByteString.toString(value);
		block.addMeta(key0, value0);
	    }

	    /*
	     * we are now at the end of the headers and the next byte to be read
	     * is the start of the binary data area, read up to the size of the
	     * size header, if I read 2MiB of data throw an exception
	     */
	    int limit = MAX_DATA_SIZE;

	    // if there is a size header limit the data size to that
	    if (block.getMeta().get(SIZE) != null) {
		int size = Integer.parseInt(block.getMeta().get(SIZE));
		if (limit > size) {
		    limit = size;
		}
	    }

	    byte[] data = new byte[limit];
	    int n = 0;
	    while (true) {
		int r = in.read(data, n, data.length - n);
		if (r < 1) {
		    break;
		}
		n += r;
	    }
	    if (n == MAX_DATA_SIZE) {
		throw new ParseException("block too long", offset + n);
	    }

	    if (data.length != n) {
		byte[] tmp = new byte[n];
		System.arraycopy(data, 0, tmp, 0, n);
		data = tmp;
	    }
	    block.setBody(data);

	    return block;
	} catch (IOException e) {
	    throw new LoadException(e);
	} catch (ParseException e) {
	    throw new LoadException(e);
	}
    }

    private static final byte[] SEPERATOR = ": ".getBytes();

    private static final byte[] ENDL = "\r\n".getBytes();

    private static final int MAX_DATA_SIZE = 2 * 1024 * 1024;

    public static final String SIZE = "size";

    /**
     * parse a block from a byte array
     * 
     * @param data
     *            the byte array
     * @return the block
     * @throws LoadException
     */
    public static Block load(byte[] data) throws LoadException {
	try {
	    InputStream in = new ByteArrayInputStream(data);
	    try {
		return load(in);
	    } finally {
		in.close();
	    }
	} catch (IOException e) {
	    throw new LoadException(e);
	}
    }

    /**
     * load a block into memory
     * 
     * @param resource
     *            the url to load
     * @return the loaded block
     * @throws LoadException
     */
    @ShouldThrowChecked
    public static Block load(URL resource) throws LoadException {
	try {
	    InputStream in = resource.openStream();
	    try {
		return load(in);
	    } finally {
		in.close();
	    }
	} catch (IOException e) {
	    throw new LoadException(e);
	}
    }

    public static Block load(File file) {
	try {
	    FileInputStream in = new FileInputStream(file);
	    try {
		return load(in);
	    } finally {
		in.close();
	    }
	} catch (IOException e) {
	    throw new LoadException(e);
	}
    }

}
