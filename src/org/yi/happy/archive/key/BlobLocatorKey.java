package org.yi.happy.archive.key;

public final class BlobLocatorKey extends AbstractLocatorKey implements
	LocatorKey {

    public BlobLocatorKey(byte[] hash) {
	super(hash);
    }

    public String getType() {
	return KeyType.BLOB;
    }

    @Override
    public BlobLocatorKey toLocatorKey() {
	return this;
    }
}
