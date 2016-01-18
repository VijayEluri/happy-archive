package org.yi.happy.archive;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A {@link FileStore} in memory.
 */
public class FileStoreMemory implements FileStore {

    private Map<String, byte[]> files = new HashMap<String, byte[]>();

    private final byte[] DIR = new byte[0];

    /**
     * Create the fake file system, initially empty.
     */
    public FileStoreMemory() {
        files.put("", DIR);
    }

    @Override
    public byte[] get(String path) throws IOException {
        byte[] data = files.get(path);

        if (data == null || data == DIR) {
            throw new FileNotFoundException();
        }

        return data.clone();
    }

    @Override
    public byte[] get(String path, int limit) throws IOException {
        byte[] data = files.get(path);

        if (data == null || data == DIR) {
            throw new FileNotFoundException();
        }

        if (data.length > limit) {
            throw new IOException();
        }

        return data.clone();
    }

    @Override
    public InputStream getStream(String path) throws IOException {
        byte[] data = files.get(path);

        if (data == null || data == DIR) {
            throw new FileNotFoundException();
        }

        return new ByteArrayInputStream(data);
    }

    @Override
    public void put(String path, byte[] bytes) throws IOException {
        if (files.get(dirName(path)) != DIR || files.get(path) == DIR) {
            throw new FileNotFoundException();
        }

        files.put(path, bytes.clone());
    }

    @Override
    public boolean putDir(String path) throws IOException {
        if (files.get(dirName(path)) != DIR) {
            throw new FileNotFoundException();
        }
    
        byte[] cur = files.get(path);
        if (cur == DIR) {
            return false;
        }
    
        if (cur != null) {
            throw new IOException();
        }
    
        files.put(path, DIR);
        return true;
    }

    private String dirName(String path) {
        if (path.contains("/")) {
            return path.replaceAll("/[^/]*$", "");
        } else {
            return "";
        }
    }

    @Override
    public List<String> list(String path) throws IOException {
        if (!isDir(path)) {
            throw new FileNotFoundException();
        }

        if (!path.equals("")) {
            path = path + "/";
        }

        List<String> out = new ArrayList<String>();

        Pattern p = Pattern.compile(Pattern.quote(path) + "([^/]+)");
        for (String name : files.keySet()) {
            Matcher m = p.matcher(name);
            if (!m.matches()) {
                continue;
            }
            out.add(m.group(1));
        }

        return out;
    }

    @Override
    public boolean isFile(String path) {
        byte[] data = files.get(path);

        if (data == null || data == DIR) {
            return false;
        }

        return true;
    }

    @Override
    public boolean isDir(String path) {
        return files.get(path) == DIR;
    }
}
