package org.yi.happy.archive;

import java.io.ByteArrayOutputStream;
import java.io.IOError;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class CapturePrintStream extends PrintStream {
    private final ByteArrayOutputStream buffer;

    public static CapturePrintStream create() {
        try {
            return new CapturePrintStream();
        } catch (UnsupportedEncodingException e) {
            throw new IOError(e);
        }
    }

    private CapturePrintStream() throws UnsupportedEncodingException {
        this(new ByteArrayOutputStream());
    }

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
}
