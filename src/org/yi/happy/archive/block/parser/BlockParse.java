package org.yi.happy.archive.block.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.yi.happy.archive.Blocks;
import org.yi.happy.archive.Streams;
import org.yi.happy.archive.block.Block;

/**
 * Generic block parsing.
 */
public class BlockParse {
    private BlockParse() {

    }

    /**
     * parse a block from a byte array
     * 
     * @param bytes
     *            the byte array
     * @return the block
     */
    public static Block parse(byte[] bytes) {
        return new GenericBlockParse().parse(bytes);
    }

    /**
     * load a block into memory
     * 
     * @param in
     *            the stream to load
     * @return the loaded block
     * @throws IOException
     *             on IO errors.
     * @throws IllegalArgumentException
     *             on parsing errors.
     */
    public static Block load(InputStream in) throws IOException,
            IllegalArgumentException {
        byte[] bytes = Streams.load(in, Blocks.MAX_SIZE);
        return parse(bytes);
    }

    /**
     * load a block into memory
     * 
     * @param resource
     *            the url to load
     * @return the loaded block
     * @throws IOException
     *             on IO errors
     */
    public static Block load(URL resource) throws IOException {
        InputStream in = resource.openStream();
        try {
            return load(in);
        } finally {
            in.close();
        }
    }

    /**
     * load a block into memory.
     * 
     * @param file
     *            the file to load.
     * @return the loaded block.
     * @throws IOException
     *             on IO errors.
     */
    public static Block load(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        try {
            return load(in);
        } finally {
            in.close();
        }
    }
}
