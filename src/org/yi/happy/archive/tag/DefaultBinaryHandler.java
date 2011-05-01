package org.yi.happy.archive.tag;

/**
 * A binary stream handler that does nothing.
 */
public class DefaultBinaryHandler implements BinaryHandler {

    @Override
    public void startStream() {
    }

    @Override
    public void startRegion(String name) {
    }

    @Override
    public void bytes(byte[] buff, int offset, int length) {
    }

    @Override
    public void endRegion(String name) {
    }

    @Override
    public void endStream() {
    }

}
