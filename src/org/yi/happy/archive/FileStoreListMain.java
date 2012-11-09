package org.yi.happy.archive;

import java.io.IOException;
import java.io.Writer;

import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.key.LocatorKey;

/**
 * A command line program to list the contents of store.
 */
public class FileStoreListMain implements MainCommand {

    private final FileSystem fs;
    private final Writer out;

    /**
     * Create with context.
     * 
     * @param fs
     *            the file system.
     * @param out
     *            the output stream.
     */
    public FileStoreListMain(FileSystem fs, Writer out) {
        this.fs = fs;
        this.out = out;
    }

    /**
     * list the contents of the given store.
     * 
     * @param env
     *            the launch environment.
     * @throws IOException
     */
    public void run(Env env) throws IOException {
        if (env.hasNoStore() || env.hasArguments()) {
            out.append("use: --store store\n");
            out.flush();
            return;
        }

        FileBlockStore store = new FileBlockStore(fs, env.getStore());
        store.visit(new BlockStoreVisitor<IOException>() {
            @Override
            public void accept(LocatorKey key) throws IOException {
                out.write(key + "\n");
            }
        });
        out.flush();
    }
}
