package org.yi.happy.archive.block.parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.EncodedBlock;

/**
 * parser for encoded blocks.
 */
public class EncodedBlockParse {
    private EncodedBlockParse() {

    }

    /**
     * parse an encoded block from an input stream.
     * 
     * @param in
     *            the input stream.
     * @return the encoded block.
     * @throws IOException
     */
    public static EncodedBlock load(InputStream in) throws IOException {
	Block block = BlockParse.load(in);
	return EncodedBlockFactory.parse(block);
    }

    /**
     * parse an encoded block from a file.
     * 
     * @param file
     *            the file to parse.
     * @return the encoded block.
     * @throws IOException
     */
    public static EncodedBlock load(File file) throws IOException {
	Block block = BlockParse.load(file);
	return EncodedBlockFactory.parse(block);
    }

    public static EncodedBlock parse(byte[] data) {
	Block block = BlockParse.parse(data);
	return EncodedBlockFactory.parse(block);
    }

    public static EncodedBlock load(URL url) throws IOException {
	InputStream in = url.openStream();
	try {
	    return load(in);
	} finally {
	    in.close();
	}
    }

}
