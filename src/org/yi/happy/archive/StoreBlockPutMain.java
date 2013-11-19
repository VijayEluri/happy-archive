package org.yi.happy.archive;

import java.io.IOException;
import java.util.List;

import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.block.parser.EncodedBlockParse;
import org.yi.happy.archive.commandLine.UsesArgs;
import org.yi.happy.archive.commandLine.UsesBlockStore;

/**
 * put a block in the store.
 */
@UsesBlockStore
@UsesArgs({ "block..." })
public class StoreBlockPutMain implements MainCommand {
    private final FileStore files;
    private final BlockStore blocks;
    private final List<String> args;

    /**
     * create.
     * 
     * @param blocks
     *            the block store to use.
     * @param files
     *            the file system to use.
     * @param args
     *            the non-option arguments.
     */
    public StoreBlockPutMain(BlockStore blocks, FileStore files,
            List<String> args) {
        this.blocks = blocks;
        this.files = files;
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
            EncodedBlock b = EncodedBlockParse.parse(files.get(arg,
                    Blocks.MAX_SIZE));
            blocks.put(b);
        }
    }
}
