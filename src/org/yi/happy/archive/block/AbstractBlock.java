package org.yi.happy.archive.block;

import java.util.Map;

import org.yi.happy.archive.ByteString;
import org.yi.happy.archive.Bytes;

/**
 * The part that is common for all the block types.
 */
public abstract class AbstractBlock implements Block {
    private byte[] ENDL = new byte[] { '\r', '\n' };
    private byte[] SEPARATOR = new byte[] { ':', ' ' };

    /**
     * get the block representation as bytes, assuming all the meta entries are
     * valid and the body is not null.
     */
    @Override
    public byte[] asBytes() {
	Map<String, String> m = getMeta();
	Bytes b = getBody();

	/*
	 * calculate the size and gather the header bytes.
	 */
	byte[][] p = new byte[m.size() * 2][];
	/**
	 * index into the part list
	 */
	int i = 0;
	/**
	 * number of bytes.
	 */
	int n = 0;
	for (Map.Entry<String, String> j : m.entrySet()) {
	    p[i] = ByteString.toUtf8(j.getKey());
	    n += p[i].length;
	    n += SEPARATOR.length;
	    i++;
	    p[i] = ByteString.toUtf8(j.getValue());
	    n += p[i].length;
	    n += ENDL.length;
	    i++;
	}
	n += ENDL.length;
	n += b.getSize();

	/*
	 * fill in the output.
	 */
	byte[] out = new byte[n];
	n = 0;
	for (i = 0; i < p.length; i += 2) {
	    System.arraycopy(p[i], 0, out, n, p[i].length);
	    n += p[i].length;
	    System.arraycopy(SEPARATOR, 0, out, n, SEPARATOR.length);
	    n += SEPARATOR.length;
	    System.arraycopy(p[i + 1], 0, out, n, p[i + 1].length);
	    n += p[i + 1].length;
	    System.arraycopy(ENDL, 0, out, n, ENDL.length);
	    n += ENDL.length;
	}
	System.arraycopy(ENDL, 0, out, n, ENDL.length);
	n += ENDL.length;
	b.getBytes(0, out, n, b.getSize());
	return out;
    }

    @Override
    public abstract Map<String, String> getMeta();

    @Override
    public abstract Bytes getBody();

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + getMeta().hashCode();
	result = prime * result + getBody().hashCode();
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (!(obj instanceof Block))
	    return false;
	Block other = (Block) obj;
	if (!getMeta().equals(other.getMeta()))
	    return false;
	if (!getBody().equals(other.getBody()))
	    return false;
	return true;
    }

}
