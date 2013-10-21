package org.yi.happy.archive;

import java.io.IOException;

import org.yi.happy.archive.restore.RestoreEngine;

/**
 * The handler interface for {@link KeyInputStream} for handing the situation
 * where a block is not found in the store.
 */
public interface NotReadyHandler {

    /**
     * called when reader is not able to read the first available block.
     * 
     * @param reader
     *            the reader that is not ready
     * @param progress
     * @throws IOException
     *             if the not ready situation can not be overcome
     */
    void notReady(RestoreEngine reader, int progress) throws IOException;

}
