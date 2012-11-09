package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.yi.happy.annotate.MagicLiteral;
import org.yi.happy.annotate.SmellsMessy;
import org.yi.happy.archive.block.encoder.BlockEncoderFactory;
import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.crypto.DigestFactory;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.tag.TagBuilder;
import org.yi.happy.archive.tag.TagOutputStream;

/**
 * Store files in a file store, printing out tags.
 */
public class FileStoreTagPutMain implements MainCommand {

    private final FileSystem fs;
    private final PrintStream out;

    /**
     * create with context.
     * 
     * @param fs
     * @param out
     */
    public FileStoreTagPutMain(FileSystem fs, PrintStream out) {
        this.fs = fs;
        this.out = out;
    }

    /**
     * the main logic to store a set of files.
     * 
     * @param env
     *            The environment.
     * @throws IOException
     */
    @Override
    @MagicLiteral
    @SmellsMessy
    public void run(Env env) throws IOException {
        if (env.hasNoStore() || env.hasNoArguments()) {
            out.println("use: --store store file...");
            out.flush();
            return;
        }

        try {
            FileBlockStore store = new FileBlockStore(fs, env.getStore());

            /*
             * do the work
             */

            StoreBlockStorage s = new StoreBlockStorage(
                    BlockEncoderFactory.getContentDefault(), store);

            TagOutputStream out = new TagOutputStream(this.out);

            for (String arg : env.getArguments()) {
                if (fs.isFile(arg)) {
                    KeyOutputStream o1 = new KeyOutputStream(s);
                    DigestOutputStream o2 = new DigestOutputStream(
                            DigestFactory.getProvider("sha-256").get());
                    TeeOutputStream o = new TeeOutputStream(o1, o2);
                    InputStream in = fs.openInputStream(arg);
                    try {
                        Streams.copy(in, o);
                    } finally {
                        in.close();
                    }
                    o.close();

                    out.write(new TagBuilder().put("name", arg)
                            .put("type", "file")
                            .put("size", Long.toString(o2.getSize()))
                            .put("data", o1.getFullKey().toString())
                            .put("hash", "sha-256:" + o2.getHash()).create());
                }
            }
        } finally {
            System.out.flush();
        }
    }
}
