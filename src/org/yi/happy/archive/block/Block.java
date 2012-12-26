package org.yi.happy.archive.block;

import java.util.Map;

import org.yi.happy.annotate.ExternalName;
import org.yi.happy.annotate.ExternalValue;
import org.yi.happy.archive.Bytes;

/**
 * a data block is a set of simple headers and a body, with a very simple
 * format. The upper size of any block in use is just over 1 MiB. If there is a
 * size header, then the size of the body must match the value of the size
 * header.
 */
public interface Block {
    /**
     * The meta-data field name for the size of the body. Implementations are
     * required to ensure that if this meta-data field is present then it
     * matches the size of the body.
     */
    @ExternalName
    public static final String SIZE_META = "size";

    /**
     * The first byte of the end of line sequence ('\r').
     */
    @ExternalValue
    public static final byte CR = '\r';

    /**
     * The second byte of the end of line sequence ('\n').
     */
    @ExternalValue
    public static final byte LF = '\n';

    /**
     * The first byte of the header separator sequence (':').
     */
    @ExternalValue
    public static final byte COLON = ':';

    /**
     * The second byte of the header separator sequence (' ').
     */
    @ExternalValue
    public static final byte SPACE = ' ';

    /**
     * The terminator of a header and the separator between the headers and body
     * (CR LF). Parsers should be flexible and accept any combination of the
     * bytes in this sequence (either, both, reverse).
     */
    @ExternalValue
    public static final Bytes ENDL = new Bytes(CR, LF);

    /**
     * The separator of a header name and the value of the header (COLON SPACE).
     * Parsers should be flexible and accept the first byte by itself in
     * addition to the full sequence.
     */
    @ExternalValue
    public static final Bytes SEPARATOR = new Bytes(COLON, SPACE);

    /**
     * get the meta data (headers).
     * 
     * @return a possibly ordered map of the meta data fields.
     */
    public Map<String, String> getMeta();

    /**
     * get the body of the block.
     * 
     * @return the body of the block.
     */
    public Bytes getBody();

    /**
     * convert the block to bytes.
     * 
     * @return the block as bytes.
     */
    public byte[] asBytes();

    /**
     * When implementing this method, it is expected that two different
     * implementations of Block which have the same meta-data and body are
     * equal.
     * 
     * @param obj
     *            the object to compare to.
     * @return true if the objects are equivalent, false otherwise.
     * @see AbstractBlock
     */
    @Override
    public boolean equals(Object obj);

    /**
     * When implementing this method, it is expected that two different
     * implementations of Block which have the same meta-data and body are
     * equal.
     * 
     * @param obj
     *            the object to compare to.
     * @return true if the objects are equivalent, false otherwise.
     * @see AbstractBlock
     */
    @Override
    public int hashCode();
}
