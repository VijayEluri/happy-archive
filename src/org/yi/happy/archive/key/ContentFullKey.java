package org.yi.happy.archive.key;

import org.yi.happy.archive.Bytes;

/**
 * a content full key. what is required to decode a content block.
 */
public final class ContentFullKey extends AbstractContentFullKey implements
        FullKey {

    @Override
    public String getType() {
        return KeyType.CONTENT_HASH;
    }

    /**
     * create
     * 
     * @param hash
     *            the hash
     * @param pass
     *            the cipher key
     */
    public ContentFullKey(Bytes hash, Bytes pass) {
        super(hash, pass);
    }

    @Override
    public ContentLocatorKey toLocatorKey() {
        return new ContentLocatorKey(getHash());
    }
}
