package org.yi.happy.archive.sandbox.interpret;

/**
 * A condition that matches the endStream event.
 */
public class OnEndStream implements OnCondition {

    @Override
    public boolean startStream() {
        return false;
    }

    @Override
    public boolean endStream() {
        return true;
    }

    @Override
    public boolean data(byte b) {
        return false;
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
