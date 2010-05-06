package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.yi.happy.annotate.EntryPoint;
import org.yi.happy.annotate.MagicLiteral;
import org.yi.happy.archive.block.encoder.BlockEncoderFactory;
import org.yi.happy.archive.crypto.DigestFactory;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.file_system.RealFileSystem;

/**
 * Store files in a file store, printing out tags.
 */
public class FileStoreTagPutMain {

    private final FileSystem fs;
    private final Writer out;

    /**
     * create with context.
     * 
     * @param fs
     * @param out
     */
    public FileStoreTagPutMain(FileSystem fs, Writer out) {
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
    public void run(String... args) throws IOException {
        FileBlockStore store = new FileBlockStore(fs, args[0]);
        StoreBlockStorage s = new StoreBlockStorage(BlockEncoderFactory
                .getContentDefault(), store);

        for (int i = 1; i < args.length; i++) {
            if (fs.isFile(args[i])) {
                KeyOutputStream o1 = new KeyOutputStream(s);
                DigestOutputStream o2 = new DigestOutputStream(DigestFactory
                        .getProvider("sha-256").get());
                TeeOutputStream o = new TeeOutputStream(o1, o2);
                InputStream in = fs.openInputStream(args[i]);
                try {
                    Streams.copy(in, o);
                } finally {
                    in.close();
                }
                o.close();

                out.write("name=" + args[i] + "\n");
                out.write("type=file\n");
                out.write("size=" + o2.getSize() + "\n");
                out.write("data=" + o1.getFullKey() + "\n");
                out.write("hash=sha-256:" + o2.getHash() + "\n");
                out.write("\n");
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
        Writer out = new OutputStreamWriter(System.out, "UTF-8");
        try {
            new FileStoreTagPutMain(fs, out).run(args);
        } finally {
            out.flush();
        }
    }
}
