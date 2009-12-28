package org.yi.happy.archive;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Streams {

	/**
	 * copy the the entire input stream to the output stream.
	 * 
	 * @param in
	 *            the source
	 * @param out
	 *            the target
	 * @throws IOException
	 *             on error
	 */
	public static void copy(InputStream in, OutputStream out)
			throws IOException {
		copy(in, out, null);
	}

    /**
	 * copy the the entire input stream to the output stream. This version
	 * allows for a buffer to be given, if the buffer is null one is created.
	 * 
	 * @param in
	 *            the source
	 * @param out
	 *            the target
	 * @param buff
	 *            the temporary buffer to use for reading, or null
	 * @throws IOException
	 *             on error
	 */
	public static void copy(InputStream in, OutputStream out, byte[] buff)
			throws IOException {
		if (buff == null) {
			buff = new byte[8192];
		}

		while (true) {
			int n = in.read(buff);

			if (n < 0) {
				break;
			}

            out.write(buff, 0, n);
		}
	}

	/**
	 * Load the entire content of the given stream into a byte array.
	 * 
	 * @param in
	 *            the stream to read.
	 * @return the bytes loaded.
	 * @throws IOException
	 *             on error.
	 */
	public static byte[] load(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Streams.copy(in, out);
		return out.toByteArray();
	}

	public static byte[] load(FileInputStream in, int limit) throws IOException {
		return load(new InputStreamLimit(in, limit));
	}

}
