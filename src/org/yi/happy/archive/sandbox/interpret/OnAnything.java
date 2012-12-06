package org.yi.happy.archive.sandbox.interpret;

/**
 * This condition matches any event.
 */
public class OnAnything implements OnCondition {

    @Override
    public boolean startStream() {
        return true;
    }

    @Override
    public boolean endStream() {
        return true;
    }

    @Override
    public boolean data(byte b) {
        return true;
    }

    @Override
    public boolean startRegion(String name) {
        return true;
    }

    @Override
    public boolean endRegion(String name) {
        return true;
    }
}
