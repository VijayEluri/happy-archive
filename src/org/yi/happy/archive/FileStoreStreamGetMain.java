package org.yi.happy.archive;

import java.io.IOException;
import java.io.OutputStream;

import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.key.FullKeyParse;

/**
 * Fetch a stream, the blocks may not all available in the file store, so the
 * ones that are needed are put in a list, and the process continues to be
 * retried until all the needed blocks become available.
 */
public class FileStoreStreamGetMain implements MainCommand {
    private final FileSystem fs;
    private final OutputStream out;
    private final WaitHandler waitHandler;

    /**
     * create.
     * 
     * @param fs
     *            the file system.
     * @param out
     *            where to send the stream.
     * @param waitHandler
     *            what to do when no blocks are ready.
     */
    public FileStoreStreamGetMain(FileSystem fs, OutputStream out,
            WaitHandler waitHandler) {
        this.fs = fs;
        this.out = out;
        this.waitHandler = waitHandler;
    }

    /**
     * restore a stream.
     * 
     * @param env
     *            the block store, where to write the pending block list, the
     *            full key to fetch.
     * @throws IOException
     */
    public void run(Env env) throws IOException {
        if (env.hasNoStore() || env.hasNoNeed() || env.hasArgumentCount() != 1) {
            System.err.println("use: --store store --need need key");
            return;
        }
        FileBlockStore store = new FileBlockStore(fs, env.getStore());
        pendingFile = env.getNeed();

        KeyInputStream in = new KeyInputStream(FullKeyParse.parseFullKey(env
                .getArgument(0)),
                new RetrieveBlockStorage(store), notReadyHandler);

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
