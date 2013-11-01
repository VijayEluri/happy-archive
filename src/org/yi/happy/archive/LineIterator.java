package org.yi.happy.archive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterates over all the lines in a stream. This is also {@link Iterable} so
 * that it can be used in a for loop.
 */
public class LineIterator implements Iterable<String>, Iterator<String> {

    private final BufferedReader in;
    private String item;

    /**
     * Set up to iterate over a reader.
     * 
     * @param in
     *            the reader to use.
     */
    public LineIterator(Reader in) {
        if (in instanceof BufferedReader) {
            this.in = (BufferedReader) in;
        } else {
            this.in = new BufferedReader(in);
        }
    }

    /**
     * Set up to iterate over an {@link InputStream}. the stream is read as
     * UTF-8 text.
     * 
     * @param in
     * @throws IOException
     */
    public LineIterator(InputStream in) throws IOException {
        this(new InputStreamReader(in, "UTF-8"));
    }

    @Override
    public boolean hasNext() {
        try {
            if (item == null) {
                item = in.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return item != null;
    }

    @Override
    public String next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        String out = item;
        item = null;
        return out;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<String> iterator() {
        return this;
    }

    /**
     * close the stream.
     * 
     * @throws IOException
     */
    public void close() throws IOException {
        in.close();
    }
}
