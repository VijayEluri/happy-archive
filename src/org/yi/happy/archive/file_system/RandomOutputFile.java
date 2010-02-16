package org.yi.happy.archive.file_system;

import java.io.IOException;

/**
 * A file that may be read and written at arbitrary positions within the byte
 * stream.
 */
public interface RandomOutputFile {
    /**
     * write some bytes at the current location.
     * 
     * @param b
     *            the byte buffer.
     * @param offset
     *            where in the byte buffer to start.
     * @param length
     *            how much of the byte buffer to write.
     * @throws IOException
     *             on error.
     */
    void write(byte[] b, int offset, int length) throws IOException;

    /**
     * write soem bytes at the current location.
     * 
     * @param b
     *            the bytes to write.
     * @throws IOException
     *             on error.
     */
    void write(byte[] b) throws IOException;

    /**
     * write a single byte at the current location.
     * 
     * @param b
     *            the byte to write.
     * @throws IOException
     *             on error.
     */
    void write(int b) throws IOException;

    /**
     * close the file.
     * 
     * @throws IOException
     *             on error.
     */
    void close() throws IOException;

    /**
     * get the current position in the file.
     * 
     * @return the current position in the file.
     * @throws IOException
     *             on error.
     */
    long getPosition() throws IOException;

    /**
     * set the position in the file.
     * 
     * @param position
     *            the current position in the file.
     * @throws IOException
     *             on error.
     */
    void setPosition(long position) throws IOException;
}
