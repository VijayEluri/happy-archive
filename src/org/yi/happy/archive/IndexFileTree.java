package org.yi.happy.archive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.yi.happy.archive.file_system.FileSystem;

/**
 * A visitable index file tree.
 */
public class IndexFileTree {
    /**
     * The interface for a visitor to this tree.
     */
    public interface Visitor {
        /**
         * Visit a file in the index file tree.
         * 
         * @param fs
         *            the file system with the file
         * @param fileName
         *            the name of the file
         * @param volumeSet
         *            the volume set for the volume
         * @param volumeName
         *            the name of the volume
         * @throws IOException
         *             if there is an error dealing with the file
         */
        public void visit(FileSystem fs, String fileName, String volumeSet,
                String volumeName) throws IOException;
    }

    /**
     * Call back to the visitor for each index file in the tree.
     * 
     * @param v
     * @throws IOException
     */
    public void accept(Visitor v) throws IOException {
        List<String> volumeSets = new ArrayList<String>(fs.list(path));
        Collections.sort(volumeSets);
        for (String volumeSet : volumeSets) {
            if (!fs.isDir(fs.join(path, volumeSet))) {
                continue;
            }
            List<String> volumeNames = new ArrayList<String>(fs.list(fs.join(
                    path, volumeSet)));
            Collections.sort(volumeNames);
            for (String volumeName : volumeNames) {
                if (volumeName.startsWith(".")) {
                    continue;
                }
                String fileName = fs.join(fs.join(path, volumeSet),
                        volumeName);

                /*
                 * for compressed volume indexes, strip the .gz off the name of
                 * the volume, and leave it on the file name.
                 */
                if (volumeName.endsWith(".gz")) {
                    volumeName = volumeName.substring(0,
                            volumeName.length() - 3);
                }

                v.visit(fs, fileName, volumeSet, volumeName);
            }
        }
    }

    private final FileSystem fs;
    private final String path;

    /**
     * set up an index file tree scanner.
     * 
     * @param fs
     *            the file system
     * @param path
     *            the path to the index file tree.
     */
    public IndexFileTree(FileSystem fs, String path) {
        this.fs = fs;
        this.path = path;
    }

}
