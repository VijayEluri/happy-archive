package org.yi.happy.archive;

import java.util.Arrays;

/**
 * An immutable value object representing a byte array.
 * 
 * @author sarah dot a dot happy at gmail dot com
 */
public final class Bytes {
    private final byte[] data;

    public Bytes(byte[] data) {
        this(data, 0, data.length);
    }

    public Bytes(byte[] data, int offset, int size) {
        this.data = new byte[size];
        System.arraycopy(data, offset, this.data, 0, size);
    }

    public Bytes() {
        this.data = new byte[0];
    }

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

    public final byte get(int index) {
        return data[index];
    }

    public final void getBytes(int pos, byte[] dest, int destPos, int length) {
        System.arraycopy(data, pos, dest, destPos, length);
    }

    public final boolean equalBytes(byte[] other) {
        if (other == null) {
            return false;
        }

        return Arrays.equals(data, other);
    }

    public final int getSize() {
        return data.length;
    }
}
