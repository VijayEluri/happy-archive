package org.yi.happy.archive.index;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.yi.happy.archive.FileStore;


/**
 * An {@link IndexStore} in a {@link FileStore}.
 */
public class IndexStoreFileStore implements IndexStore {

    private final FileStore files;
    private final String base;

    /**
     * Set up.
     * 
     * @param files
     *            the {@link FileStore} where the indexes are.
     * @param base
     *            the base file name.
     */
    public IndexStoreFileStore(FileStore files, String base) {
        this.files = files;
        this.base = base;
    }

    @Override
    public List<String> listVolumeSets() throws IOException {
        List<String> out = new ArrayList<String>();
        if (files.isDir(base)) {
            for (String name : files.list(base)) {
                if (files.isDir(base + "/" + name)) {
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
        if (files.isDir(path)) {
            for (String name : files.list(path)) {
                if (files.isFile(path + "/" + name)) {
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
                in = files.getStream(name);
                stream = in;
            } catch (FileNotFoundException e) {
                in = files.getStream(name + ".gz");
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
