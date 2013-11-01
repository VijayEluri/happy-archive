package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;

import org.yi.happy.archive.commandLine.UsesInput;
import org.yi.happy.archive.commandLine.UsesStore;
import org.yi.happy.archive.key.LocatorKey;
import org.yi.happy.archive.key.LocatorKeyParse;

/**
 * Remove keys from the store.
 */
@UsesStore
@UsesInput("key-list")
public class StoreRemoveMain implements MainCommand {
    private final BlockStore blocks;
    private final InputStream in;

    /**
     * create with context.
     * 
     * @param blocks
     *            the block store to use.
     * @param in
     *            the input stream to use.
     */
    public StoreRemoveMain(BlockStore blocks, InputStream in) {
        this.blocks = blocks;
        this.in = in;
    }

    /**
     * invoke the command.
     * 
     * @param args
     *            store, remove list
     * @throws IOException
     */
    @Override
    public void run() throws IOException {
        for (String line : new LineIterator(in)) {
            LocatorKey key = LocatorKeyParse.parseLocatorKey(line);
            blocks.remove(key);
        }
    }
}
