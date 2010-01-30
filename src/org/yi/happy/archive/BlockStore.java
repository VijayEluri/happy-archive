package org.yi.happy.archive;

import java.io.IOException;

public interface BlockStore {
    void put(EncodedBlock block) throws IOException;
}
