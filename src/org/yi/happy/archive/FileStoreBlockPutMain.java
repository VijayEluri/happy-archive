package org.yi.happy.archive;

import java.io.IOException;

import org.yi.happy.annotate.EntryPoint;
import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.block.parser.EncodedBlockParse;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.file_system.RealFileSystem;

/**
 * put a block in a file store. Both the file store base directory and the block
 * as a file are named on the command line.
 */
public class FileStoreBlockPutMain {
    private final FileSystem fs;

    /**
     * store a block in a file store.
     * 
     * @param args
     * @throws IOException
     */
    @EntryPoint
    public static void main(String[] args) throws IOException {
        RealFileSystem fs = new RealFileSystem();
        new FileStoreBlockPutMain(fs).run(args);
    }

    /**
     * create.
     * 
     * @param fs
     *            the file system to use.
     */
    public FileStoreBlockPutMain(FileSystem fs) {
        this.fs = fs;
    }

    /**
     * store a block in the store.
     * 
     * @param args
     *            the store, the block.
     * @throws IOException
     */
    public void run(String... args) throws IOException {
        FileBlockStore store = new FileBlockStore(fs, args[0]);

        EncodedBlock b = EncodedBlockParse.parse(fs.load(args[1],
                Blocks.MAX_SIZE));

        store.put(b);
    }
}
