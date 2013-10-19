package org.yi.happy.archive.binary_stream;

/**
 * A condition that matches any start region.
 */
public class OnStartAnyRegion implements OnCondition {

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
        return false;
    }

    @Override
    public boolean startRegion(String name) {
        return true;
    }

    @Override
    public boolean endRegion(String name) {
        return false;
    }

}
