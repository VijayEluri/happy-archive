package org.yi.happy.archive;

import java.io.UnsupportedEncodingException;

/**
 * convert byte arrays to 8 bit strings and back.
 * 
 * @author sarah dot a dot happy at gmail dot com
 */
public class ByteString {
    /**
     * prevent construction
     */
    private ByteString() {
        // never called
    }

    /**
     * convert bytes to a string, the string is made of characters with the low
     * 8 bits set to the bytes.
     * 
     * @param data
     *            the bytes
     * @return an 8 bit string
     */
    public static String toString(byte[] data) {
        char[] out = new char[data.length];

        for (int i = 0; i < data.length; i++) {
            out[i] = (char) (data[i] & 0xff);
        }

        return new String(out);
    }

    /**
     * convert a string to bytes, only the low 8 bits of the characters in the
     * string are used.
     * 
     * @param data
     *            the string
     * @return the low 8 bits of each character
     */
    public static byte[] toBytes(String data) {
        char[] in = data.toCharArray();
        byte[] out = new byte[in.length];

        for (int i = 0; i < in.length; i++) {
            out[i] = (byte) in[i];
        }

        return out;
    }

    /**
     * convert a string to UTF-8
     * 
     * @param string
     *            the string to convert
     * @return the resulting bytes
     * @throws Utf8NotSupportedError
     *             if UTF-8 encoding is not supported.
     */
    public static byte[] toUtf8(String string) {
        try {
            return string.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new Utf8NotSupportedError(e);
        }
    }

    /**
     * convert UTF-8 to a string.
     * 
     * @param data
     *            the UTF-8 encoded string.
     * @return the string.
     * @throws Utf8NotSupportedError
     *             if UTF-8 encoding is not supported.
     */
    public static String fromUtf8(byte[] data) {
        try {
            return new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new Utf8NotSupportedError(e);
        }
    }
}
