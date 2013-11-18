package org.yi.happy.archive;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

import org.yi.happy.annotate.DuplicatedLogic;
import org.yi.happy.archive.block.BlobEncodedBlock;
import org.yi.happy.archive.block.ContentEncodedBlock;
import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.block.parser.EncodedBlockParse;
import org.yi.happy.archive.commandLine.UsesArgs;
import org.yi.happy.archive.commandLine.UsesOutput;
import org.yi.happy.archive.index.IndexWriter;

/**
 * Index a volume that has been burned.
 */
@UsesArgs({ "image-path" })
@UsesOutput("index")
public class IndexVolumeMain implements MainCommand {

    private final FileStore fs;
    private final PrintStream out;
    private final PrintStream err;
    private final List<String> args;
    private final IndexWriter index;

    /**
     * create with a context.
     * 
     * @param fs
     *            the file system to use.
     * @param out
     *            the output stream.
     * @param err
     *            the error stream.
     * @param args
     *            the non-option arguments.
     */
    public IndexVolumeMain(FileStore fs, PrintStream out, PrintStream err,
            List<String> args) {
        this.fs = fs;
        this.out = out;
        this.err = err;
        this.args = args;

        this.index = new IndexWriter(out);
    }

    /**
     * do the index.
     * 
     * @param args
     *            the command line ( image-path )
     * @throws IOException
     */
    @Override
    public void run() throws IOException {
        try {
            List<String> names = fs.list(args.get(0));
            Collections.sort(names);
            for (String name : names) {
                process(args.get(0) + "/" + name, name);
            }
        } finally {
            out.flush();
        }
    }

    @DuplicatedLogic("with IndexCheckMain.run, and internally")
    private void process(String path, String name) throws IOException {
        if (fs.isDir(path)) {
            processDir(path, name);
            return;
        }

        try {
            byte[] data = fs.get(path, Blocks.MAX_SIZE);
            EncodedBlock block = EncodedBlockParse.parse(data);
            index.write(name, "plain", block);

            /*
             * to-blob
             */
            if (block instanceof ContentEncodedBlock) {
                block = new BlobEncodedBlock(block.getDigest(),
                        block.getCipher(), block.getBody());
                index.write(name, "to-blob", block);
            }
        } catch (Exception e) {
            e.printStackTrace(err);
        }
    }

    private void processDir(String path, String base) throws IOException {
        List<String> names = fs.list(path);
        Collections.sort(names);
        for (String name : names) {
            process(path + "/" + name, base + "/" + name);
        }
    }
}
