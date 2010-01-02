package org.yi.happy.archive.key;

/**
 * turn bytes into a base 16 string
 */
public class HexEncode {

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

}
