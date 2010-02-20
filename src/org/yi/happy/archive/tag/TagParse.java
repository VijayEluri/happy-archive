package org.yi.happy.archive.tag;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.yi.happy.annotate.MagicLiteral;
import org.yi.happy.archive.ByteString;
import org.yi.happy.archive.block.parser.GenericBlockParse;
import org.yi.happy.archive.block.parser.Range;

/**
 * A parser for archive tags, the data format that describes directories.
 */
public class TagParse {
    /*
     * The legacy tag parser used 8-bit characters and '%xx' escaped byte values
     * (0x00..0x19, 0x7f..0xff, '%', '='), this one does not.
     * 
     * This parser uses UTF-8 character encoding and no escaping at this time,
     * though escaping may be introduced in the future.
     */

    /**
     * Parse all of the tags from a block of bytes.
     * 
     * @param d
     *            the bytes to parse.
     * @return the list of tags.
     */
    public static List<Tag> parse(byte[] d) {
	return parse(d, new Range(0, d.length));
    }

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
    private static Range findNewLine(byte[] bytes, Range range) {
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
     * Find a record separator (one or blank line) in the given range.
     * 
     * @param d
     *            the data buffer.
     * @param r
     *            the range to search.
     * @return the range where the record separator was found, the end of the
     *         range is considered a record separator.
     */
    public static Range findRecordSeparator(byte[] d, Range r) {
	Range l1 = findNewLine(d, r);

	/*
	 * the start of the range was the blank line
	 */
	if (l1.getOffset() == r.getOffset()) {
	    return l1;
	}

	while (true) {
	    Range l2 = findNewLine(d, r.after(l1));

	    /*
	     * just found a blank line.
	     */
	    if (l1.getEnd() == l2.getOffset()) {
		return l2;
	    }
	    l1 = l2;
	}
    }

    /**
     * Parse the tags found within a range.
     * 
     * @param d
     *            the data buffer.
     * @param r
     *            the range to search.
     * @return the found tags.
     */
    public static List<Tag> parse(byte[] d, Range r) {
	List<Tag> out = new ArrayList<Tag>();
	Map<String, String> fields = new LinkedHashMap<String, String>();
	while (true) {
	    Range endOfLine = findNewLine(d, r);
	    Range line = r.before(endOfLine);
	    r = r.after(endOfLine);

	    if (line.getLength() > 0) {
		Range s = findSeparator(d, line);
		String key = ByteString.fromUtf8(d, line.before(s));
		String value = ByteString.fromUtf8(d, line.after(s));
		if (fields.containsKey(key)) {
		    /*
		     * what should be done if the key is repeated?
		     */
		} else {
		    fields.put(key, value);
		}
		continue;
	    }

	    /*
	     * the line is empty.
	     */

	    if (!fields.isEmpty()) {
		/*
		 * there is a record to store.
		 */
		out.add(new Tag(fields));
		fields.clear();
	    }

	    if (endOfLine.getLength() == 0) {
		/*
		 * we are at the end of the data.
		 */
		break;
	    }
	}

	return out;
    }

    private static Range findSeparator(byte[] d, Range line) {
	for (int i = line.getOffset(); i < line.getEnd(); i++) {
	    if (d[i] == '=') {
		return new Range(i, 1);
	    }
	}
	return new Range(line.getEnd(), 0);
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
    private static Range findLastNewLine(byte[] bytes, Range range) {
	for (int i = range.getEnd() - 1; i >= range.getOffset(); i--) {
	    if (bytes[i] == '\r') {
		if (i - 1 >= range.getOffset() && bytes[i - 1] == '\n') {
		    return new Range(i - 1, 2);
		}
		return new Range(i, 1);
	    }
	    if (bytes[i] == '\n') {
		if (i - 1 >= range.getOffset() && bytes[i - 1] == '\r') {
		    return new Range(i - 1, 2);
		}
		return new Range(i, 1);
	    }
	}

	return new Range(range.getOffset(), 0);
    }

    /**
     * Find all the full records in the buffer.
     * 
     * @param d
     *            the buffer.
     * @param r
     *            the range to search.
     * @return the range that ends right after the last record separator, or an
     *         empty range at the start of the search range if no separator was
     *         found.
     */
    public static Range findFullRecords(byte[] d, Range r) {
	Range l2 = findLastNewLine(d, r);
	while (true) {
	    Range l1 = findLastNewLine(d, r.before(l2));
	    if (l1.getEnd() == l2.getOffset()) {
		/*
		 * l1 is the start of the blank line; l2 is the end of the blank
		 * line.
		 */
		return new Range(r.getOffset(), l2.getEnd() - r.getOffset());
	    }
	    l2 = l1;
	}
    }
}
