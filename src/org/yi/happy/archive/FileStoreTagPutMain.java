package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.yi.happy.annotate.DuplicatedLogic;
import org.yi.happy.annotate.EntryPoint;
import org.yi.happy.annotate.MagicLiteral;
import org.yi.happy.annotate.SmellsMessy;
import org.yi.happy.archive.block.encoder.BlockEncoderFactory;
import org.yi.happy.archive.commandLine.MyArgs;
import org.yi.happy.archive.commandLine.MyArgs.CommandLineException;
import org.yi.happy.archive.crypto.DigestFactory;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.file_system.RealFileSystem;
import org.yi.happy.archive.tag.TagBuilder;
import org.yi.happy.archive.tag.TagOutputStream;

/**
 * Store files in a file store, printing out tags.
 */
public class FileStoreTagPutMain {

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
     * @param args
     *            store, file...
     * @throws IOException
     */
    @MagicLiteral
    @SmellsMessy
    @DuplicatedLogic("getting options, with LocalCandidateList")
    public void run(String... args) throws IOException {
        /*
         * parse command line
         */
        MyArgs cmd;
        try {
            cmd = MyArgs.parse(args).needStore();
        } catch (CommandLineException e) {
            e.showHelp(out);
            return;
        }

        FileBlockStore store = new FileBlockStore(fs, cmd.getStore());

        /*
         * do the work
         */

        StoreBlockStorage s = new StoreBlockStorage(
                BlockEncoderFactory.getContentDefault(), store);

        TagOutputStream out = new TagOutputStream(this.out);

        for (String arg : cmd.getFiles()) {
            if (fs.isFile(arg)) {
                KeyOutputStream o1 = new KeyOutputStream(s);
                DigestOutputStream o2 = new DigestOutputStream(DigestFactory
                        .getProvider("sha-256").get());
                TeeOutputStream o = new TeeOutputStream(o1, o2);
                InputStream in = fs.openInputStream(arg);
                try {
                    Streams.copy(in, o);
                } finally {
                    in.close();
                }
                o.close();

                out.write(new TagBuilder().put("name", arg).put("type", "file")
                        .put("size", Long.toString(o2.getSize()))
                        .put("data", o1.getFullKey().toString())
                        .put("hash", "sha-256:" + o2.getHash()).create());
            }
        }
    }

    /**
     * The command line entry point.
     * 
     * @param args
     * @throws IOException
     */
    @EntryPoint
    public static void main(String[] args) throws IOException {
        FileSystem fs = new RealFileSystem();
        try {
            new FileStoreTagPutMain(fs, System.out).run(args);
        } finally {
            System.out.flush();
        }
    }
}
