package org.yi.happy.archive;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.yi.happy.archive.commandLine.UsesArgs;
import org.yi.happy.archive.commandLine.UsesNeed;
import org.yi.happy.archive.commandLine.UsesOutput;
import org.yi.happy.archive.commandLine.UsesStore;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.FullKeyParse;
import org.yi.happy.archive.key.LocatorKey;

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
    private final OutputStream out;
    private final WaitHandler waitHandler;
    private final BlockStore store;
    private final NeedHandler needHandler;
    private final List<String> args;

    /**
     * create.
     * 
     * @param store
     *            the block store to use.
     * @param out
     *            where to send the stream.
     * @param waitHandler
     *            what to do when no blocks are ready.
     * @param env
     *            the invocation environment.
     */
    public FileStoreStreamGetMain(BlockStore store, OutputStream out,
            WaitHandler waitHandler, NeedHandler needHandler, List<String> args) {
        this.store = store;
        this.out = out;
        this.waitHandler = waitHandler;
        this.needHandler = needHandler;
        this.args = args;
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
        KeyInputStream in = new KeyInputStream(FullKeyParse.parseFullKey(args
                .get(0)), new RetrieveBlockStorage(store), notReadyHandler);

        Streams.copy(in, out);
    }

    private NotReadyHandler notReadyHandler = new NotReadyHandler() {
        private int progress;

        @Override
        public void notReady(SplitReader reader) throws IOException {
            List<LocatorKey> keys = new ArrayList<LocatorKey>();
            for (FullKey key : reader.getPending()) {
                keys.add(key.toLocatorKey());
            }
            needHandler.post(keys);

            waitHandler.doWait(progress != reader.getProgress());
            progress = reader.getProgress();
        }
    };
}
