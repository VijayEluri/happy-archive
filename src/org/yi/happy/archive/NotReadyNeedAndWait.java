package org.yi.happy.archive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.LocatorKey;
import org.yi.happy.archive.restore.RestoreEngine;

/**
 * A {@link NotReadyHandler} that uses a {@link NeedHandler} and a
 * {@link WaitHandler}.
 */
public class NotReadyNeedAndWait implements NotReadyHandler {
    private final NeedHandler needHandler;
    private final WaitHandler waitHandler;

    /**
     * Create.
     * 
     * @param needHandler
     *            the {@link NeedHandler} to call.
     * @param waitHandler
     *            the {@link WaitHandler} to call.
     */
    public NotReadyNeedAndWait(NeedHandler needHandler, WaitHandler waitHandler) {
        this.needHandler = needHandler;
        this.waitHandler = waitHandler;
    }

    @Override
    public void onNotReady(RestoreEngine engine, boolean progress)
            throws IOException {
        List<LocatorKey> keys = new ArrayList<LocatorKey>();
        for (FullKey key : engine.getNeeded()) {
            keys.add(key.toLocatorKey());
        }
        needHandler.post(keys);

        waitHandler.doWait(progress);
    }
}
