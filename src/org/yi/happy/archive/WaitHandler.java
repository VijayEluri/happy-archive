package org.yi.happy.archive;

import java.io.IOException;

/**
 * Strategy for when no blocks are ready and waiting is required.
 */
public interface WaitHandler {
    /**
     * called when no blocks are ready.
     * 
     * @param progress
     *            if there was progress since the last call.
     * @throws IOException
     */
    void doWait(boolean progress) throws IOException;
}
