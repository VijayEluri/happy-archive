package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

import org.yi.happy.annotate.MagicLiteral;
import org.yi.happy.annotate.SmellsMessy;
import org.yi.happy.archive.block.encoder.BlockEncoderFactory;
import org.yi.happy.archive.commandLine.UsesArgs;
import org.yi.happy.archive.commandLine.UsesOutput;
import org.yi.happy.archive.commandLine.UsesBlockStore;
import org.yi.happy.archive.crypto.DigestFactory;
import org.yi.happy.archive.tag.TagBuilder;
import org.yi.happy.archive.tag.TagOutputStream;

/**
 * Store files in a file store, printing out tags.
 */
@UsesBlockStore
@UsesArgs({ "file..." })
@UsesOutput("tag-list")
public class StoreTagPutMain implements MainCommand {

    private final FileStore files;
    private final PrintStream out;
    private final BlockStore blocks;
    private final List<String> args;

    /**
     * create with context.
     * 
     * @param blocks
     *            the block store to use.
     * @param files
     *            the file system.
     * @param out
     *            the output stream.
     * @param args
     *            the non-option arguments.
     */
    public StoreTagPutMain(BlockStore blocks, FileStore files,
            PrintStream out, List<String> args) {
        this.blocks = blocks;
        this.files = files;
        this.out = out;
        this.args = args;
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
    public void run() throws IOException {
        try {
            /*
             * do the work
             */

            ClearBlockTargetStore s = new ClearBlockTargetStore(
                    BlockEncoderFactory.getContentDefault(), blocks);

            TagOutputStream out = new TagOutputStream(this.out);

            for (String arg : args) {
                if (files.isFile(arg)) {
                    KeyOutputStream o1 = new KeyOutputStream(s);
                    DigestOutputStream o2 = new DigestOutputStream(
                            DigestFactory.getProvider("sha-256").get());
                    TeeOutputStream o = new TeeOutputStream(o1, o2);
                    InputStream in = files.getStream(arg);
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
