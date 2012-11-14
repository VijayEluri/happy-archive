package org.yi.happy.archive;

import java.io.IOException;
import java.util.List;

import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.key.LocatorKey;

public class NeedWriter implements NeedHandler {

    private final String needFile;
    private final FileSystem fs;

    public NeedWriter(FileSystem fs, String needFile) {
        this.fs = fs;
        this.needFile = needFile;
    }

    @Override
    public void post(List<LocatorKey> keys) throws IOException {
        StringBuilder p = new StringBuilder();
        for (LocatorKey key : keys) {
            p.append(key).append("\n");
        }
        fs.save(needFile + ".tmp", ByteString.toUtf8(p.toString()));
        fs.rename(needFile + ".tmp", needFile);
    }

}
