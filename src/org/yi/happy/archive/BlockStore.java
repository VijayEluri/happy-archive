package org.yi.happy.archive;

import java.io.IOException;

import org.yi.happy.archive.block.EncodedBlock;

public interface BlockStore {
    void put(EncodedBlock block) throws IOException;
}
