package org.yi.happy.archive.block.parser;

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

    public static EncodedBlock parse(byte[] data) {
	Block block = BlockParse.parse(data);
	return EncodedBlockFactory.parse(block);
    }

    public static EncodedBlock load(URL url) throws IOException {
	InputStream in = url.openStream();
	try {
	    Block block = BlockParse.load(in);
	    return EncodedBlockFactory.parse(block);
	} finally {
	    in.close();
	}
    }
}
