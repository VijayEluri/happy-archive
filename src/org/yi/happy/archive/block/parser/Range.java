package org.yi.happy.archive.block.parser;

/**
 * A range.
 */
public class Range {
    private final int offset;
    private final int length;

    /**
     * create a range.
     * 
     * @param offset
     *            the offset where the range starts.
     * @param length
     *            the length of the range.
     */
    public Range(int offset, int length) {
	this.offset = offset;
	this.length = length;
    }

    /**
     * 
     * @return the offset where the range starts.
     */
    public int getOffset() {
	return offset;
    }

    /**
     * 
     * @return the length of the range.
     */
    public int getLength() {
	return length;
    }

    /**
     * The end of the range, as in the index after the last index that is part
     * of the range.
     * 
     * @return index just past the end of the range.
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
	Range other = (Range) obj;
	if (length != other.length)
	    return false;
	if (offset != other.offset)
	    return false;
	return true;
    }

    /**
     * the slice of the current range that is before the given range.
     * 
     * @param other
     *            the range to compare to.
     * @return a range that starts at the minimum of the start of this and
     *         other, and ends at the minimum of the end of this and the
     *         beginning of other.
     */
    public Range before(Range other) {
	if (this.getEnd() < other.getOffset()) {
	    return this;
	}

	if (other.getOffset() < this.getOffset()) {
	    return new Range(other.getOffset(), 0);
	}

	return new Range(this.getOffset(), other.getOffset() - this.getOffset());
    }

    /**
     * the slice of the current range that is after the given range.
     * 
     * @param other
     *            the range to compare to.
     * @return a range that starts at the maximum of the end of the other range
     *         and the beginning of this range, and ends at the maximum of the
     *         end of the other range and the end of this range.
     */
    public Range after(Range other) {
	if (other.getEnd() < this.getOffset()) {
	    return this;
	}

	if (other.getEnd() > this.getEnd()) {
	    return new Range(other.getEnd(), 0);
	}

	return new Range(other.getEnd(), this.getEnd() - other.getEnd());
    }
    
    @Override
    public String toString() {
	return "(" + getOffset() + ".." + getEnd() + ")";
    }
}
