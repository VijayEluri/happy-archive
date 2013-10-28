package org.yi.happy.archive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.yi.happy.annotate.DuplicatedLogic;
import org.yi.happy.annotate.RestoreLoop;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.commandLine.UsesArgs;
import org.yi.happy.archive.commandLine.UsesNeed;
import org.yi.happy.archive.commandLine.UsesStore;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.file_system.RandomOutputFile;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.FullKeyParse;
import org.yi.happy.archive.key.LocatorKey;
import org.yi.happy.archive.restore.RestoreEngine;

/**
 * get a file from a file store.
 */
@UsesStore
@UsesNeed
@UsesArgs({ "key", "output" })
public class FileStoreFileGetMain implements MainCommand {
    private final BlockStore store;
    private final List<String> args;
    private final FileSystem fs;
    private final NeedHandler needHandler;

    /**
     * create.
     * 
     * @param store
     *            the block store to use.
     * @param fs
     *            the file system to use.
     * @param waitHandler
     *            what to do when it is time to wait for data.
     * @param needHandler
     *            where to post the needed keys.
     * @param args
     *            the non-option command line arguments.
     */
    public FileStoreFileGetMain(BlockStore store, FileSystem fs,
            WaitHandler waitHandler, NeedHandler needHandler, List<String> args) {
        this.store = store;
        this.fs = fs;
        this.waitHandler = waitHandler;
        this.needHandler = needHandler;
        this.args = args;
    }

    /**
     * get a file from a file store.
     * 
     * @param env
     *            the file store, where to write the pending list, the key to
     *            fetch, the output file name.
     * @throws IOException
     */
    @Override
    @RestoreLoop
    public void run() throws IOException {
        FullKey key = FullKeyParse.parseFullKey(args.get(0));
        String path = args.get(1);

        ClearBlockSource source = new StorageClearBlockSource(store);
        RestoreEngine engine = new RestoreEngine(key);

        /*
         * do the work
         */
        while (true) {
            boolean progress = false;
            RandomOutputFile out = null;
            try {
                engine.start();
                while (engine.findReady()) {
                    Block block = source.get(engine.getKey());
                    if (block == null) {
                        engine.skip();
                        continue;
                    }

                    Fragment part = engine.step(block);
                    progress = true;

                    if (part != null) {
                        if (out == null) {
                            out = fs.openRandomOutputFile(path);
                        }
                        out.writeAt(part.getOffset(), part.getData()
                                .toByteArray());
                    }
                }
            } finally {
                if (out != null) {
                    out.close();
                    out = null;
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

    private WaitHandler waitHandler;
}
