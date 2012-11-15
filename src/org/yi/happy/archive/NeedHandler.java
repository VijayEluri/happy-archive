package org.yi.happy.archive;

import java.io.IOException;
import java.util.List;

import org.yi.happy.archive.key.LocatorKey;

/**
 * Handle requests for needed keys.
 */
public interface NeedHandler {
    /**
     * post a list of needed keys.
     * 
     * @param keys
     *            the keys that are needed.
     * @throws IOException
     */
    public void post(List<LocatorKey> keys) throws IOException;
}
