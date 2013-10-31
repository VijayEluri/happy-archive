package org.yi.happy.archive;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.yi.happy.archive.file_system.FileStore;

/**
 * An {@link IndexStore} in a {@link FileStore}.
 */
public class IndexStoreFileStore implements IndexStore {

    private final FileStore fs;
    private final String base;

    /**
     * Set up.
     * 
     * @param fs
     *            the {@link FileStore} where the indexes are.
     * @param base
     *            the base file name.
     */
    public IndexStoreFileStore(FileStore fs, String base) {
        this.fs = fs;
        this.base = base;
    }

    @Override
    public List<String> listVolumeSets() throws IOException {
        List<String> out = new ArrayList<String>();
        if (fs.isDir(base)) {
            for (String name : fs.list(base)) {
                if (fs.isDir(base + "/" + name)) {
                    out.add(name);
                }
            }
        }
        Collections.sort(out);
        return out;
    }

    @Override
    public List<String> listVolumeNames(String volumeSet) throws IOException {
        List<String> out = new ArrayList<String>();
        String path = base + "/" + volumeSet;
        if (fs.isDir(path)) {
            for (String name : fs.list(path)) {
                if (fs.isFile(path + "/" + name)) {
                    if (name.endsWith(".gz")) {
                        name = name.substring(0, name.length() - 3);
                    }
                    out.add(name);
                }
            }
        }
        Collections.sort(out);
        return out;
    }

    @Override
    public Reader open(String volumeSet, String volumeName) throws IOException {
        String name = base + "/" + volumeSet + "/" + volumeName;
        InputStream in = null;
        try {
            InputStream stream = in;
            try {
                in = fs.getStream(name);
                stream = in;
            } catch (FileNotFoundException e) {
                in = fs.getStream(name + ".gz");
                stream = new GZIPInputStream(in);
            }

            Reader reader = new InputStreamReader(stream, "utf-8");

            in = null;
            return reader;
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
}
