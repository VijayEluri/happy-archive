package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.key.LocatorKey;
import org.yi.happy.archive.key.LocatorKeyParse;

/**
 * Remove keys from the store.
 */
public class StoreRemoveMain implements MainCommand {

    private final FileSystem fs;
    private final Writer out;

    /**
     * create with context.
     * 
     * @param fs
     *            the file system to use.
     * @param out
     *            standard output.
     */
    public StoreRemoveMain(FileSystem fs, Writer out) {
        this.fs = fs;
        this.out = out;
    }

    /**
     * invoke the command.
     * 
     * @param args
     *            store, remove list
     * @throws IOException
     */
    public void run(Env env) throws IOException {
        if (env.hasNoStore() || env.hasArgumentCount() != 1) {
            out.write("use: --store store remove.lst\n");
            out.flush();
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
