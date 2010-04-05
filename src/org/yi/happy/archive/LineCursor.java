package org.yi.happy.archive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * A cursor for moving through lines in a stream.
 */
public class LineCursor {

    private final BufferedReader in;

    private String item;

    /**
     * create a attached to a reader.
     * 
     * @param in
     *            the reader to attach to.
     */
    public LineCursor(Reader in) {
        if (in instanceof BufferedReader) {
            this.in = (BufferedReader) in;
        } else {
            this.in = new BufferedReader(in);
        }
    }

    /**
     * create attached to an input stream, this uses the UTF-8 encoding to
     * convert to characters.
     * 
     * @param in
     *            the input stream.
     * @throws IOException
     */
    public LineCursor(InputStream in) throws IOException {
        this(new InputStreamReader(in, "utf-8"));
    }

    /**
     * move to the next line.
     * 
     * @return true if there is a next line, false if the end has been reached.
     * @throws IOException
     *             on errors.
     */
    public boolean next() throws IOException {
        item = in.readLine();
        return item != null;
    }

    /**
     * fetch the current line.
     * 
     * @return the current line.
     * @throws IllegalStateException
     *             if there is no current line.
     */
    public String get() {
        if (item == null) {
            throw new IllegalStateException();
        }

        return item;
    }

    /**
     * close the input.
     * 
     * @throws IOException
     */
    public void close() throws IOException {
        item = null;
        in.close();
    }
}
