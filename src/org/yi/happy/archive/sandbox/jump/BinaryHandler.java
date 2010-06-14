package org.yi.happy.archive.sandbox.jump;

/**
 * A binary parser event stream handler.
 */
public interface BinaryHandler {
    public void startStream();

    public void endStream();

    public void startRegion(String name);

    public void endRegion(String name);

    public void text(byte[] buff, int offset, int length);
}
