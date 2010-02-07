package org.yi.happy.archive.key;

import org.yi.happy.archive.Bytes;

public class BlobFullKey extends AbstractContentFullKey implements FullKey {
    public BlobFullKey(Bytes hash, Bytes pass) {
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
