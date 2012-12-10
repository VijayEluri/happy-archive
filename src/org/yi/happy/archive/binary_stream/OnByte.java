package org.yi.happy.archive.binary_stream;

/**
 * This condition matches a specific byte event.
 */
public class OnByte implements OnCondition {
    private final byte value;

    /**
     * Create a condition that matches a specific data event.
     * 
     * @param value
     *            the data value to match.
     */
    public OnByte(byte value) {
        this.value = value;
    }

    /**
     * Create a condition that matches a specific data event.
     * 
     * @param c
     *            the data value to match (the low 8 bits are used).
     */
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
