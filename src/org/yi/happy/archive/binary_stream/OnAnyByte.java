package org.yi.happy.archive.binary_stream;

/**
 * This condition matches any data event.
 */
public class OnAnyByte implements OnCondition {

    @Override
    public boolean startStream() {
        return false;
    }

    @Override
    public boolean endStream() {
        return false;
    }

    @Override
    public boolean data(byte b) {
        return true;
    }

    @Override
    public boolean startRegion(String name) {
        return false;
    }

    @Override
    public boolean endRegion(String name) {
        return false;
    }

}
