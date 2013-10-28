package org.yi.happy.archive;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.yi.happy.annotate.DuplicatedLogic;
import org.yi.happy.annotate.RestoreLoop;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.commandLine.UsesArgs;
import org.yi.happy.archive.commandLine.UsesNeed;
import org.yi.happy.archive.commandLine.UsesOutput;
import org.yi.happy.archive.commandLine.UsesStore;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.FullKeyParse;
import org.yi.happy.archive.key.LocatorKey;
import org.yi.happy.archive.restore.RestoreEngine;

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
     * @param needHandler
     *            where to post the needed keys.
     * @param args
     *            the non-option command line arguments.
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
    @RestoreLoop
    public void run() throws IOException {
        FragmentOutputStream target = new FragmentOutputStream(out);
        ClearBlockSource source = new StorageClearBlockSource(store);
        FullKey key = FullKeyParse.parseFullKey(args.get(0));

        RestoreEngine engine = new RestoreEngine(key);

        /*
         * do the work
         */
        while (true) {
            boolean progress = false;
            while (engine.findReady()) {
                Block block = source.get(engine.getKey());
                if (block == null) {
                    break;
                }

                Fragment part = engine.step(block);
                progress = true;

                if (part != null) {
                    target.write(part);
                }
            }

            if (engine.isDone()) {
                break;
            }

            notReady(engine, progress);
        }
    }

    @DuplicatedLogic("with FileStoreTagGetMain.notReady")
    private void notReady(RestoreEngine engine, boolean progress)
            throws IOException {
        List<LocatorKey> keys = new ArrayList<LocatorKey>();
        for (FullKey key : engine.getNeeded()) {
            keys.add(key.toLocatorKey());
        }
        needHandler.post(keys);

        waitHandler.doWait(progress);
    }
}
