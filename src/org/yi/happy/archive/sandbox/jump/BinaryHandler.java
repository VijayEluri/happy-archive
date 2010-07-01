package org.yi.happy.archive.sandbox.jump;

/**
 * A binary parser event stream handler.
 */
public interface BinaryHandler {
    /**
     * signals the start of the stream.
     */
    public void startStream();

    /**
     * signals the end of the stream.
     */
    public void endStream();

    /**
     * signals the start of a region.
     * 
     * @param name
     *            the name of the region.
     */
    public void startRegion(String name);

    /**
     * signals the end of a region.
     * 
     * @param name
     *            the name of the region.
     */
    public void endRegion(String name);

    /**
     * signals some content bytes.
     * 
     * @param buff
     *            the byte buffer.
     * @param offset
     *            the offset where the content starts.
     * @param length
     *            the length of the content.
     */
    public void text(byte[] buff, int offset, int length);
}
