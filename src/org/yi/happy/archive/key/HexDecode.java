package org.yi.happy.archive.key;

/**
 * decode base 16 strings into bytes
 */
public class HexDecode {

    /**
     * turn a base 16 string into bytes
     * 
     * @param string
     *            the string
     * @return the bytes
     * @throws IllegalArgumentException
     *             if the string is not an even number of hex digits.
     */
    public static byte[] decode(String string) throws IllegalArgumentException {
	if (string.length() % 2 != 0) {
	    throw new IllegalArgumentException("bad string length "
		    + string.length());
	}
	try {
	    byte[] b = new byte[string.length() / 2];
	    for (int i = 0; i < b.length; i++) {
		b[i] = (byte) Integer.parseInt(string.substring(i * 2,
			i * 2 + 2), 16);
	    }
	    return b;
	} catch (NumberFormatException e) {
	    throw new IllegalArgumentException(e);
	}
    }

}
