package org.yi.happy.archive.index;

import java.io.InputStream;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.yi.happy.archive.LineIterator;
import org.yi.happy.archive.key.LocatorKey;
import org.yi.happy.archive.key.LocatorKeyParse;

/**
 * Iterate over index entries in a stream.
 */
public class IndexIterator implements Iterable<IndexEntry>,
        Iterator<IndexEntry> {

    private final LineIterator lines;
    private IndexEntry item = null;

    /**
     * set up to iterate over an input stream.
     * 
     * @param in
     *            the input stream.
     */
    public IndexIterator(InputStream in) {
        lines = new LineIterator(in);
    }

    /**
     * set up to iterate over a reader.
     * 
     * @param in
     *            the reader.
     */
    public IndexIterator(Reader in) {
        lines = new LineIterator(in);
    }

    @Override
    public boolean hasNext() {
        if (item == null && lines.hasNext()) {
            String line = lines.next();
            String[] parts = line.split("\t", -1);
            if (parts.length < 3) {
                throw new IndexOutOfBoundsException("short line");
            }
            String name = parts[0];
            String loader = parts[1];
            LocatorKey key = LocatorKeyParse.parseLocatorKey(parts[2]);
            String hash = parts.length > 3 ? parts[3] : "";
            String size = parts.length > 4 ? parts[4] : "";
            item = new IndexEntry(name, loader, key, hash, size);
        }
        return item != null;
    }

    @Override
    public IndexEntry next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        IndexEntry out = item;
        item = null;
        return out;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<IndexEntry> iterator() {
        return this;
    }

}
