package org.yi.happy.archive;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.file_system.RandomOutputFile;

/**
 * Save fragments using the {@link FileSystem} interface.
 */
public class FragmentSaveFileSystem implements FragmentSave {
    private final FileSystem fs;

    /**
     * Set up.
     * 
     * @param fs
     *            the {@link FileSystem} interface.
     */
    public FragmentSaveFileSystem(FileSystem fs) {
        this.fs = fs;
    }

    private String fileName = null;
    private RandomOutputFile file = null;

    @Override
    public void save(String path, Fragment fragment) throws IOException {
        if (path == null) {
            throw new NullPointerException();
        }

        if (file != null && !fileName.equals(path)) {
            file.close();
            file = null;
            fileName = null;
        }

        if (file == null) {
            try {
                file = fs.openRandomOutputFile(path);
            } catch (FileNotFoundException e) {
                // attempt recovery
                fs.mkparentdir(path);
                file = fs.openRandomOutputFile(path);
            }

            fileName = path;
        }

        file.writeAt(fragment.getOffset(), fragment.getData().toByteArray());
    }

    @Override
    public void close() throws IOException {
        if (file != null) {
            file.close();
            file = null;
            fileName = null;
        }
    }
}
