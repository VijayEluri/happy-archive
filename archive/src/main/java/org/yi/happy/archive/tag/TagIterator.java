package org.yi.happy.archive.tag;

import java.io.InputStream;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.yi.happy.archive.LineIterator;

/**
 * Iterate over tags in a stream.
 */
public class TagIterator implements Iterator<Tag>, Iterable<Tag> {

    private LineIterator lines;
    private Tag item;

    /**
     * set up to iterate over an input stream.
     * 
     * @param in
     *            the input stream.
     */
    public TagIterator(InputStream in) {
        lines = new LineIterator(in);
    }

    /**
     * set up to iterate over a reader.
     * 
     * @param in
     *            the reader.
     */
    public TagIterator(Reader in) {
        lines = new LineIterator(in);
    }

    @Override
    public Iterator<Tag> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        if (item == null && lines.hasNext()) {
            TagBuilder tag = null;
            for (String line : lines) {
                if (tag == null && line.isEmpty()) {
                    continue;
                }
                if (line.isEmpty()) {
                    break;
                }
                if(tag == null) {
                    tag = new TagBuilder();
                }
                String[] parts = line.split("=", 2);
                if (parts.length == 1) {
                    tag.add(parts[0], "");
                } else {
                    tag.add(parts[0], parts[1]);
                }
            }
            if (tag != null) {
                item = tag.create();
            }
        }
        return item != null;
    }

    @Override
    public Tag next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        Tag out = item;
        item = null;
        return out;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
