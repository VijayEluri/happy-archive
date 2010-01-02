package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;

/**
 * A wrapper for an input stream that throws an exception when more than a set
 * limit of bytes are read through it.
 */
public class InputStreamLimit extends InputStream {

    /**
     * the maximum bytes before exceptions are thrown on read.
     */
    private final int limit;

    /**
     * the number of bytes that have been read.
     */
    private int used;

    /**
     * the real {@link InputStream}.
     */
    private final InputStream in;

    /**
     * Make an input stream that throws exceptions after the given number of
     * bytes are read.
     * 
     * @param in
     *            the real input stream to read from.
     * @param limit
     *            the maximum number of bytes to read before errors are thrown.
     */
    public InputStreamLimit(InputStream in, int limit) {
	this.in = in;
	this.limit = limit;

	used = 0;
    }

    @Override
    public int read() throws IOException {
	if (used > limit) {
	    throw new IOException();
	}

	int out = in.read();

	if (out >= 0) {
	    used++;
	}
	if (used > limit) {
	    throw new IOException();
	}

	return out;
    }

    @Override
    public int read(byte[] b) throws IOException {
	if (used > limit) {
	    throw new IOException();
	}

	int out = in.read(b);

	if (out > 0) {
	    used += out;
	}
	if (used > limit) {
	    throw new IOException();
	}

	return out;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
	if (used > limit) {
	    throw new IOException();
	}

	int out = in.read(b, off, len);

	if (out > 0) {
	    used += out;
	}
	if (used > limit) {
	    throw new IOException();
	}

	return out;
    }
}
