package org.yi.happy.archive;

import java.io.IOException;
import java.io.PrintStream;

import org.yi.happy.archive.commandLine.UsesOutput;
import org.yi.happy.archive.commandLine.UsesBlockStore;
import org.yi.happy.archive.key.LocatorKey;

/**
 * A command line program to list the contents of store.
 */
@UsesBlockStore
@UsesOutput("key-list")
public class StoreListMain implements MainCommand {

    private final PrintStream out;
    private final BlockStore blocks;

    /**
     * Create with context.
     * 
     * @param blocks
     *            the block store to use.
     * @param fs
     *            the file system.
     * @param out
     *            the output stream.
     * @param err
     */
    public StoreListMain(BlockStore blocks, PrintStream out) {
        this.blocks = blocks;
        this.out = out;
    }

    /**
     * list the contents of the given store.
     * 
     * @param env
     *            the launch environment.
     * @throws IOException
     */
    @Override
    public void run() throws IOException {
        for (LocatorKey key : blocks) {
            out.println(key);
        }
        out.flush();
    }
}
