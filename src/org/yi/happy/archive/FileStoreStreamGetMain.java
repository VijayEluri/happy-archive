package org.yi.happy.archive;

import java.io.IOException;
import java.io.OutputStream;

import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.commandLine.UsesArgs;
import org.yi.happy.archive.commandLine.UsesNeed;
import org.yi.happy.archive.commandLine.UsesOutput;
import org.yi.happy.archive.commandLine.UsesStore;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.key.FullKeyParse;

/**
 * Fetch a stream, the blocks may not all available in the file store, so the
 * ones that are needed are put in a list, and the process continues to be
 * retried until all the needed blocks become available.
 */
@UsesStore
@UsesNeed
@UsesArgs({ "key" })
@UsesOutput("file")
public class FileStoreStreamGetMain implements MainCommand {
    private final FileSystem fs;
    private final OutputStream out;
    private final WaitHandler waitHandler;
    private final BlockStore store;
    private final Env env;

    /**
     * create.
     * 
     * @param store
     *            the block store to use.
     * @param fs
     *            the file system.
     * @param out
     *            where to send the stream.
     * @param waitHandler
     *            what to do when no blocks are ready.
     */
    public FileStoreStreamGetMain(BlockStore store, FileSystem fs,
            OutputStream out, WaitHandler waitHandler, Env env) {
        this.store = store;
        this.fs = fs;
        this.out = out;
        this.waitHandler = waitHandler;
        this.env = env;
    }

    /**
     * restore a stream.
     * 
     * @param env
     *            the block store, where to write the pending block list, the
     *            full key to fetch.
     * @throws IOException
     */
    @Override
    public void run() throws IOException {
        pendingFile = env.getNeed();

        KeyInputStream in = new KeyInputStream(FullKeyParse.parseFullKey(env
                .getArgument(0)), new RetrieveBlockStorage(store),
                notReadyHandler);

        Streams.copy(in, out);
    }

    private String pendingFile;

    private NotReadyHandler notReadyHandler = new NotReadyHandler() {
        private int progress;

        @Override
        public void notReady(SplitReader reader) throws IOException {
            PendingList.savePendingListFile(reader.getPending(), fs,
                    pendingFile);
            waitHandler.doWait(progress != reader.getProgress());
            progress = reader.getProgress();
        }
    };
}
