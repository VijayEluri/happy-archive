package org.yi.happy.archive;

import java.io.IOException;

import org.yi.happy.annotate.EntryPoint;
import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.block.parser.EncodedBlockParse;
import org.yi.happy.archive.commandLine.Env;
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
     * @param env
     * @throws IOException
     */
    @EntryPoint
    public static void main(Env env) throws IOException {
        RealFileSystem fs = new RealFileSystem();
        new FileStoreBlockPutMain(fs).run(env);
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
     * @param env
     *            the store, the block.
     * @throws IOException
     */
    public void run(Env env) throws IOException {
        if (env.hasNoStore() || env.hasArgumentCount() != 1) {
            System.err.println("use: --store store block");
            return;
        }

        FileBlockStore store = new FileBlockStore(fs, env.getStore());

        EncodedBlock b = EncodedBlockParse.parse(fs.load(env.getArgument(0),
                Blocks.MAX_SIZE));

        store.put(b);
    }
}
