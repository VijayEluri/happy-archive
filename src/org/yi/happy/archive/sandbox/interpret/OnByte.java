package org.yi.happy.archive.sandbox.interpret;

public class OnByte implements OnCondition {
    private final byte value;

    public OnByte(byte value) {
        this.value = value;
    }

    public OnByte(char c) {
        this.value = (byte) c;
    }

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
        return b == value;
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
