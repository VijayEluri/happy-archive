package org.yi.happy.archive.tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.yi.happy.archive.RetrieveBlock;
import org.yi.happy.archive.SplitReader;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.key.FullKey;

/**
 * A management process for multiple files being restored.
 */
public class RestoreManager {

    private List<RestoreFile> files;
    private final FileSystem fs;
    private final RetrieveBlock store;

    /**
     * initialize.
     * 
     * @param fs
     *            the file system to use.
     * @param store
     *            the block store to use.
     */
    public RestoreManager(FileSystem fs, RetrieveBlock store) {
        this.fs = fs;
        this.store = store;
        files = new ArrayList<RestoreFile>();
    }

    /**
     * add a file to the set of files being restored.
     * 
     * @param path
     *            the path to the file to restore.
     * @param fullKey
     *            the key to store there.
     */
    public void addFile(String path, FullKey fullKey) {
        files.add(new RestoreFile(new SplitReader(fullKey, store), path, fs));
    }

    /**
     * do a processing step.
     * 
     * @throws IOException
     */
    public void step() throws IOException {
        for (RestoreFile f : files) {
            f.step();
        }
    }

    /**
     * get the progress so far.
     * 
     * @return the progress so far.
     */
    public int getProgress() {
        int progress = 0;
        for (RestoreFile f : files) {
            progress += f.getProgress();
        }
        return progress;
    }

    /**
     * get the keys for the blocks that are known to be needed.
     * 
     * @return a list of the needed blocks.
     */
    public List<FullKey> getPending() {
        List<FullKey> out = new ArrayList<FullKey>();
        Set<FullKey> seen = new HashSet<FullKey>();
        for (RestoreFile f : files) {
            for (FullKey k : f.getPending()) {
                if (seen.contains(k)) {
                    continue;
                }
                out.add(k);
                seen.add(k);
            }
        }
        return out;
    }

    /**
     * check if the restore is done.
     * 
     * @return true if the restore is done.
     */
    public boolean isDone() {
        for (RestoreFile f : files) {
            if (!f.isDone()) {
                return false;
            }
        }
        return true;
    }

}
