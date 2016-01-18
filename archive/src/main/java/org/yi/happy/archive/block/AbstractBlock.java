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
        Map<String, String> meta = getMeta();
        Bytes body = getBody();

        /*
         * calculate the size and gather the header bytes.
         */
        byte[][] parts = new byte[meta.size() * 2][];
        int i = 0;
        int offset = 0;
        for (Map.Entry<String, String> entry : meta.entrySet()) {
            parts[i] = ByteString.toUtf8(entry.getKey());
            offset += parts[i].length;
            offset += SEPARATOR.getSize();
            parts[i + 1] = ByteString.toUtf8(entry.getValue());
            offset += parts[i + 1].length;
            offset += ENDL.getSize();
            i += 2;
        }
        offset += ENDL.getSize();
        offset += body.getSize();

        /*
         * fill in the output.
         */
        byte[] out = new byte[offset];
        offset = 0;
        for (i = 0; i < parts.length; i += 2) {
            System.arraycopy(parts[i], 0, out, offset, parts[i].length);
            offset += parts[i].length;
            SEPARATOR.getBytes(out, offset);
            offset += SEPARATOR.getSize();
            System.arraycopy(parts[i + 1], 0, out, offset, parts[i + 1].length);
            offset += parts[i + 1].length;
            ENDL.getBytes(out, offset);
            offset += ENDL.getSize();
        }
        ENDL.getBytes(out, offset);
        offset += ENDL.getSize();
        body.getBytes(out, offset);
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

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("[").append(getClass().getSimpleName()).append(": ");
        for (Map.Entry<String, String> i : getMeta().entrySet()) {
            out.append(i.getKey()).append(" = ").append(i.getValue())
                    .append(", ");
        }
        out.append(getBody()).append("]");
        return out.toString();
    }
}
