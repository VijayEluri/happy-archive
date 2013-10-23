package org.yi.happy.archive.tag;

import java.io.IOException;
import java.util.List;

import org.yi.happy.archive.Fragment;
import org.yi.happy.archive.RetrieveBlock;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.file_system.RandomOutputFile;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.restore.RestoreEngine;

/**
 * An incremental file restore process.
 */
public class RestoreFile {
    private final RestoreEngine engine;
    private final RetrieveBlock store;
    private final String path;
    private final FileSystem fs;

    /**
     * the number of blocks read and processed.
     */
    private int progress = 0;

    /**
     * initialize an incremental file restore process.
     * 
     * @param key
     *            the key to the file.
     * @param store
     *            the block source.
     * @param path
     *            the output file name.
     * @param fs
     *            the file system to use.
     */
    public RestoreFile(FullKey key, RetrieveBlock store, String path,
            FileSystem fs) {
        this.engine = new RestoreEngine(key);
        this.store = store;
        this.path = path;
        this.fs = fs;
    }

    /**
     * performs a restore step: if there is data ready, write it out to the
     * named file.
     * 
     * @throws IOException
     */
    public void step() throws IOException {
        engine.start();
        Fragment part = null;
        while (engine.findReady()) {
            Block block = store.retrieveBlock(engine.getKey());
            if (block == null) {
                engine.skip();
                continue;
            }
            part = engine.step(block);
            progress++;
            if (part != null) {
                break;
            }
        }

        if (part == null) {
            return;
        }

        RandomOutputFile f = fs.openRandomOutputFile(path);
        try {
            while (true) {
                if (part != null) {
                    f.writeAt(part.getOffset(), part.getData().toByteArray());
                    part = null;
                }

                if (engine.findReady() == false) {
                    break;
                }

                Block block = store.retrieveBlock(engine.getKey());
                if (block == null) {
                    engine.skip();
                    continue;
                }

                part = engine.step(block);
                progress++;
            }
        } finally {
            f.close();
        }
    }

    /**
     * check if the process is complete.
     * 
     * @return true if there is no more reading to do.
     */
    public boolean isDone() {
        return engine.isDone();
    }

    /**
     * get the list of blocks that are needed at this time.
     * 
     * @return the full keys of the blocks that are needed at this time.
     */
    public List<FullKey> getPending() {
        return engine.getNeeded();
    }

    /**
     * Get the number of blocks read and processed.
     * 
     * @return the number of blocks read and processed.
     */
    public int getProgress() {
        return progress;
    }

}
