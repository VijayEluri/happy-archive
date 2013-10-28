package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;

import org.yi.happy.annotate.SmellsMessy;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.restore.RestoreEngine;

/**
 * an input stream for reading keys from a store.
 * 
 * @author sarah dot a dot happy at gmail dot com
 * 
 */
public class KeyInputStream extends InputStream {

    /**
     * the split reader that does most of the work
     */
    private RestoreEngine reader;

    /**
     * the decoding block store.
     */
    private ClearBlockSource store;

    /**
     * how much of the stream has been read
     */
    private long offset;

    /**
     * the number of blocks read and processed.
     */
    private int progress;

    /**
     * the current data fragment
     */
    private Fragment buff;

    /**
     * notified when there is no block ready to read, to defer the decision of
     * what to do to the client of this class.
     */
    private final NotReadyHandler notReadyHandler;

    /**
     * configure for reading
     * 
     * @param fullKey
     *            the key to read
     * @param store
     *            the store to get blocks from
     * @param notReadyHandler
     *            notified when a block is needed and not found in the store
     */
    public KeyInputStream(FullKey fullKey, ClearBlockSource store,
            NotReadyHandler notReadyHandler) {
        this.reader = new RestoreEngine(fullKey);
        this.store = store;
        this.notReadyHandler = notReadyHandler;
        this.offset = 0;
    }

    /**
     * read a byte from the stream. in the case of overlap the overlapping area
     * is skipped.
     */
    @Override
    @SmellsMessy
    public int read() throws IOException {
        while (true) {
            /*
             * if the buffer is before the current offset, drop it
             */
            if (buff != null && buff.getOffset() + buff.getSize() <= offset) {
                buff = null;
            }

            if (buff != null) {
                if (buff.getOffset() > offset) {
                    return 0;
                }

                int out = buff.getAbsolute(offset) & 0xff;
                offset++;

                return out;
            }

            if (reader.isDone()) {
                return -1;
            }

            if (reader.getOffset() > offset) {
                offset++;
                return 0;
            }

            try {
                while (reader.findReady()) {
                    Block block = store.get(reader.getKey());
                    if (block == null) {
                        break;
                    }
                    buff = reader.step(block);
                    progress++;
                    if (buff != null) {
                        break;
                    }
                }
            } catch (IOException e) {
                buff = null;
            }
            if (buff == null) {
                notReadyHandler.notReady(reader, progress);
            }
        }
    }
}
