package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.google.inject.ImplementedBy;

/**
 * A file storage service. The file paths components are '/' separated.
 */
@ImplementedBy(FileStoreFile.class)
public interface FileStore {
    /**
     * The path component separator.
     */
    String SEPARATOR = "/";

    /**
     * load a file.
     * 
     * @param path
     *            the file to load.
     * @return the bytes of the file.
     * @throws IOException
     *             if the file can not be loaded.
     */
    byte[] get(String path) throws IOException;

    /**
     * load a file if it is smaller than limit.
     * 
     * @param path
     *            the name of the file to load.
     * @param limit
     *            the limit at which an error is raised.
     * @return the bytes of the file.
     * @throws IOException
     *             if the file can not be loaded, or is too large.
     */
    byte[] get(String path, int limit) throws IOException;

    /**
     * open a file for reading as a stream.
     * 
     * @param path
     *            the file to open.
     * @return an open input stream.
     * @throws IOException
     *             if the file can not be opened.
     */
    InputStream getStream(String path) throws IOException;

    /**
     * save a file.
     * 
     * @param path
     *            the name of the file to save.
     * @param content
     *            the bytes to save in the file.
     * @throws IOException
     *             on error.
     */
    void put(String path, byte[] content) throws IOException;

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
    boolean putDir(String path) throws IOException;

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
     * check that a path exists and is a file.
     * 
     * @param path
     *            the path to check.
     * @return true if the path exists and is a file.
     */
    boolean isFile(String path);

    /**
     * check that a path exists and is a directory.
     * 
     * @param path
     *            the path to check.
     * @return true if the path exists and is a directory.
     */
    boolean isDir(String path);
}
