package org.yi.happy.archive;

import java.io.IOException;

import org.yi.happy.archive.restore.RestoreEngine;

/**
 * Handler for when the immediately available blocks have been processed.
 */
public interface NotReadyHandler {
    /**
     * called when the immediately available blocks have been processed.
     * 
     * @param engine
     *            the current state of the restore.
     * @param progress
     *            true if any blocks have been processed since the last time
     *            this was called.
     * @throws IOException
     */
    public void onNotReady(RestoreEngine engine, boolean progress)
            throws IOException;
}
