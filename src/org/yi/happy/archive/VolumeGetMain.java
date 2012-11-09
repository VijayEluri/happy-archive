package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.block.parser.EncodedBlockParse;
import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.file_system.FileSystem;

/**
 * load a list of files from a volume into a store.
 */
public class VolumeGetMain implements MainCommand {

    private final FileSystem fs;
    private final InputStream in;
    private final PrintStream out;
    private final PrintStream err;

    /**
     * create with context.
     * 
     * @param fs
     *            the file system.
     * @param in
     *            the standard input.
     * @param out
     *            the standard output.
     * @param err
     *            the standard error.
     */
    public VolumeGetMain(FileSystem fs, InputStream in, PrintStream out,
            PrintStream err) {
        this.fs = fs;
        this.in = in;
        this.out = out;
        this.err = err;
    }

    /**
     * run the body.
     * 
     * @param env
     *            the command line.
     * @throws IOException
     */
    public void run(Env env) throws IOException {
        if (env.hasNoStore() || env.hasArgumentCount() != 1) {
            err.println("use: store base < list");
            return;
        }

        FileBlockStore s = new FileBlockStore(fs, env.getStore());

        LineCursor in = new LineCursor(this.in);
        while (in.next()) {
            try {
                byte[] data = fs.load(fs.join(env.getArgument(0), in.get()),
                        Blocks.MAX_SIZE);
                EncodedBlock b = EncodedBlockParse.parse(data);
                s.put(b);
            } catch (Exception e) {
                e.printStackTrace(err);
            }
        }
    }
}
