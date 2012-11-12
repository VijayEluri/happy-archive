package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.commandLine.UsesArgs;
import org.yi.happy.archive.commandLine.UsesStore;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.key.LocatorKey;
import org.yi.happy.archive.key.LocatorKeyParse;

/**
 * Remove keys from the store.
 */
@UsesStore
@UsesArgs("key-list")
public class StoreRemoveMain implements MainCommand {

    private final FileSystem fs;
    private final PrintStream err;
    private final BlockStore store;
    private final Env env;

    /**
     * create with context.
     * 
     * @param store
     *            the block store to use.
     * @param fs
     *            the file system to use.
     * @param out
     *            standard output.
     */
    public StoreRemoveMain(BlockStore store, FileSystem fs, PrintStream err,
            Env env) {
        this.store = store;
        this.fs = fs;
        this.err = err;
        this.env = env;
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
        InputStream in = fs.openInputStream(env.getArgument(0));
        try {
            LineCursor line = new LineCursor(in);
            while (line.next()) {
                LocatorKey key = LocatorKeyParse.parseLocatorKey(line.get());
                store.remove(key);
            }
        } finally {
            in.close();
        }
    }
}
