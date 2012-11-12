package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.yi.happy.archive.block.encoder.BlockEncoder;
import org.yi.happy.archive.block.encoder.BlockEncoderFactory;
import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.commandLine.UsesStore;
import org.yi.happy.archive.file_system.FileSystem;

/**
 * given the base name of a file store on the command line, and a stream on
 * stdin, store the stream, and print out the resulting key.
 */
@UsesStore
public class FileStoreStreamPutMain implements MainCommand {
    private FileSystem fs;
    private InputStream in;
    private PrintStream out;

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
    public FileStoreStreamPutMain(FileSystem fs, InputStream in, PrintStream out) {
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
    @Override
    public void run(Env env) throws IOException {
        BlockStore store = new FileBlockStore(fs, env.getStore());
        BlockEncoder encoder = BlockEncoderFactory.getContentDefault();

        KeyOutputStream s = new KeyOutputStream(new StoreBlockStorage(encoder,
                store));

        Streams.copy(in, s);
        s.close();

        out.println(s.getFullKey());
    }
}
