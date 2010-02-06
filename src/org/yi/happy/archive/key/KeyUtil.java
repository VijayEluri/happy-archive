package org.yi.happy.archive.key;

import java.security.MessageDigest;

import org.yi.happy.annotate.TypeSwitch;
import org.yi.happy.archive.ByteString;
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
    @TypeSwitch
    // TODO move into key types
    public static LocatorKey toLocatorKey(Key key)
	    throws UnknownAlgorithmException {
	if (key instanceof LocatorKey) {
	    return (LocatorKey) key;
	}

	if (key instanceof BlobFullKey) {
	    BlobFullKey k = (BlobFullKey) key;
	    return new BlobLocatorKey(k.getHash());
	}

	if (key instanceof ContentFullKey) {
	    ContentFullKey k = (ContentFullKey) key;
	    return new ContentLocatorKey(k.getHash());
	}

	if (key instanceof NameFullKey) {
	    NameFullKey k = (NameFullKey) key;

	    MessageDigest md = k.getDigest().get();
	    md.update(ByteString.toUtf8(k.getName()));
	    return new NameLocatorKey(md.digest());
	}

	throw new IllegalArgumentException("can not convert key: " + key);
    }
}
