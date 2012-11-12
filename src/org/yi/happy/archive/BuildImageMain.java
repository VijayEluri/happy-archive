package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.yi.happy.annotate.SmellsMessy;
import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.commandLine.UsesArgs;
import org.yi.happy.archive.commandLine.UsesOutput;
import org.yi.happy.archive.commandLine.UsesStore;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.key.LocatorKey;
import org.yi.happy.archive.key.LocatorKeyParse;

/**
 * Build an image of a backup disk. The set of files that will be burned to a
 * backup disk.
 */
@UsesStore
@UsesArgs({ "key-list", "image-path", "size-mb" })
@UsesOutput("size")
public class BuildImageMain implements MainCommand {

    private final FileSystem fs;
    private final PrintStream out;
    private final PrintStream err;
    private final BlockStore store;
    private final Env env;

    /**
     * create with context.
     * 
     * @param store
     *            the block store to use.
     * @param fs
     *            the file system to use.
     * @param out
     *            where to send output.
     * @param err
     */
    public BuildImageMain(BlockStore store, FileSystem fs, PrintStream out,
            PrintStream err, Env env) {
        this.store = store;
        this.fs = fs;
        this.out = out;
        this.err = err;
        this.env = env;
    }

    /**
     * Build the image.
     * 
     * @param args
     *            the command line arguments.
     * @throws IOException
     */
    @Override
    @SmellsMessy
    public void run() throws IOException {
        InputStream in0 = fs.openInputStream(env.getArgument(0));
        int limit = Integer.parseInt(env.getArgument(2));
        IsoEstimate size = new IsoEstimate();
        int count = 0;
        try {
            LineCursor lines = new LineCursor(in0);

            while (lines.next()) {
                LocatorKey key = LocatorKeyParse.parseLocatorKey(lines.get());
                EncodedBlock block;
                try {
                    block = store.get(key);
                } catch (DecodeException e) {
                    err.println("error loading block: " + key);
                    e.printStackTrace(err);
                    continue;
                }
                byte[] data = block.asBytes();
                size.add(data.length);
                if (size.getMegaSize() > limit) {
                    size.remove(data.length);
                    break;
                }
                fs.save(env.getArgument(1) + "/"
                        + String.format("%08x.dat", count), data);
                count++;
            }

        } finally {
            in0.close();
        }

        String full = "";
        if (size.getMegaSize() > limit * 99 / 100) {
            full = "\tfull";
        }

        out.println(size.getCount() + "\t" + size.getMegaSize() + full);
    }
}
