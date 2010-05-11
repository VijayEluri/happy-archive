package org.yi.happy.archive.key;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.yi.happy.annotate.SmellsMessy;
import org.yi.happy.archive.crypto.DigestFactory;

/**
 * parser for {@link LocatorKey}, and {@link FullKey}.
 */
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
     * parse a {@link FullKey}.
     * 
     * @param key
     *            the string form of the key.
     * @return the full key.
     * @throws IllegalArgumentException
     *             if the key does not parse.
     */
    @SmellsMessy
    public static FullKey parseFullKey(String key) {
        Matcher m = BLOB_KEY.matcher(key);
        if (m.matches()) {
            return parseBlobFullKey(m.group(1), m.group(2));
        }

        m = CONTENT_KEY.matcher(key);
        if (m.matches()) {
            return new ContentFullKey(new HashValue(m.group(1)), new PassValue(
                    m.group(2)));
        }

        m = NAME_KEY.matcher(key);
        if (m.matches()) {
            return new NameFullKey(DigestFactory.getProvider(m.group(1)), m
                    .group(2));
        }

        throw new IllegalArgumentException("can not parse key");
    }

    /**
     * finish the parsing of a {@link BlobFullKey}.
     * 
     * @param hash
     *            the hash part.
     * @param pass
     *            the pass part.
     * @return the parsed key.
     */
    public static BlobFullKey parseBlobFullKey(String hash, String pass) {
        return new BlobFullKey(new HashValue(hash), new PassValue(pass));
    }

    /**
     * finish the parsing of a {@link ContentFullKey}.
     * 
     * @param hash
     *            the hash part.
     * @param pass
     *            the pass part.
     * @return the parsed key.
     */
    public static ContentFullKey parseContentFullKey(String hash, String pass) {
        return new ContentFullKey(new HashValue(hash), new PassValue(pass));
    }

    /**
     * finish the parsing of a {@link NameFullKey}.
     * 
     * @param digest
     *            the digest part.
     * @param name
     *            the name part.
     * @return the parsed key.
     */
    public static NameFullKey parseNameFullKey(String digest, String name) {
        return new NameFullKey(DigestFactory.getProvider(digest), name);
    }

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
