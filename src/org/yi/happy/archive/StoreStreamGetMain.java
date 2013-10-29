package org.yi.happy.archive;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.yi.happy.annotate.RestoreLoop;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.commandLine.UsesArgs;
import org.yi.happy.archive.commandLine.UsesNeed;
import org.yi.happy.archive.commandLine.UsesOutput;
import org.yi.happy.archive.commandLine.UsesStore;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.FullKeyParse;
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
public class StoreStreamGetMain implements MainCommand {
    private final OutputStream out;
    private final ClearBlockSource source;
    private final List<String> args;
    private final NotReadyHandler notReady;

    /**
     * create.
     * 
     * @param source
     *            the clear block source to use.
     * @param out
     *            where to send the stream.
     * @param notReady
     *            what to do when no needed blocks are ready.
     * @param args
     *            the non-option command line arguments.
     */
    public StoreStreamGetMain(ClearBlockSource source, OutputStream out,
            NotReadyHandler notReady, List<String> args) {
        this.source = source;
        this.out = out;
        this.notReady = notReady;
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
        FullKey key = FullKeyParse.parseFullKey(args.get(0));
        RestoreEngine engine = new RestoreEngine(key);

        /*
         * do the work
         */
        FragmentOutputStream target = new FragmentOutputStream(out);
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

            notReady.onNotReady(engine, progress);
        }
    }
}
