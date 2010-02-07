package org.yi.happy.archive.key;

import org.yi.happy.archive.Bytes;

public final class BlobLocatorKey extends AbstractLocatorKey implements
	LocatorKey {

    public BlobLocatorKey(Bytes hash) {
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
