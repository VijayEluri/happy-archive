package org.yi.happy.archive;

import java.io.IOException;

public interface WaitHandler {
    void doWait(boolean progress) throws IOException;
}
