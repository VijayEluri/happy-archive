package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.yi.happy.annotate.EntryPoint;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.file_system.RealFileSystem;
import org.yi.happy.archive.key.LocatorKey;
import org.yi.happy.archive.key.LocatorKeyParse;

/**
 * Remove keys from the store.
 */
public class StoreRemoveMain {

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
    public void run(String... args) throws IOException {
        if (args.length != 2) {
            out.write("use: store remove.lst\n");
            return;
        }

        BlockStore store = new FileBlockStore(fs, args[0]);
        InputStream in = fs.openInputStream(args[1]);
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

    /**
     * invoke from the command line.
     * 
     * @param args
     * @throws IOException
     */
    @EntryPoint
    public static void main(String[] args) throws IOException {
        FileSystem fs = new RealFileSystem();
        Writer out = new OutputStreamWriter(System.out, "UTF-8");
        try {
            new StoreRemoveMain(fs, out).run(args);
        } finally {
            out.flush();
        }

    }
}
