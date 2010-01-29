package org.yi.happy.archive.file_system;

import java.io.IOException;
import java.io.InputStream;

public interface FileSystem {

    byte[] load(String name) throws IOException;

    InputStream openInputStream(String name) throws IOException;

    byte[] load(String name, int limit) throws IOException;

    void save(String name, byte[] bytes) throws IOException;

    String join(String base, String name);

    /**
     * ensure that a directory exists.
     * 
     * @param path
     *            the directory to create.
     * @return true if the directory was created, false if the directory already
     *         exists.
     * @throws IOException
     *             if the directory can not be created.
     */
    boolean mkdir(String path) throws IOException;
}
