package org.yi.happy.archive;

import java.io.ByteArrayOutputStream;
import java.io.IOError;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * A {@link PrintStream} that captures what is sent to it. This is used in tests
 * to take the place of the standard output and standard error streams. Text
 * written to this string is encoded using UTF-8.
 */
public class CapturePrintStream extends PrintStream {
    private final ByteArrayOutputStream buffer;

    /**
     * Create the capturing print stream.
     * 
     * @return a new capturing print stream.
     */
    public static CapturePrintStream create() {
        try {
            return new CapturePrintStream();
        } catch (UnsupportedEncodingException e) {
            throw new IOError(e);
        }
    }

    /**
     * Create the capturing print stream. Use {@link #create()} to avoid the
     * checked exception.
     * 
     * @throws UnsupportedEncodingException
     *             should not be thrown.
     */
    public CapturePrintStream() throws UnsupportedEncodingException {
        this(new ByteArrayOutputStream());
    }

    /**
     * Create the capturing print stream.
     * 
     * @param buffer
     *            the internal buffer.
     * @throws UnsupportedEncodingException
     *             should not be thrown.
     */
    private CapturePrintStream(ByteArrayOutputStream buffer)
            throws UnsupportedEncodingException {
        super(buffer, true, "UTF-8");
        this.buffer = buffer;
    }

    @Override
    public String toString() {
        try {
            return buffer.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IOError(e);
        }
    }

    /**
     * @return the size of the captured output.
     */
    public int size() {
        return buffer.size();
    }
}
