package org.yi.happy.archive;

import org.yi.happy.archive.block.parser.Range;

/**
 * Functions for doing parsing on byte arrays.
 */
public class ByteParse {
    /**
     * A line feed character (0x0d).
     */
    public static final char LF = '\n';

    /**
     * A carrier-return character (0x0a).
     */
    public static final char CR = '\r';

    /**
     * find the first line break in the range.
     * 
     * @param bytes
     *            the bytes.
     * @param range
     *            the range to search in.
     * @return range that is the line break, or the end of the range if none
     *         found. Before this range is the first line, after this range is
     *         the start of the next line.
     */
    public static Range findNewLine(byte[] bytes, Range range) {
        for (int i = range.getOffset(); i < range.getEnd(); i++) {
            if (bytes[i] == CR) {
                if (i + 1 < range.getEnd() && bytes[i + 1] == LF) {
                    return new Range(i, 2);
                }
                return new Range(i, 1);
            }
            if (bytes[i] == LF) {
                if (i + 1 < range.getEnd() && bytes[i + 1] == CR) {
                    return new Range(i, 2);
                }
                return new Range(i, 1);
            }
        }

        return new Range(range.getEnd(), 0);
    }

    /**
     * find the last line break in the range.
     * 
     * @param bytes
     *            the bytes.
     * @param range
     *            the range to search in.
     * @return range that is the line break, or the start of the range if none
     *         found.
     */
    public static Range findLastNewLine(byte[] bytes, Range range) {
        for (int i = range.getEnd() - 1; i >= range.getOffset(); i--) {
            if (bytes[i] == CR) {
                if (i - 1 >= range.getOffset() && bytes[i - 1] == LF) {
                    return new Range(i - 1, 2);
                }
                return new Range(i, 1);
            }
            if (bytes[i] == LF) {
                if (i - 1 >= range.getOffset() && bytes[i - 1] == CR) {
                    return new Range(i - 1, 2);
                }
                return new Range(i, 1);
            }
        }

        return new Range(range.getOffset(), 0);
    }
}
