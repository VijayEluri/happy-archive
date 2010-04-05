package org.yi.happy.archive;

import java.io.IOException;
import java.util.List;

import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.key.FullKey;

/**
 * Utility methods for dealing with a pending list.
 */
public class PendingList {

    /**
     * Save a list of pending keys to a named file in the file system, using a
     * temporary file while the file is being written.
     * 
     * @param pending
     *            the list of pending keys.
     * @param fs
     *            the file system where the named file is.
     * @param pendingFile
     *            the name of the file.
     * @throws IOException
     *             on error.
     */
    public static void savePendingListFile(List<FullKey> pending,
            FileSystem fs, String pendingFile) throws IOException {
        StringBuilder p = new StringBuilder();
        for (FullKey k : pending) {
            p.append(k.toLocatorKey() + "\n");
        }
        fs.save(pendingFile + ".tmp", ByteString.toUtf8(p.toString()));
        fs.rename(pendingFile + ".tmp", pendingFile);
    }

}
