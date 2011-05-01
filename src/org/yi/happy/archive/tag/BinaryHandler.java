package org.yi.happy.archive.tag;

/**
 * Event parsing for binary data.
 */
public interface BinaryHandler {
    /**
     * Signal the start of the binary stream.
     */
    void startStream();

    /**
     * Signal the start of a region in the stream.
     * 
     * @param name
     *            the name of the region.
     */
    void startRegion(String name);

    /**
     * Binary data in the stream.
     * 
     * @param buff
     *            the data buffer.
     * @param offset
     *            the offset in the buffer where the data is.
     * @param length
     *            the length of the data in the buffer.
     */
    void bytes(byte[] buff, int offset, int length);

    /**
     * Signal the end of a region in the stream.
     * 
     * @param name
     *            the name of the region.
     */
    void endRegion(String name);

    /**
     * Signal the end of the binary stream.
     */
    void endStream();
}
