package org.yi.happy.archive.key;

import org.yi.happy.archive.crypto.UnknownAlgorithmException;

/**
 * utility for parsing keys
 */
public class KeyUtil {
    /**
     * turn a key into a locator key
     * 
     * @param key
     *            the key
     * @return a locator key
     */
    public static LocatorKey toLocatorKey(Key key)
	    throws UnknownAlgorithmException {
	if (key == null) {
	    throw new IllegalArgumentException();
	}

	return key.toLocatorKey();
    }
}
