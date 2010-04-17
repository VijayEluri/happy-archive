package org.yi.happy.archive;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.List;

import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.block.parser.EncodedBlockParse;
import org.yi.happy.archive.crypto.DigestFactory;
import org.yi.happy.archive.crypto.DigestProvider;
import org.yi.happy.archive.crypto.Digests;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.file_system.RealFileSystem;

/**
 * Index a volume that has been burned.
 */
public class IndexVolumeMain {

    private final FileSystem fs;
    private final Writer out;
    private final DigestProvider digest;

    /**
     * create with a context.
     * 
     * @param fs
     *            the file system to use.
     * @param out
     *            the output stream.
     */
    public IndexVolumeMain(FileSystem fs, Writer out) {
        this.fs = fs;
        this.out = out;

        digest = DigestFactory.getProvider("sha-256");
    }

    /**
     * do the index.
     * 
     * @param args
     *            the command line ( image-path )
     * @throws IOException
     */
    public void run(String... args) throws IOException {
        if (args.length < 1) {
            out.write("use: image\n");
            return;
        }

        List<String> names = fs.list(args[0]);
        Collections.sort(names);
        for (String name : names) {
            process(fs.join(args[0], name), name);
        }
    }

    private void process(String path, String name) throws IOException {
        if (fs.isDir(path)) {
            processDir(path, name);
            return;
        }

        byte[] data = fs.load(path, Blocks.MAX_SIZE);
        EncodedBlock block = EncodedBlockParse.parse(data);

        String key = block.getKey().toString();

        data = block.asBytes();

        String hash = Base16.encode(Digests.digestData(digest, data));
        String size = Integer.toString(data.length);

        out.write(name + "\t" + "plain" + "\t" + key + "\t" + hash + "\t"
                + size + "\n");
    }

    private void processDir(String path, String base) throws IOException {
        List<String> names = fs.list(path);
        Collections.sort(names);
        for (String name : names) {
            process(fs.join(path, name), base + "/" + name);
        }
    }

    /**
     * invoke from the command line.
     * 
     * @param args
     *            the command line arguments.
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        FileSystem fs = new RealFileSystem();
        Writer out = new OutputStreamWriter(System.out);
        try {
            new IndexVolumeMain(fs, out).run(args);
        } finally {
            out.flush();
        }
    }
}
