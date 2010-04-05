package org.yi.happy.archive.key;

import org.yi.happy.archive.Bytes;

/**
 * A locator key for a blob encoded block.
 */
public final class BlobLocatorKey extends AbstractLocatorKey implements
        LocatorKey {

    /**
     * Create a locator key for a blob encoded block.
     * 
     * @param hash
     *            the hash part of the key.
     */
    public BlobLocatorKey(Bytes hash) {
        super(hash);
    }

    @Override
    public String getType() {
        return KeyType.BLOB;
    }

    @Override
    public BlobLocatorKey toLocatorKey() {
        return this;
    }
}
