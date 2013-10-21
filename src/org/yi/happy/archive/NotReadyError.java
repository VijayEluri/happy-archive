package org.yi.happy.archive;

import java.io.IOException;

import org.yi.happy.archive.restore.RestoreEngine;

/**
 * a not ready handler that just throws an exception
 */
public class NotReadyError implements NotReadyHandler {

    @Override
    public void notReady(RestoreEngine reader, int progress) throws IOException {
        throw new IONotReadyException();
    }

}
