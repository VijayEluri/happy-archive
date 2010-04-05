package org.yi.happy.archive.key;

import org.yi.happy.archive.Bytes;

/**
 * A full key for a blob encoded block.
 */
public class BlobFullKey extends AbstractContentFullKey implements FullKey {
    /**
     * create a full key for a blob encoded block.
     * 
     * @param hash
     *            the hash part of the key.
     * @param pass
     *            the encryption key part of the key.
     */
    public BlobFullKey(HashValue hash, Bytes pass) {
        super(hash, pass);
    }

    @Override
    public String getType() {
        return KeyType.BLOB;
    }

    @Override
    public BlobLocatorKey toLocatorKey() {
        return new BlobLocatorKey(getHash());
    }
}
