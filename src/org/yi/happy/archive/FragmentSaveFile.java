package org.yi.happy.archive;

import java.io.IOException;
import java.io.RandomAccessFile;

import org.yi.happy.annotate.GlobalFilesystem;

/**
 * Save fragments to real files.
 */
@GlobalFilesystem
public class FragmentSaveFile implements FragmentSave {
    private RandomAccessFile file = null;
    private String fileName = null;

    @Override
    public void save(String path, Fragment fragment) throws IOException {
        if(path == null) {
            throw new NullPointerException();
        }
        
        if (file != null && !fileName.equals(path)) {
            file.close();
            file = null;
            fileName = null;
        }

        if (file == null) {
            file = new RandomAccessFile(path, "rw");
            fileName = path;
        }

        file.seek(fragment.getOffset());
        file.write(fragment.getData().toByteArray());
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
