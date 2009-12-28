package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamLimit extends InputStream {

	private final int limit;
	private int used;
	private final InputStream in;

	public InputStreamLimit(InputStream in, int limit) {
		this.in = in;
		this.limit = limit;

		used = 0;
	}

	@Override
	public int read() throws IOException {
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
