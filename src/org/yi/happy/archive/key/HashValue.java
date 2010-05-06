package org.yi.happy.archive.key;

import org.yi.happy.archive.Base16;
import org.yi.happy.archive.Bytes;

/**
 * A byte string that is the result of performing some message digest. This is
 * just a string of at least one byte.
 */
public final class HashValue implements Comparable<HashValue> {
    private final Bytes value;

    /**
     * Create from Bytes.
     * 
     * @param value
     *            the Bytes for the value.
     */
    public HashValue(Bytes value) {
        if (value.getSize() < 1) {
            throw new IllegalArgumentException("hash must be at least one byte");
        }
        this.value = value;
    }

    /**
     * Turn this Hash into a Bytes value.
     * 
     * @return the Hash as Bytes.
     */
    public Bytes toBytes() {
        return value;
    }

    /**
     * Create from a Base16 encoded string.
     * 
     * @param value
     *            the base 16 encoded string.
     */
    public HashValue(String value) {
        this(Base16.decode(value));
    }

    /**
     * Turn this Hash into a Base16 encoded string.
     * 
     * @return the base 16 encoded string representation of this hash.
     */
    @Override
    public String toString() {
        return Base16.encode(value);
    }

    /**
     * Create from a byte array.
     * 
     * @param value
     *            the byte array.
     */
    public HashValue(byte[] value) {
        this(new Bytes(value));
    }

    /**
     * Create from an integer array. This is to allow for easy creation of
     * literals.
     * 
     * @param value
     *            the contents, each entry will be chopped to the least
     *            significant eight bits.
     */
    public HashValue(int... value) {
        this(new Bytes(value));
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
        HashValue other = (HashValue) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

    @Override
    public int compareTo(HashValue o) {
        return value.compareTo(o.value);
    }

    /**
     * Compare the bytes of this hash to a byte array.
     * 
     * @param other
     *            the bytes to compare to.
     * @return true if the hash and the bytes are equal.
     */
    public boolean equalBytes(byte[] other) {
        return value.equalBytes(other);
    }

}
