package org.yi.happy.archive.block.parser;


/**
 * functions for parsing byte strings.
 */
public class ByteParse {
    /**
     * The carrier return end of line character.
     */
    public static final byte CR = '\r';

    /**
     * The line feed end of line character.
     */
    public static final byte LF = '\n';

    /**
     * find the first line break within the range.
     * 
     * @param bytes
     *            the bytes.
     * @param range
     *            the range to search in.
     * @return range that is the line break, or the end of the range if none
     *         found. Before this range is the first line, after this range is
     *         the start of the next line.
     */
    public static Range findEndOfLine(byte[] bytes, Range range) {
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

}
