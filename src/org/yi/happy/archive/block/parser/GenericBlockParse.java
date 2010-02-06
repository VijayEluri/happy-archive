package org.yi.happy.archive.block.parser;

import org.yi.happy.annotate.SmellsProcedural;
import org.yi.happy.archive.ByteString;
import org.yi.happy.archive.block.GenericBlock;

@SmellsProcedural
public class GenericBlockParse {

    /**
     * A very tolerant generic block parser. If the size header is a
     * non-negative number less than the size of the data remaining after the
     * headers are parsed, then the body is trimmed to that size.
     * 
     * @param bytes
     *            the bytes for the block.
     * @return the parsed block.
     */
    public GenericBlock parse(byte[] bytes) {
	GenericBlock out = new GenericBlock();

	Range rest = new Range(0, bytes.length);

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
	    out.addMeta(name, value);
	}

	trim: try {
	    String s = out.getMeta().get("size");
	    if (s == null) {
		break trim;
	    }

	    int i = Integer.parseInt(s);
	    if (i < 0 || i >= rest.getLength()) {
		break trim;
	    }

	    rest = new Range(rest.getOffset(), i);
	} catch (NumberFormatException e) {
	    break trim;
	}

	byte[] body = new byte[rest.getLength()];
	System.arraycopy(bytes, rest.getOffset(), body, 0, rest.getLength());
	out.setBody(body);

	return out;
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
    private Range findEndOfLine(byte[] bytes, Range range) {
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
