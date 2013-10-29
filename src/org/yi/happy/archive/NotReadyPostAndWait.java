package org.yi.happy.archive;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InterruptedIOException;

import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.restore.RestoreEngine;

/**
 * The file posting and waiting not ready handler.
 */
public class NotReadyPostAndWait implements NotReadyHandler {
    private final String needFile;
    private int delay = 1;
    private int lastDelay = 0;

    /**
     * Create the file posting and waiting not ready handler.
     * 
     * @param needFile
     *            the file name to post to.
     */
    public NotReadyPostAndWait(String needFile) {
        this.needFile = needFile;
    }

    @Override
    public void onNotReady(RestoreEngine engine, boolean progress)
            throws IOException {
        doPost(engine);
        doWait(progress);
    }

    private void doWait(boolean progress) throws IOException {
        if (progress) {
            delay = 1;
            lastDelay = 0;
        } else {
            int nextDelay = delay + lastDelay;
            lastDelay = delay;
            delay = nextDelay;
            if (delay > 60) {
                delay = 60;
            }
        }

        try {
            Thread.sleep(delay * 1000);
        } catch (InterruptedException e) {
            throw new InterruptedIOException();
        }
    }

    private void doPost(RestoreEngine engine) throws IOException {
        FileWriter out = new FileWriter(needFile + ".part");
        try {
            for (FullKey key : engine.getNeeded()) {
                out.append(key.toLocatorKey().toString()).append("\n");
            }
        } finally {
            out.close();
        }

        if (!new File(needFile + ".part").renameTo(new File(needFile))) {
            throw new IOException();
        }
    }

}
