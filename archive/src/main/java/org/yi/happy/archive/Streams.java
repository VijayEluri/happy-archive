package org.yi.happy.archive;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;

/**
 * Utility methods for doing common operations {@link InputStream} and
 * {@link OutputStream}.
 */
public class Streams {
    /**
     * The default buffer size used here. This is the same buffer size used by
     * {@link BufferedInputStream}.
     */
    public static int BUFFER_SIZE = 8192;

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
            buff = new byte[BUFFER_SIZE];
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

    /**
     * Load the entire content of the given stream into a byte array, unless it
     * is larger than limit.
     * 
     * @param in
     *            the stream to read from.
     * @param limit
     *            the maximum bytes allowed to be read.
     * @return the bytes loaded.
     * @throws IOException
     *             on error, including exceeding the limit.
     */
    public static byte[] load(InputStream in, int limit) throws IOException {
        return load(new InputStreamLimit(in, limit));
    }

    /**
     * Load the entire content of the given reader into a string.
     * 
     * @param in
     *            the reader to read from.
     * @return the characters loaded.
     * @throws IOException
     */
    public static String load(Reader in) throws IOException {
        StringBuilder out = new StringBuilder();
        char[] buff = new char[BUFFER_SIZE];
        while (true) {
            int n = in.read(buff);

            if (n < 0) {
                break;
            }

            out.append(buff, 0, n);
        }
        return out.toString();
    }
}
