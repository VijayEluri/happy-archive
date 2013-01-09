package org.yi.happy.archive;

import java.io.IOException;
import java.util.List;

import org.yi.happy.annotate.MagicLiteral;
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
        for (int i = 0; i < engine.getItemCount(); i++) {
            Fragment out = fetch(i);
            if (out != null) {
                return out;
            }
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
        return fetch(0);
    }

    /**
     * try to fetch the given entry in the pending list, if it can be fetched
     * return the details and remove it from the pending list. if the block
     * being fetched is an indirection then the pending list gets updated and
     * the attempt is repeated. If the offset of the entry in the pending list
     * is not known, it can not be fetched and null is returned.
     * 
     * @param index
     *            the index in the pending list to try and load
     * @return the loaded data for the given entry, or null if the block is not
     *         available.
     * @throws IOException
     *             if the block is available but fetching or decoding failed.
     */
    @MagicLiteral
    private Fragment fetch(int index) throws IOException {
        while (true) {
            if (engine.isOutputReady()) {
                return engine.getOutput();
            }

            if (index >= engine.getItemCount()) {
                return null;
            }

            if (engine.isReady(index) == false) {
                return null;
            }

            FullKey key = engine.getKey(index);
            Block b = storage.retrieveBlock(key);
            if (b == null) {
                return null;
            }

            try {
                if (engine.step(index, b)) {
                    progress++;
                }
            } catch (IllegalArgumentException e) {
                throw new DecodeException(e);
            }
        }
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
        return engine.getOffset(0);
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
