package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.block.parser.EncodedBlockParse;
import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.commandLine.UsesArgs;
import org.yi.happy.archive.commandLine.UsesInput;
import org.yi.happy.archive.commandLine.UsesStore;
import org.yi.happy.archive.file_system.FileSystem;

/**
 * load a list of files from a volume into a store.
 */
@UsesStore
@UsesArgs({ "volume-path" })
@UsesInput("file-list")
public class VolumeGetMain implements MainCommand {

    private final FileSystem fs;
    private final InputStream in;
    private final PrintStream err;
    private final BlockStore store;
    private final Env env;

    /**
     * create with context.
     * 
     * @param store
     *            the block store to use.
     * @param fs
     *            the file system.
     * @param in
     *            the standard input.
     * @param err
     *            the standard error.
     * @param env
     *            the invocation environment.
     */
    public VolumeGetMain(BlockStore store, FileSystem fs, InputStream in,
            PrintStream err, Env env) {
        this.store = store;
        this.fs = fs;
        this.in = in;
        this.err = err;
        this.env = env;
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
        LineCursor in = new LineCursor(this.in);
        while (in.next()) {
            try {
                byte[] data = fs.load(fs.join(env.getArgument(0), in.get()),
                        Blocks.MAX_SIZE);
                EncodedBlock b = EncodedBlockParse.parse(data);
                store.put(b);
            } catch (Exception e) {
                e.printStackTrace(err);
            }
        }
    }
}
