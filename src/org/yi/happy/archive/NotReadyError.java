package org.yi.happy.archive;

import java.io.IOException;

/**
 * a not ready handler that just throws an exception
 * 
 * @author sarah dot a dot happy at gmail dot com
 * 
 */
public class NotReadyError implements NotReadyHandler {

    public void notReady(SplitReader reader) throws IOException {
        throw new IONotReadyException();
    }

}
