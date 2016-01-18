package org.yi.happy.archive.key;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.yi.happy.annotate.SmellsMessy;
import org.yi.happy.archive.crypto.DigestFactory;

/**
 * Parser for {@link FullKey}s.
 */
public class FullKeyParse {
    private FullKeyParse() {

    }

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
            return parseContentFullKey(m.group(1), m.group(2));
        }

        m = NAME_KEY.matcher(key);
        if (m.matches()) {
            return parseNameFullKey(m.group(1), m.group(2));
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
}
