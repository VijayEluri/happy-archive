package org.yi.happy.archive.tag;

import java.io.IOException;
import java.util.List;

import org.yi.happy.archive.Fragment;
import org.yi.happy.archive.ClearBlockSource;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.file_system.RandomOutputFile;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.restore.RestoreEngine;

/**
 * A management process for multiple files being restored.
 */
public class RestoreManager {
    private final RestoreEngine engine;
    private final FileSystem fs;
    private final ClearBlockSource store;

    /**
     * the number of blocks read and processed.
     */
    private int progress = 0;

    /**
     * initialize.
     * 
     * @param fs
     *            the file system to use.
     * @param store
     *            the block store to use.
     */
    public RestoreManager(FileSystem fs, ClearBlockSource store) {
        this.fs = fs;
        this.store = store;
        this.engine = new RestoreEngine();
    }

    /**
     * add a file to the set of files being restored.
     * 
     * @param path
     *            the path to the file to restore.
     * @param fullKey
     *            the key to store there.
     */
    public void addFile(String path, FullKey fullKey) {
        engine.add(path, fullKey);
    }

    /**
     * do a processing step.
     * 
     * @throws IOException
     */
    public void step() throws IOException {
        RandomOutputFile out = null;
        String path = null;
        try {
            engine.start();
            while (engine.findReady()) {
                Block block = store.get(engine.getKey());
                if (block == null) {
                    engine.skip();
                    continue;
                }
                Fragment part = engine.step(block);
                progress++;
                if (part != null) {
                    if (out != null && !path.equals(engine.getJobName())) {
                        out.close();
                        out = null;
                    }
                    if (out == null) {
                        path = engine.getJobName();
                        out = fs.openRandomOutputFile(path);
                    }
                    out.writeAt(part.getOffset(), part.getData().toByteArray());
                }
            }
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * get the number of blocks read and processed.
     * 
     * @return the number of blocks read and processed.
     */
    public int getProgress() {
        return progress;
    }

    /**
     * get the keys for the blocks that are known to be needed.
     * 
     * @return a list of the needed blocks.
     */
    public List<FullKey> getPending() {
        return engine.getNeeded();
    }

    /**
     * check if the restore is done.
     * 
     * @return true if the restore is done.
     */
    public boolean isDone() {
        return engine.isDone();
    }
}
