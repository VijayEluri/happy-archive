package org.yi.happy.archive;

import java.util.Arrays;

/**
 * An immutable value object representing a byte array.
 */
public final class Bytes implements Comparable<Bytes> {
    /**
     * The bytes.
     */
    private final byte[] data;

    /**
     * Create from a byte array.
     * 
     * @param data
     *            the contents.
     * @throws NullPointerException
     *             if data is null.
     */
    public Bytes(byte[] data) {
        this.data = data.clone();
    }

    /**
     * Create from an integer array. This is to allow for easy creation of
     * literals.
     * 
     * @param data
     *            the contents, each entry will be chopped to the least
     *            significant eight bits.
     */
    public Bytes(int... data) {
        this.data = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            this.data[i] = (byte) data[i];
        }
    }

    /**
     * Create from a slice of a byte array.
     * 
     * @param data
     *            the byte array.
     * @param offset
     *            the offset where the slice begins.
     * @param size
     *            the size of the slice.
     */
    public Bytes(byte[] data, int offset, int size) {
        this.data = new byte[size];
        System.arraycopy(data, offset, this.data, 0, size);
    }

    /**
     * Create empty.
     */
    public Bytes() {
        this.data = new byte[0];
    }

    /**
     * Create from a slice of a byte array.
     * 
     * @param data
     *            the byte array.
     * @param offset
     *            the offset where the slice begins.
     * @param size
     *            the size of the slice.
     */
    public Bytes(Bytes data, int offset, int size) {
        this(data.data, offset, size);
    }

    /**
     * Get the contents. The return value is a copy of the internal value and is
     * safe to modify.
     * 
     * @return the contents.
     */
    public final byte[] toByteArray() {
        return data.clone();
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(data);
        return result;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Bytes other = (Bytes) obj;
        if (!Arrays.equals(data, other.data))
            return false;
        return true;
    }

    /**
     * get the byte at the given index.
     * 
     * @param index
     *            the index.
     * @return the byte.
     */
    public final byte get(int index) {
        return data[index];
    }

    /**
     * Copy bytes from this value.
     * 
     * @param pos
     *            the position in this value to start copying from.
     * @param dest
     *            the destination byte array.
     * @param destPos
     *            the position in the destination byte array to copy to.
     * @param length
     *            the number of bytes to copy.
     */
    public final void getBytes(int pos, byte[] dest, int destPos, int length) {
        System.arraycopy(data, pos, dest, destPos, length);
    }

    /**
     * Copy all the bytes from this value.
     * 
     * @param dest
     *            the destination byte array.
     * @param destPos
     *            the position in the destination byte array to copy to.
     */
    public final void getBytes(byte[] dest, int destPos) {
        System.arraycopy(data, 0, dest, destPos, data.length);
    }

    /**
     * Compare the bytes in this value with a byte array.
     * 
     * @param other
     *            the byte array to compare to.
     * @return true if this value and the byte array are equivalent.
     */
    public final boolean equalBytes(byte[] other) {
        if (other == null) {
            return false;
        }

        return Arrays.equals(data, other);
    }

    /**
     * Get the number of bytes in this value.
     * 
     * @return the number of bytes in this value.
     */
    public final int getSize() {
        return data.length;
    }

    @Override
    public int compareTo(Bytes o) {
        int n = Math.min(data.length, o.data.length);
        for (int i = 0; i < n; i++) {
            if (data[i] == o.data[i]) {
                continue;
            }
            return (data[i] & 0xff) - (o.data[i] & 0xff);
        }
        return data.length - o.data.length;
    }
}
