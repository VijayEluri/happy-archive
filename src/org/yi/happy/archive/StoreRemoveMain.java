package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.key.LocatorKey;
import org.yi.happy.archive.key.LocatorKeyParse;

/**
 * Remove keys from the store.
 */
public class StoreRemoveMain implements MainCommand {

    private final FileSystem fs;
    private final PrintStream err;

    /**
     * create with context.
     * 
     * @param fs
     *            the file system to use.
     * @param out
     *            standard output.
     */
    public StoreRemoveMain(FileSystem fs, PrintStream err) {
        this.fs = fs;
        this.err = err;
    }

    /**
     * invoke the command.
     * 
     * @param args
     *            store, remove list
     * @throws IOException
     */
    @Override
    public void run(Env env) throws IOException {
        if (env.hasNoStore() || env.hasArgumentCount() != 1) {
            err.println("use: --store store remove.lst");
            return;
        }

        BlockStore store = new FileBlockStore(fs, env.getStore());
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
