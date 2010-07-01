package org.yi.happy.archive.sandbox.search;

/**
 * The common interface for random input files.
 */
public interface RandomInputFile {
    /**
     * read some bytes from the file.
     * 
     * @param offset
     *            where to read from.
     * @param length
     *            how much to read.
     * @return the bytes read.
     */
    byte[] read(long offset, int length);
}
