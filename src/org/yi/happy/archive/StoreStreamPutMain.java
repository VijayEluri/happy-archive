package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.yi.happy.archive.block.encoder.BlockEncoder;
import org.yi.happy.archive.block.encoder.BlockEncoderFactory;
import org.yi.happy.archive.commandLine.UsesInput;
import org.yi.happy.archive.commandLine.UsesOutput;
import org.yi.happy.archive.commandLine.UsesStore;

/**
 * given the base name of a file store on the command line, and a stream on
 * stdin, store the stream, and print out the resulting key.
 */
@UsesStore
@UsesInput("file")
@UsesOutput("key")
public class StoreStreamPutMain implements MainCommand {
    private InputStream in;
    private PrintStream out;
    private final BlockStore store;

    /**
     * create.
     * 
     * @param store
     *            the block store.
     * @param fs
     *            the file system to use.
     * @param in
     *            the stream to store.
     * @param out
     *            the stream to write the result.
     */
    public StoreStreamPutMain(BlockStore store, InputStream in, PrintStream out) {
        this.store = store;
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
    public void run() throws IOException {
        BlockEncoder encoder = BlockEncoderFactory.getContentDefault();

        KeyOutputStream s = new KeyOutputStream(new ClearBlockTargetStore(encoder,
                store));

        Streams.copy(in, s);
        s.close();

        out.println(s.getFullKey());
    }
}
