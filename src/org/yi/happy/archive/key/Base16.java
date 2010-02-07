package org.yi.happy.archive.key;

public class Base16 {

    private static final char[] table = { '0', '1', '2', '3', '4', '5', '6',
	    '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     * turn bytes into a base 16 string
     * 
     * @param bytes
     *            the bytes
     * @return the string
     */
    public static String encode(byte[] bytes) {
	char[] out = new char[bytes.length * 2];
	int o = 0;
	for (byte b : bytes) {
	    out[o++] = table[b >> 4 & 0xf];
	    out[o++] = table[b >> 0 & 0xf];
	}
	return new String(out);
    }

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

	byte[] b = new byte[string.length() / 2];
	for (int i = 0; i < b.length; i++) {
	    b[i] = (byte) Integer.parseInt(string.substring(i * 2, i * 2 + 2),
		    16);
	}
	return b;
    }

}
