package org.yi.happy.archive.file_system;

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
 * A fake file system for testing. This should behave just like a real file
 * system given the same calls under the same conditions.
 */
public class FileSystemMemory implements FileSystem {

    private Map<String, byte[]> files = new HashMap<String, byte[]>();

    private final byte[] DIR = new byte[0];

    /**
     * Create the fake file system, initially empty.
     */
    public FileSystemMemory() {
        files.put("", DIR);
    }

    @Override
    public void save(String name, byte[] bytes) throws IOException {
        if (name.contains("/")
                && files.get(name.replaceAll("/[^/]*$", "")) != DIR) {
            throw new FileNotFoundException("parent does not exist or is"
                    + " not a folder");
        }

        if (files.get(name) == DIR) {
            throw new IOException();
        }

        files.put(name, bytes.clone());
    }

    @Override
    public byte[] load(String name) throws IOException {
        byte[] data = files.get(name);

        if (data == null) {
            throw new FileNotFoundException();
        }

        return data.clone();
    }

    @Override
    public byte[] load(String name, int limit) throws IOException {
        byte[] data = files.get(name);

        if (data == null) {
            throw new FileNotFoundException();
        }

        if (data.length > limit) {
            throw new IOException();
        }

        return data.clone();
    }

    @Override
    public InputStream openInputStream(String name) throws IOException {
        byte[] data = files.get(name);
        if (data == null) {
            throw new FileNotFoundException();
        }
        return new ByteArrayInputStream(data);
    }

    @Override
    public String join(String base, String name) {
        return base + "/" + name;
    }

    @Override
    public boolean mkdir(String path) throws IOException {
        /*
         * TODO extract parent check to a method
         */
        if (path.contains("/")
                && files.get(path.replaceAll("/[^/]*$", "")) != DIR) {
            throw new FileNotFoundException(
                    "parent does not exist or is not a folder");
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

    @Override
    public List<String> list(String path) throws IOException {
        if (!isDir(path)) {
            throw new IOException();
        }

        List<String> out = new ArrayList<String>();

        if (path.equals(".")) {
            path = "";
        }
        if (path.length() > 0 && !path.endsWith("/")) {
            path = path + "/";
        }

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
    public boolean isDir(String path) {
        if (path.equals(".") || path.equals("/")) {
            return true;
        }
        if (path.length() > 1 && path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return files.get(path) == DIR;
    }

    @Override
    public boolean isFile(String path) {
        byte[] data = files.get(path);

        if (data == null) {
            return false;
        }
        if (data == DIR) {
            return false;
        }

        return true;
    }
}
