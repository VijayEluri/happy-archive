package org.yi.happy.archive.key;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.yi.happy.archive.Base16;
import org.yi.happy.archive.crypto.DigestFactory;

public class KeyParse {
    private KeyParse() {

    }

    /**
     * the pattern for blob locators
     */
    public static final Pattern BLOB_LOCATOR = Pattern
	    .compile("blob:(\\p{XDigit}+)");

    /**
     * the pattern for content locators
     */
    public static final Pattern CONTENT_LOCATOR = Pattern
	    .compile("content-hash:(\\p{XDigit}+)");

    /**
     * the pattern for name locators
     */
    public static final Pattern NAME_LOCATOR = Pattern
	    .compile("name-hash:(\\p{XDigit}+)");

    /**
     * the pattern for blob keys
     */
    public static final Pattern BLOB_KEY = Pattern
	    .compile("blob:(\\p{XDigit}+):(\\p{XDigit}*)");

    /**
     * the pattern for content keys
     */
    public static final Pattern CONTENT_KEY = Pattern
	    .compile("content-hash:(\\p{XDigit}+):(\\p{XDigit}*)");

    /**
     * the pattern for name keys
     */
    public static final Pattern NAME_KEY = Pattern
	    .compile("name-hash:([^:]+):(.*)");

    /**
     * parse a string into whatever type of key it is.
     * 
     * @param key
     *            the string
     * @return the key (ContentKey, NameKey, LocatorKey)
     */
    public static Key parseKey(String key) {
	try {
	    return parseFullKey(key);
	} catch (IllegalArgumentException e) {
	    // try something else
	}

	try {
	    return parseLocatorKey(key);
	} catch (IllegalArgumentException e) {
	    // try something else
	}

	throw new IllegalArgumentException("can not parse key");
    }

    public static FullKey parseFullKey(String key) {
	Matcher m = BLOB_KEY.matcher(key);
	if (m.matches()) {
	    return new BlobFullKey(Base16.decode(m.group(1)), Base16.decode(m.group(2)));
	}

	m = CONTENT_KEY.matcher(key);
	if (m.matches()) {
	    return new ContentFullKey(Base16.decode(m.group(1)), Base16.decode(m.group(2)));
	}

	m = NAME_KEY.matcher(key);
	if (m.matches()) {
	    return new NameFullKey(DigestFactory.getProvider(m.group(1)), m
		    .group(2));
	}

	throw new IllegalArgumentException("can not parse key");
    }

    public static LocatorKey parseLocatorKey(String key) {
	Matcher m;
	m = BLOB_LOCATOR.matcher(key);
	if (m.matches()) {
	    return new BlobLocatorKey(Base16.decode(m.group(1)));
	}

	m = CONTENT_LOCATOR.matcher(key);
	if (m.matches()) {
	    return new ContentLocatorKey(Base16.decode(m.group(1)));
	}

	m = NAME_LOCATOR.matcher(key);
	if (m.matches()) {
	    return new NameLocatorKey(Base16.decode(m.group(1)));
	}

	throw new IllegalArgumentException("can not parse key");
    }

    /**
     * make a locator key from two block headers
     * 
     * @param type
     *            the type of the key
     * @param hash
     *            the hash of the key
     * @return the locator key
     */
    public static LocatorKey parseLocatorKey(String type, String hash) {
	return parseLocatorKey(type, Base16.decode(hash));
    }

    /**
     * make a locator key from the two logical parts.
     * 
     * @param type
     *            the type of the key
     * @param hash
     *            the hash of the key
     * @return the locator key
     */
    public static LocatorKey parseLocatorKey(String type, byte[] hash) {
	if (type.equals(KeyType.BLOB)) {
	    return new BlobLocatorKey(hash);
	}

	if (type.equals(KeyType.CONTENT_HASH)) {
	    return new ContentLocatorKey(hash);
	}

	if (type.equals(KeyType.NAME_HASH)) {
	    return new NameLocatorKey(hash);
	}

	throw new IllegalArgumentException("unknown type: " + type);
    }

    public static BlobLocatorKey parseBlobLocatorKey(String hash) {
	return new BlobLocatorKey(Base16.decode(hash));
    }

}
