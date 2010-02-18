package org.yi.happy.archive.block.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.yi.happy.archive.Blocks;
import org.yi.happy.archive.Streams;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.key.KeyType;

/**
 * parser for encoded blocks.
 */
public class EncodedBlockParse {
    private EncodedBlockParse() {

    }

    /**
     * load an encoded block from a URL.
     * 
     * @param url
     *            the url to load from.
     * @return the encoded block.
     * @throws IOException
     *             on IO errors.
     * @throws IllegalArgumentException
     *             on parsing errors.
     */
    public static EncodedBlock load(URL url) throws IOException {
	InputStream in = url.openStream();
	try {
	    byte[] data = Streams.load(in, Blocks.MAX_SIZE);
	    return parse(data);
	} finally {
	    in.close();
	}
    }

    /**
     * parse an encoded block from bytes.
     * 
     * @param data
     *            the block as bytes.
     * @return the encoded block.
     */
    public static EncodedBlock parse(byte[] data) {
	Block block = new GenericBlockParse().parse(data);
	return parse(block);
    }

    /**
     * parse an encoded block from a parsed block.
     * 
     * @param block
     *            the parsed block.
     * @return the encoded block.
     */
    public static EncodedBlock parse(Block block) {
	if (block instanceof EncodedBlock) {
	    return (EncodedBlock) block;
	}

	String keyType = block.getMeta().get(EncodedBlock.KEY_TYPE_META);
	if (keyType == null) {
	    throw new IllegalArgumentException("missing key-type");
	}

	if (keyType.equals(KeyType.CONTENT_HASH)) {
	    return ContentEncodedBlockParse.parse(block);
	}

	if (keyType.equals(KeyType.BLOB)) {
	    return BlobEncodedBlockParse.parse(block);
	}

	if (keyType.equals(KeyType.NAME_HASH)) {
	    return NameEncodedBlockParse.parse(block);
	}

	throw new UnknownKeyTypeException();
    }
}
