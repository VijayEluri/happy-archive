package org.yi.happy.archive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.yi.happy.archive.key.LocatorKey;

public class NeedCapture implements NeedHandler {
    private List<LocatorKey> keys;

    @Override
    public void post(List<LocatorKey> keys) throws IOException {
        this.keys = new ArrayList<LocatorKey>(keys);
    }

    public List<LocatorKey> getKeys() {
        return keys;
    }
}
