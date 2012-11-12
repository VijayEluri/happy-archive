package org.yi.happy.archive;

import java.io.IOException;
import java.util.List;

import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.block.parser.EncodedBlockParse;
import org.yi.happy.archive.commandLine.UsesArgs;
import org.yi.happy.archive.commandLine.UsesStore;
import org.yi.happy.archive.file_system.FileSystem;

/**
 * put a block in the store.
 */
@UsesStore
@UsesArgs({ "block..." })
public class FileStoreBlockPutMain implements MainCommand {
    private final FileSystem fs;
    private final BlockStore store;
    private final List<String> args;

    /**
     * create.
     * 
     * @param store
     *            the block store to use.
     * @param fs
     *            the file system to use.
     */
    public FileStoreBlockPutMain(BlockStore store, FileSystem fs,
            List<String> args) {
        this.store = store;
        this.fs = fs;
        this.args = args;
    }

    /**
     * store a block in the store.
     * 
     * @param env
     *            the store, the block.
     * @throws IOException
     */
    @Override
    public void run() throws IOException {
        for (String arg : args) {
            EncodedBlock b = EncodedBlockParse.parse(fs.load(arg,
                    Blocks.MAX_SIZE));
            store.put(b);
        }
    }
}
