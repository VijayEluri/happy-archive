package org.yi.happy.archive.sandbox.interpret;

public class OnStartStream implements OnCondition {

    @Override
    public boolean startStream() {
        return true;
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
        return false;
    }

    @Override
    public boolean endRegion(String name) {
        return false;
    }

}
