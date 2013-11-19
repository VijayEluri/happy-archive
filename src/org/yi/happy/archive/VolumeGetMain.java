package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.block.parser.EncodedBlockParse;
import org.yi.happy.archive.commandLine.UsesArgs;
import org.yi.happy.archive.commandLine.UsesInput;
import org.yi.happy.archive.commandLine.UsesBlockStore;

/**
 * load a list of files from a volume into a store.
 */
@UsesBlockStore
@UsesArgs({ "volume-path" })
@UsesInput("file-list")
public class VolumeGetMain implements MainCommand {

    private final FileStore files;
    private final InputStream in;
    private final PrintStream err;
    private final BlockStore blocks;
    private final List<String> args;

    /**
     * create with context.
     * 
     * @param blocks
     *            the block store to use.
     * @param files
     *            the file system.
     * @param in
     *            the standard input.
     * @param err
     *            the standard error.
     * @param args
     *            the non-option command line arguments.
     */
    public VolumeGetMain(BlockStore blocks, FileStore files, InputStream in,
            PrintStream err, List<String> args) {
        this.blocks = blocks;
        this.files = files;
        this.in = in;
        this.err = err;
        this.args = args;
    }

    /**
     * run the body.
     * 
     * @param env
     *            the command line.
     * @throws IOException
     */
    @Override
    public void run() throws IOException {
        for (String line : new LineIterator(in)) {
            try {
                byte[] data = files.get(args.get(0) + "/" + line, Blocks.MAX_SIZE);
                EncodedBlock b = EncodedBlockParse.parse(data);
                blocks.put(b);
            } catch (Exception e) {
                e.printStackTrace(err);
            }
        }
    }
}
