package org.yi.happy.archive.block.parser;

import java.util.LinkedHashMap;
import java.util.Map;

import org.yi.happy.annotate.MagicLiteral;
import org.yi.happy.annotate.SmellsProcedural;
import org.yi.happy.archive.ByteParse;
import org.yi.happy.archive.ByteString;
import org.yi.happy.archive.Bytes;
import org.yi.happy.archive.block.GenericBlock;

/**
 * parser for {@link GenericBlock}.
 */
@SmellsProcedural
public class GenericBlockParse {

    /**
     * A very tolerant generic block parser. If the size header is present than
     * it must be valid, and the body is trimmed to the value of it.
     * 
     * @param bytes
     *            the bytes for the block.
     * @return the parsed block.
     * @throws IllegalArgumentException
     *             if a header is repeated, or if the size header is not valid.
     */
    public GenericBlock parse(byte[] bytes) {
        Map<String, String> meta = new LinkedHashMap<String, String>();

        Range rest = new Range(0, bytes.length);

        /*
         * parse the headers
         */
        while (true) {
            Range endOfLine = findEndOfLine(bytes, rest);
            Range line = rest.before(endOfLine);
            rest = rest.after(endOfLine);

            /*
             * if the line has zero length, then the headers are done and the
             * rest is the body.
             */
            if (line.getLength() == 0) {
                break;
            }

            Range divider = findDivider(bytes, line);

            String name = ByteString.fromUtf8(bytes, line.before(divider));
            String value = ByteString.fromUtf8(bytes, line.after(divider));

            if (meta.containsKey(name)) {
                throw new IllegalArgumentException("repeated header: " + name);
            }
            meta.put(name, value);
        }

        trim: {
            /*
             * if the size header is present...
             */
            String s = meta.get(GenericBlock.SIZE_META);
            if (s == null) {
                break trim;
            }

            /*
             * it is required to be valid, it is not valid if it does not parse
             * as a number, or it is less than zero, or it is greater than the
             * length of the rest of the data.
             */
            int i;
            try {
                i = Integer.parseInt(s);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("bad size header");
            }
            if (i < 0 || i > rest.getLength()) {
                throw new IllegalArgumentException("bad size header");
            }

            /*
             * if it matches the size of the rest of the data, then it does not
             * need trimming.
             */
            if (i == rest.getLength()) {
                break trim;
            }

            /*
             * otherwise trim it.
             */
            rest = new Range(rest.getOffset(), i);
        }

        /*
         * copy the body out of the buffer.
         */
        Bytes body = new Bytes(bytes, rest.getOffset(), rest.getLength());

        return new GenericBlock(meta, body);
    }

    /**
     * find the line break byte at or after index.
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
    private Range findEndOfLine(byte[] bytes, Range range) {
        return ByteParse.findNewLine(bytes, range);
    }

    /**
     * find the field key/value divider in a line.
     * 
     * @param bytes
     *            the bytes being parsed.
     * @param line
     *            the range that is the line.
     * @return the range that is the divider, or the end of the range if not
     *         found. Before this range is the key, after this range is the
     *         value.
     */
    @MagicLiteral
    private Range findDivider(byte[] bytes, Range line) {
        for (int i = line.getOffset(); i < line.getEnd(); i++) {
            if (bytes[i] == ':') {
                if (i + 1 < line.getEnd() && bytes[i + 1] == ' ') {
                    return new Range(i, 2);
                }
                return new Range(i, 1);
            }
        }

        return new Range(line.getEnd(), 0);
    }
}
