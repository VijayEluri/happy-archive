package org.yi.happy.archive.file_system;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * A file system interface.
 */
public interface FileSystem {

    /**
     * load a file.
     * 
     * @param name
     *            the file to load.
     * @return the bytes of the file.
     * @throws IOException
     *             if the file can not be loaded.
     */
    byte[] load(String name) throws IOException;

    /**
     * open a file for reading as a stream.
     * 
     * @param name
     *            the file to open.
     * @return an open input stream.
     * @throws IOException
     *             if the file can not be opened.
     */
    InputStream openInputStream(String name) throws IOException;

    /**
     * load a file if it is smaller than limit.
     * 
     * @param name
     *            the name of the file to load.
     * @param limit
     *            the limit at which an error is raised.
     * @return the bytes of the file.
     * @throws IOException
     *             if the file can not be loaded, or is too large.
     */
    byte[] load(String name, int limit) throws IOException;

    /**
     * save a file.
     * 
     * @param name
     *            the name of the file to save.
     * @param bytes
     *            the bytes to save in the file.
     * @throws IOException
     *             on error.
     */
    void save(String name, byte[] bytes) throws IOException;

    /**
     * join two file names.
     * 
     * @param base
     *            the base file name.
     * @param name
     *            the file name to resolve from the base.
     * @return the joined file name.
     */
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

    /**
     * List the names in a directory.
     * 
     * @param path
     *            the directory to get the list of names from.
     * @return the list of names.
     * @throws IOException
     *             if path is not a directory.
     */
    List<String> list(String path) throws IOException;

    /**
     * check that a path exists and is a directory.
     * 
     * @param path
     *            the path to check.
     * @return true if the path exists and is a directory.
     */
    boolean isDir(String path);

    /**
     * check that a path exists and is a file.
     * 
     * @param path
     *            the path to check.
     * @return true if the path exists and is a file.
     */
    boolean isFile(String path);
}
