package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import org.yi.happy.archive.block.encoder.BlockEncoder;
import org.yi.happy.archive.block.encoder.BlockEncoderFactory;
import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.file_system.FileSystem;

/**
 * given the base name of a file store on the command line, and a stream on
 * stdin, store the stream, and print out the resulting key.
 */
public class FileStoreStreamPutMain implements MainCommand {
    private FileSystem fs;
    private InputStream in;
    private Writer out;

    /**
     * create.
     * 
     * @param fs
     *            the file system to use.
     * @param in
     *            the stream to store.
     * @param out
     *            the stream to write the result.
     */
    public FileStoreStreamPutMain(FileSystem fs, InputStream in, Writer out) {
        this.fs = fs;
        this.in = in;
        this.out = out;
    }

    /**
     * store a stream in a file store.
     * 
     * @param env
     *            the file store path.
     * @throws IOException
     */
    public void run(Env env) throws IOException {
        if (env.hasNoStore()) {
            out.write("use: --store store\n");
            out.flush();
            return;
        }

        BlockStore store = new FileBlockStore(fs, env.getStore());
        BlockEncoder encoder = BlockEncoderFactory.getContentDefault();

        KeyOutputStream s = new KeyOutputStream(new StoreBlockStorage(encoder,
                store));

        Streams.copy(in, s);
        s.close();

        out.write(s.getFullKey() + "\n");
        out.flush();
    }
}
