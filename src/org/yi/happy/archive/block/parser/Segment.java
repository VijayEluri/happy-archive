package org.yi.happy.archive.block.parser;

/**
 * An offset and length.
 */
public class Segment {
    private final int offset;
    private final int length;

    /**
     * create a segment.
     * 
     * @param offset
     *            the offset where the segment starts.
     * @param length
     *            the length of the segment.
     */
    public Segment(int offset, int length) {
        this.offset = offset;
        this.length = length;
    }

    /**
     * 
     * @return the offset where the segment starts.
     */
    public int getOffset() {
        return offset;
    }

    /**
     * 
     * @return the length of the segment.
     */
    public int getLength() {
        return length;
    }

    /**
     * The end of the segment, as in the index after the last index that is part
     * of the segment.
     * 
     * @return index just past the end of the segment.
     */
    public int getEnd() {
        return offset + length;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + length;
        result = prime * result + offset;
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
        Segment other = (Segment) obj;
        if (length != other.length)
            return false;
        if (offset != other.offset)
            return false;
        return true;
    }

    /**
     * the slice of the current segment that is before the given segment.
     * 
     * @param other
     *            the segment to compare to.
     * @return a segment that starts at the minimum of the start of this and
     *         other, and ends at the minimum of the end of this and the
     *         beginning of other.
     */
    public Segment before(Segment other) {
        if (this.getEnd() < other.getOffset()) {
            return this;
        }

        if (other.getOffset() < this.getOffset()) {
            return new Segment(other.getOffset(), 0);
        }

        return new Segment(this.getOffset(), other.getOffset() - this.getOffset());
    }

    /**
     * the slice of the current segment that is after the given segment.
     * 
     * @param other
     *            the segment to compare to.
     * @return a segment that starts at the maximum of the end of the other
     *         segment and the beginning of this segment, and ends at the
     *         maximum of the end of the other segment and the end of this
     *         segment.
     */
    public Segment after(Segment other) {
        if (other.getEnd() < this.getOffset()) {
            return this;
        }

        if (other.getEnd() > this.getEnd()) {
            return new Segment(other.getEnd(), 0);
        }

        return new Segment(other.getEnd(), this.getEnd() - other.getEnd());
    }

    @Override
    public String toString() {
        return "(" + getOffset() + " + " + getLength() + " = " + getEnd() + ")";
    }
}
