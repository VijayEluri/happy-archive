package org.yi.happy.archive.key;

import org.yi.happy.archive.Base16;
import org.yi.happy.archive.Bytes;

/**
 * The binary password value part of content keys.
 */
public final class PassValue {
    private final Bytes value;

    /**
     * Create from Bytes.
     * 
     * @param value
     *            the bytes to create from.
     */
    public PassValue(Bytes value) {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        this.value = value;
    }

    /**
     * Create from bytes.
     * 
     * @param value
     *            the bytes to create from.
     */
    public PassValue(byte[] value) {
        this(new Bytes(value));
    }

    /**
     * Create from a base 16 encoded string.
     * 
     * @param value
     *            the base 16 encoded string to create from.
     */
    public PassValue(String value) {
        this(Base16.decode(value));
    }

    /**
     * create from the low eight bits of a sequence of integers.
     * 
     * @param value
     *            the contents, each entry will be chopped to the least
     *            significant eight bits.
     */
    public PassValue(int... value) {
        this(new Bytes(value));
    }

    /**
     * Convert to a Bytes value.
     * 
     * @return the Bytes value.
     */
    public Bytes toBytes() {
        return value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PassValue other = (PassValue) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

    /**
     * Convert to a base 16 encoded string.
     * 
     * @return the base 16 encoded string for the value.
     */
    @Override
    public String toString() {
        return Base16.encode(value);
    }

    /**
     * Convert to a byte array.
     * 
     * @return the byte array for the value.
     */
    public byte[] toByteArray() {
        return value.toByteArray();
    }
}
