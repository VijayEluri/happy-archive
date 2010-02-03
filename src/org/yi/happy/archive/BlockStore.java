package org.yi.happy.archive;

import java.io.IOException;

import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.key.LocatorKey;

public interface BlockStore {
    void put(EncodedBlock block) throws IOException;

    EncodedBlock get(LocatorKey key) throws IOException;

    boolean contains(LocatorKey key) throws IOException;
}
