package org.yi.happy.archive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.yi.happy.archive.key.LocatorKey;

/**
 * Capture the list of needed keys. This is for testing when the list of needed
 * keys is wanted.
 */
public class NeedCapture implements NeedHandler {
    private List<LocatorKey> keys = Collections.emptyList();

    @Override
    public void post(List<LocatorKey> keys) throws IOException {
        this.keys = new ArrayList<LocatorKey>(keys);
    }

    /**
     * @return the list of keys that were posted.
     */
    public List<LocatorKey> getKeys() {
        return keys;
    }
}
