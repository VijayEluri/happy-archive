package org.yi.happy.archive;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.yi.happy.annotate.GlobalFilesystem;

/**
 * A {@link FileStore} in the global file system.
 */
@GlobalFilesystem
public class FileStoreFile implements FileStore {

    @Override
    public byte[] get(String name) throws IOException {
        FileInputStream in = new FileInputStream(name);
        try {
            return Streams.load(in);
        } finally {
            in.close();
        }
    }

    @Override
    public byte[] get(String name, int limit) throws IOException {
        FileInputStream in = new FileInputStream(name);
        try {
            return Streams.load(in, limit);
        } finally {
            in.close();
        }
    }

    @Override
    public InputStream getStream(String name) throws IOException {
        return new FileInputStream(name);
    }

    @Override
    public void put(String name, byte[] bytes) throws IOException {
        FileOutputStream out = new FileOutputStream(name);
        try {
            out.write(bytes);
        } finally {
            out.close();
        }
    }

    @Override
    public boolean putDir(String path) throws IOException {
        File f = new File(path);
    
        if (f.mkdir()) {
            return true;
        }
    
        if (f.isDirectory()) {
            return false;
        }
    
        throw new IOException();
    }

    @Override
    public List<String> list(String path) throws IOException {
        String[] names = new File(path).list();
        if (names == null) {
            throw new FileNotFoundException();
        }
        return Arrays.asList(names);
    }

    @Override
    public boolean isFile(String path) {
        return new File(path).isFile();
    }

    @Override
    public boolean isDir(String path) {
        return new File(path).isDirectory();
    }
}
