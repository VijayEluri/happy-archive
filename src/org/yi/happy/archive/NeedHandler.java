package org.yi.happy.archive;

import java.io.IOException;
import java.util.List;

import org.yi.happy.archive.key.LocatorKey;

public interface NeedHandler {
    public void post(List<LocatorKey> keys) throws IOException;
}
