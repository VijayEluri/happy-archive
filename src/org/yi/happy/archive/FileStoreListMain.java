package org.yi.happy.archive;

import java.io.IOException;
import java.io.PrintStream;

import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.commandLine.UsesStore;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.key.LocatorKey;

/**
 * A command line program to list the contents of store.
 */
@UsesStore
public class FileStoreListMain implements MainCommand {

    private final FileSystem fs;
    private final PrintStream out;
    private final PrintStream err;

    /**
     * Create with context.
     * 
     * @param fs
     *            the file system.
     * @param out
     *            the output stream.
     */
    public FileStoreListMain(FileSystem fs, PrintStream out, PrintStream err) {
        this.fs = fs;
        this.out = out;
        this.err = err;
    }

    /**
     * list the contents of the given store.
     * 
     * @param env
     *            the launch environment.
     * @throws IOException
     */
    @Override
    public void run(Env env) throws IOException {
        if (env.hasNoStore() || env.hasArguments()) {
            err.println("use: --store store");
            return;
        }

        FileBlockStore store = new FileBlockStore(fs, env.getStore());
        store.visit(new BlockStoreVisitor<IOException>() {
            @Override
            public void accept(LocatorKey key) throws IOException {
                out.println(key);
            }
        });
        out.flush();
    }
}
