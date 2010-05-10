package org.yi.happy.archive;

import org.yi.happy.annotate.MagicLiteral;
import org.yi.happy.archive.block.parser.GenericBlockParse;
import org.yi.happy.archive.block.parser.Range;

/**
 * Functions for doing parsing on byte arrays.
 */
public class ByteParse {
    /**
     * find the line break byte at or after index.
     * 
     * XXX duplicate of {@link GenericBlockParse#findEndOfLine(byte[],Range)}.
     * 
     * @param bytes
     *            the bytes.
     * @param range
     *            the range to search in.
     * @return range that is the line break, or the end of the range if none
     *         found. Before this range is the first line, after this range is
     *         the start of the next line.
     */
    @MagicLiteral
    public static Range findNewLine(byte[] bytes, Range range) {
        for (int i = range.getOffset(); i < range.getEnd(); i++) {
            if (bytes[i] == '\r') {
                if (i + 1 < range.getEnd() && bytes[i + 1] == '\n') {
                    return new Range(i, 2);
                }
                return new Range(i, 1);
            }
            if (bytes[i] == '\n') {
                if (i + 1 < range.getEnd() && bytes[i + 1] == '\r') {
                    return new Range(i, 2);
                }
                return new Range(i, 1);
            }
        }

        return new Range(range.getEnd(), 0);
    }

}
