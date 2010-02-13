package org.yi.happy.archive.block.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.yi.happy.annotate.MagicLiteral;
import org.yi.happy.archive.Blocks;
import org.yi.happy.archive.Streams;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.EncodedBlock;

/**
 * parser for encoded blocks.
 */
public class EncodedBlockParse {
    private EncodedBlockParse() {

    }

    public static EncodedBlock load(URL url) throws IOException {
	InputStream in = url.openStream();
	try {
	    byte[] data = Streams.load(in, Blocks.MAX_SIZE);
	    return parse(data);
	} finally {
	    in.close();
	}
    }

    public static EncodedBlock parse(byte[] data) {
	Block block = new GenericBlockParse().parse(data);
	return parse(block);
    }

    @MagicLiteral
    public static EncodedBlock parse(Block block) {
	if (block instanceof EncodedBlock) {
	    return (EncodedBlock) block;
	}

	String keyType = block.getMeta().get("key-type");
	if (keyType == null) {
	    throw new IllegalArgumentException("missing key-type");
	}

	if (keyType.equals("content-hash")) {
	    return ContentEncodedBlockParse.parse(block);
	}

	if (keyType.equals("blob")) {
	    return BlobEncodedBlockParse.parse(block);
	}

	if (keyType.equals("name-hash")) {
	    return NameEncodedBlockParse.parse(block);
	}

	throw new UnknownKeyTypeException();
    }
}
