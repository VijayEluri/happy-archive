package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.yi.happy.annotate.EntryPoint;
import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.file_system.RealFileSystem;
import org.yi.happy.archive.key.KeyParse;
import org.yi.happy.archive.key.LocatorKey;

/**
 * Build an image of a backup disk. The set of files that will be burned to a
 * backup disk.
 */
public class BuildImageMain {

    private final FileSystem fs;
    private final Writer out;

    /**
     * create with context.
     * 
     * @param fs
     *            the file system to use.
     * @param out
     *            where to send output.
     */
    public BuildImageMain(FileSystem fs, Writer out) {
        this.fs = fs;
        this.out = out;
    }

    /**
     * Build the image.
     * 
     * @param args
     *            the command line arguments.
     * @throws IOException
     */
    public void run(String... args) throws IOException {
        if (args.length < 4) {
            out.write("use: store outstanding-list image-directory"
                    + " image-size-in-mb\n");
            return;
        }

        FileBlockStore store = new FileBlockStore(fs, args[0]);
        InputStream in0 = fs.openInputStream(args[1]);
        int limit = Integer.parseInt(args[3]);
        IsoEstimate size = new IsoEstimate();
        int count = 0;
        try {
            LineCursor lines = new LineCursor(in0);

            while (lines.next()) {
                LocatorKey key = KeyParse.parseLocatorKey(lines.get());
                EncodedBlock block = store.get(key);
                byte[] data = block.asBytes();
                size.add(data.length);
                if (size.getMegaSize() > limit) {
                    size.remove(data.length);
                    break;
                }
                fs.save(args[2] + "/" + String.format("%08x.dat", count), data);
                count++;
            }

        } finally {
            in0.close();
        }

        String full = "";
        if (size.getMegaSize() > limit * 99 / 100) {
            full = "\tfull";
        }

        out.write(size.getCount() + "\t" + size.getMegaSize() + full + "\n");
    }

    /**
     * Invoke from the command line.
     * 
     * @param args
     *            the command line arguments.
     * @throws IOException
     */
    @EntryPoint
    public static void main(String[] args) throws IOException {
        FileSystem fs = new RealFileSystem();
        Writer out = new OutputStreamWriter(System.out);
        new BuildImageMain(fs, out).run(args);
        out.flush();
    }

}
