package org.yi.happy.archive.block.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.yi.happy.annotate.Simplify;
import org.yi.happy.archive.Blocks;
import org.yi.happy.archive.Streams;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.GenericBlock;

@Simplify("loading all the data and parsing in memory is easier and faster, "
	+ "since stream parsing is not saving memory here")
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
    public static Block load(InputStream in) throws IOException {
	byte[] bytes = Streams.load(in, Blocks.MAX_SIZE);
	return parse(bytes);
    }

    /**
     * parse a block from a byte array
     * 
     * @param bytes
     *            the byte array
     * @return the block
     */
    public static Block parse(byte[] bytes) {
	GenericBlock block = new GenericBlockParse().parse(bytes);
	trim: try {
	    int size = Integer.parseInt(block.getMeta().get("size"));
	    if (size >= block.getBody().length) {
		break trim;
	    }
	    byte[] body = new byte[size];
	    System.arraycopy(block.getBody(), 0, body, 0, size);
	    block.setBody(body);
	} catch (IllegalArgumentException e) {
	    // keep what we already had
	}
	return block;
    }

    /**
     * load a block into memory
     * 
     * @param resource
     *            the url to load
     * @return the loaded block
     */
    public static Block load(URL resource) throws IOException {
	InputStream in = resource.openStream();
	try {
	    return load(in);
	} finally {
	    in.close();
	}
    }

    public static Block load(File file) throws IOException {
	FileInputStream in = new FileInputStream(file);
	try {
	    return load(in);
	} finally {
	    in.close();
	}
    }

}
