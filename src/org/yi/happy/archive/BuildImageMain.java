package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.yi.happy.annotate.SmellsMessy;
import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.key.LocatorKey;
import org.yi.happy.archive.key.LocatorKeyParse;

/**
 * Build an image of a backup disk. The set of files that will be burned to a
 * backup disk.
 */
public class BuildImageMain implements MainCommand {

    private final FileSystem fs;
    private final PrintStream out;
    private final PrintStream err;

    /**
     * create with context.
     * 
     * @param fs
     *            the file system to use.
     * @param out
     *            where to send output.
     */
    public BuildImageMain(FileSystem fs, PrintStream out, PrintStream err) {
        this.fs = fs;
        this.out = out;
        this.err = err;
    }

    /**
     * Build the image.
     * 
     * @param args
     *            the command line arguments.
     * @throws IOException
     */
    @SmellsMessy
    public void run(Env env) throws IOException {
        if (env.hasNoStore() || env.hasArgumentCount() != 3) {
            err.println("use: --store store outstanding-list image-directory"
                    + " image-size-in-mb\n");
            return;
        }

        FileBlockStore store = new FileBlockStore(fs, env.getStore());
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
