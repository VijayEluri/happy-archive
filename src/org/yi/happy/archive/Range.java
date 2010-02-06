package org.yi.happy.archive;

public class Range {
    private final int offset;
    private final int length;

    public Range(int offset, int length) {
	this.offset = offset;
	this.length = length;
    }

    public int getOffset() {
	return offset;
    }

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

    public Range before(Range other) {
	if (this.getEnd() < other.getOffset()) {
	    return this;
	}

	if (other.getOffset() < this.getOffset()) {
	    return new Range(other.getOffset(), 0);
	}

	return new Range(this.getOffset(), other.getOffset() - this.getOffset());
    }

    public Range after(Range other) {
	if (other.getEnd() < this.getOffset()) {
	    return this;
	}

	if (other.getEnd() > this.getEnd()) {
	    return new Range(other.getEnd(), 0);
	}

	return new Range(other.getEnd(), this.getEnd() - other.getEnd());
    }

}
