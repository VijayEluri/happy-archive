package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

import org.yi.happy.annotate.DuplicatedLogic;
import org.yi.happy.archive.block.BlobEncodedBlock;
import org.yi.happy.archive.block.ContentEncodedBlock;
import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.block.parser.EncodedBlockParse;
import org.yi.happy.archive.commandLine.UsesArgs;
import org.yi.happy.archive.commandLine.UsesInput;
import org.yi.happy.archive.commandLine.UsesOutput;
import org.yi.happy.archive.index.IndexWriter;

/**
 * Check that the files on a volume are still readable. Do this by building a
 * new index based on the file names of an existing index.
 */
@UsesArgs({ "image-path" })
@UsesInput("index")
@UsesOutput("index")
public class IndexCheckMain implements MainCommand {
    private final FileStore files;
    private final InputStream in;
    private final PrintStream out;
    private final PrintStream err;
    private final List<String> args;

    /**
     * setup the command.
     * 
     * @param files
     *            the file system to use.
     * @param in
     *            the input stream to use.
     * @param out
     *            the output stream to use.
     * @param err
     *            the error stream to use.
     * @param args
     *            the arguments to use.
     */
    public IndexCheckMain(FileStore files, InputStream in, PrintStream out,
            PrintStream err, List<String> args) {
        this.files = files;
        this.in = in;
        this.out = out;
        this.err = err;
        this.args = args;
    }

    @Override
    @DuplicatedLogic("with IndexVolumeMain.process, and internally")
    public void run() throws Exception {
        String imagePath = args.get(0);
        IndexWriter index = new IndexWriter(out);

        String prevName = null;
        for (String line : new LineIterator(in)) {
            try {
                String name = line.split("\t")[0];
                String path = imagePath + "/" + name;
                if (name.equals(prevName)) {
                    continue;
                }
                prevName = name;

                byte[] data = files.get(path, Blocks.MAX_SIZE);
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
            } catch (IOException e) {
                err.println("Unable to read: " + prevName);
            } catch (Exception e) {
                e.printStackTrace(err);
            }
        }
    }

}
