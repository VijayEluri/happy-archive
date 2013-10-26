package org.yi.happy.archive;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.yi.happy.annotate.GlobalFilesystem;
import org.yi.happy.archive.key.LocatorKey;

/**
 * A need handler that posts the needed keys to a file.
 */
@GlobalFilesystem
public class NeedWriter implements NeedHandler {
    private final String needFile;

    /**
     * Create the file posting need handler.
     * 
     * @param fs
     *            the file system to use.
     * @param needFile
     *            the file name to post to.
     */
    public NeedWriter(String needFile) {
        this.needFile = needFile;
    }

    @Override
    public void post(List<LocatorKey> keys) throws IOException {
        FileWriter out = new FileWriter(needFile + ".part");
        try {
            for (LocatorKey key : keys) {
                out.append(key.toString()).append("\n");
            }
        } finally {
            out.close();
        }
        new File(needFile + ".part").renameTo(new File(needFile));
    }
}
