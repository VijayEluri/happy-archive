package org.yi.happy.archive;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * parser for encoded blocks.
 */
public class EncodedBlockParse {
    /**
     * parse an encoded block from an input stream.
     * 
     * @param in
     *            the input stream.
     * @return the encoded block.
     */
    public EncodedBlock load(InputStream in) {
        Block block = BlockParse.load(in);
        return EncodedBlockFactory.parse(block);
    }

    /**
     * parse an encoded block from a file.
     * 
     * @param file
     *            the file to parse.
     * @return the encoded block.
     */
    public EncodedBlock load(File file) {
        Block block = BlockParse.load(file);
        return EncodedBlockFactory.parse(block);
    }

    public EncodedBlock parse(byte[] data) {
        Block block = BlockParse.load(data);
        return EncodedBlockFactory.parse(block);
    }

    public EncodedBlock load(URL url) throws IOException {
        InputStream in = url.openStream();
        try {
            return load(in);
        } finally {
            in.close();
        }
    }

}
