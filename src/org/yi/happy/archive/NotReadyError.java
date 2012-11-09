package org.yi.happy.archive;

import java.io.IOException;

/**
 * a not ready handler that just throws an exception
 */
public class NotReadyError implements NotReadyHandler {

    @Override
    public void notReady(SplitReader reader) throws IOException {
        throw new IONotReadyException();
    }

}
