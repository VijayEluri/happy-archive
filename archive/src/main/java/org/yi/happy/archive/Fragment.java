package org.yi.happy.archive;

/**
 * A data fragment. This is just an offset and a block of bytes.
 */
public final class Fragment {
    private final long offset;
    private final Bytes data;

    /**
     * Create a fragment.
     * 
     * @param offset
     *            the offset for this fragment.
     * @param data
     *            the data for this fragment.
     */
    public Fragment(long offset, byte[] data) {
        this(offset, new Bytes(data));
    }

    /**
     * Create a fragment.
     * 
     * @param offset
     *            the offset for this fragment.
     * @param data
     *            the data for this fragment.
     */
    public Fragment(long offset, Bytes data) {
        if (data == null) {
            throw new IllegalArgumentException();
        }
        this.offset = offset;
        this.data = data;
    }

    /**
     * @return the offset for this fragment.
     */
    public final long getOffset() {
        return offset;
    }

    /**
     * Get the data found at the offset.
     * 
     * @return the data found at the offset, not null.
     */
    public final Bytes getData() {
        return data;
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        result = prime * result + (int) (offset ^ (offset >>> 32));
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
        Fragment other = (Fragment) obj;
        if (data == null) {
            if (other.data != null)
                return false;
        } else if (!data.equals(other.data))
            return false;
        if (offset != other.offset)
            return false;
        return true;
    }

    /**
     * get the size of the fragment.
     * 
     * @return the size of the fragment.
     */
    public final int getSize() {
        return data.getSize();
    }

    /**
     * get a byte from the fragment based on the absolute offset of the byte.
     * 
     * @param offset
     *            the offset of the byte to get.
     * @return the byte.
     * @throws IndexOutOfBoundsException
     *             if the index is not within the fragment.
     */
    public final byte getAbsolute(long offset) {
        if (offset < this.offset || offset >= this.offset + data.getSize()) {
            throw new IndexOutOfBoundsException();
        }

        return data.get((int) (offset - this.offset));
    }

    @Override
    public String toString() {
        return "Fragment [offset=" + offset + ", data=" + data + "]";
    }
}
