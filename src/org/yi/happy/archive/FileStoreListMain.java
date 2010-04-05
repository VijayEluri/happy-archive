package org.yi.happy.archive;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.file_system.RealFileSystem;
import org.yi.happy.archive.key.LocatorKey;

/**
 * A command line program to list the contents of store.
 */
public class FileStoreListMain {

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
     * @param args
     *            store.
     * @throws IOException
     */
    public void run(String... args) throws IOException {
        if (args.length < 1) {
            out.append("use: store\n");
            return;
        }

        FileBlockStore store = new FileBlockStore(fs, args[0]);
        store.visit(new BlockStoreVisitor<IOException>() {
            @Override
            public void accept(LocatorKey key) throws IOException {
                out.write(key + "\n");
            }
        });
    }

    /**
     * Command line program to list the keys in a store.
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        FileSystem fs = new RealFileSystem();
        Writer out = new OutputStreamWriter(System.out);
        new FileStoreListMain(fs, out).run(args);
        out.flush();
    }
}
