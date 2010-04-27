package org.yi.happy.archive.tag;

import java.io.IOException;
import java.util.List;

import org.yi.happy.archive.Fragment;
import org.yi.happy.archive.SplitReader;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.file_system.RandomOutputFile;
import org.yi.happy.archive.key.FullKey;

/**
 * An incremental file restore process.
 */
public class RestoreFile {
    private final SplitReader data;
    private final String path;
    private final FileSystem fs;

    /**
     * initialize an incremental file restore process.
     * 
     * @param data
     *            the block source.
     * @param path
     *            the output file name.
     * @param fs
     *            the file system to use.
     */
    public RestoreFile(SplitReader data, String path, FileSystem fs) {
        this.data = data;
        this.path = path;
        this.fs = fs;
    }

    /**
     * performs a restore step: if there is data ready, write it out to the
     * named file.
     * 
     * @throws IOException
     */
    public void step() throws IOException {
        Fragment part = data.fetchAny();
        if (part == null) {
            return;
        }

        RandomOutputFile f = fs.openRandomOutputFile(path);
        try {
            while (part != null) {
                f.writeAt(part.getOffset(), part.getData().toByteArray());

                part = data.fetchAny();
            }
        } finally {
            f.close();
        }
    }

    /**
     * check if the process is complete.
     * 
     * @return true if there is no more reading to do.
     */
    public boolean isDone() {
        return data.isDone();
    }

    /**
     * get the list of blocks that are needed at this time.
     * 
     * @return the full keys of the blocks that are needed at this time.
     */
    public List<FullKey> getPending() {
        return data.getPending();
    }

    /**
     * Get the amount of progress that has been made.
     * 
     * @return the amount of progress that has been made.
     */
    public int getProgress() {
        return data.getProgress();
    }

}
