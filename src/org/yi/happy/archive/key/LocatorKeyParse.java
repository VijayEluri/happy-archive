package org.yi.happy.archive.key;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for {@link LocatorKey}s.
 */
public class LocatorKeyParse {

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
     * parse a {@link LocatorKey}.
     * 
     * @param key
     *            the string form of the key.
     * @return the parsed key.
     * @throws IllegalArgumentException
     *             if the key does not parse.
     */
    public static LocatorKey parseLocatorKey(String key) {
        Matcher m;

        m = BLOB_LOCATOR.matcher(key);
        if (m.matches()) {
            return parseBlobLocatorKey(m.group(1));
        }

        m = CONTENT_LOCATOR.matcher(key);
        if (m.matches()) {
            return parseContentLocatorKey(m.group(1));
        }

        m = NAME_LOCATOR.matcher(key);
        if (m.matches()) {
            return parseNameLocatorKey(m.group(1));
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
        if (type.equals(KeyType.BLOB)) {
            return parseBlobLocatorKey(hash);
        }

        if (type.equals(KeyType.CONTENT_HASH)) {
            return parseContentLocatorKey(hash);
        }

        if (type.equals(KeyType.NAME_HASH)) {
            return parseNameLocatorKey(hash);
        }

        throw new IllegalArgumentException("unknown type: " + type);
    }

    /**
     * finish the parsing of a blob locator key.
     * 
     * @param hash
     *            the hash part.
     * @return the key.
     */
    public static BlobLocatorKey parseBlobLocatorKey(String hash) {
        return new BlobLocatorKey(new HashValue(hash));
    }

    /**
     * finish the parsing of a content locator key.
     * 
     * @param hash
     *            the hash part.
     * @return the key.
     */
    public static ContentLocatorKey parseContentLocatorKey(String hash) {
        return new ContentLocatorKey(new HashValue(hash));
    }

    /**
     * finish the parsing of a name locator key.
     * 
     * @param hash
     *            the hash part.
     * @return the key.
     */
    public static NameLocatorKey parseNameLocatorKey(String hash) {
        return new NameLocatorKey(new HashValue(hash));
    }
}
