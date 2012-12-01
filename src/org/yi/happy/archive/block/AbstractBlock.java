package org.yi.happy.archive.block;

import java.util.Map;

import org.yi.happy.archive.ByteString;
import org.yi.happy.archive.Bytes;

/**
 * The part that is common for all the block types.
 */
public abstract class AbstractBlock implements Block {
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
            n += SEPARATOR.getSize();
            i++;
            p[i] = ByteString.toUtf8(j.getValue());
            n += p[i].length;
            n += ENDL.getSize();
            i++;
        }
        n += ENDL.getSize();
        n += b.getSize();

        /*
         * fill in the output.
         */
        byte[] out = new byte[n];
        n = 0;
        for (i = 0; i < p.length; i += 2) {
            System.arraycopy(p[i], 0, out, n, p[i].length);
            n += p[i].length;
            SEPARATOR.getBytes(out, n);
            n += SEPARATOR.getSize();
            System.arraycopy(p[i + 1], 0, out, n, p[i + 1].length);
            n += p[i + 1].length;
            ENDL.getBytes(out, n);
            n += ENDL.getSize();
        }
        ENDL.getBytes(out, n);
        n += ENDL.getSize();
        b.getBytes(out, n);
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

    /**
     * Check a meta-data header.
     * 
     * @param name
     *            the name part of the header.
     * @param value
     *            the value part of the header.
     * @throws IllegalArgumentException
     *             if the name or value have a newline in them, or if the name
     *             has a ':' in it.
     * @throws NullPointerException
     *             if name or value are null.
     */
    protected void checkHeader(String name, String value) {
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (c == COLON || c == CR || c == LF) {
                throw new IllegalArgumentException(
                        "name can not have colon or newline");
            }
        }

        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == CR || c == LF) {
                throw new IllegalArgumentException("value can not have newline");
            }
        }
    }

}
