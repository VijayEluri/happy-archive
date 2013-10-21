package org.yi.happy.archive;

import java.io.IOException;
import java.util.List;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.restore.RestoreEngine;

/**
 * fetches the blocks for a split key of various kinds.
 */
public class SplitReader {

    private int progress;

    private final RestoreEngine engine;

    /**
     * create to read fullKey finding blocks in storage
     * 
     * @param fullKey
     *            the key to read
     * @param storage
     *            where to find the blocks
     */
    public SplitReader(FullKey fullKey, RetrieveBlock storage) {
        this.engine = new RestoreEngine(fullKey);

        this.storage = storage;
    }

    /**
     * fetch any data block that is ready.
     * 
     * @return the offset to the data and the clear data, or null if none is
     *         ready.
     * @throws IOException
     *             if the block is available but fetching or decoding failed.
     */
    public Fragment fetchAny() throws IOException {
        engine.start();
        while (engine.findReady()) {
            Block block = storage.retrieveBlock(engine.getKey());
            if (block == null) {
                engine.skip();
                continue;
            }

            Fragment out;
            try {
                out = engine.step(block);
            } catch (IllegalArgumentException e) {
                throw new DecodeException(e);
            }
            progress++;
            if (out == null) {
                continue;
            }

            return out;
        }

        return null;
    }

    /**
     * get the first data block.
     * 
     * @return the offset and the clear data, or null if none is ready.
     * @throws IOException
     *             if the block is available but fetching or decoding failed.
     */
    public Fragment fetchFirst() throws IOException {
        engine.start();
        while (engine.findReady()) {
            Block block = storage.retrieveBlock(engine.getKey());
            if (block == null) {
                break;
            }

            Fragment out;
            try {
                out = engine.step(block);
            } catch (IllegalArgumentException e) {
                throw new DecodeException(e);
            }
            progress++;
            if (out == null) {
                continue;
            }

            return out;
        }

        return null;
    }

    /**
     * get the list of blocks that are needed at this time. a later call to this
     * may return more blocks in the list.
     * 
     * @return the list of full keys for blocks that are needed.
     */
    public List<FullKey> getPending() {
        return engine.getNeeded();
    }

    /**
     * is the reader out of blocks to read
     * 
     * @return true if there are no more pending blocks.
     */
    public boolean isDone() {
        return engine.isDone();
    }

    private RetrieveBlock storage;

    /**
     * get the offset where the first entry in the pending list will go
     * 
     * @return the next offset, or null if done
     */
    public Long getOffset() {
        if (engine.isDone()) {
            return null;
        }
        engine.start();
        return engine.getOffset();
    }

    /**
     * get the progress counter. At this time the return value of this is
     * different if any progress was made reading blocks since the last time
     * this was called. The initial value is zero.
     * 
     * @return the progress counter, which at the moment is the number of blocks
     *         successfully retrieved from storage.
     */
    public int getProgress() {
        return progress;
    }
}
