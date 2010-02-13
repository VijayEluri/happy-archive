package org.yi.happy.archive.key;

import org.yi.happy.archive.Bytes;

/**
 * a content locator key
 */
public final class ContentLocatorKey extends AbstractLocatorKey implements
	LocatorKey {

    @Override
    public String getType() {
	return KeyType.CONTENT_HASH;
    }

    /**
     * create
     * 
     * @param hash
     *            the hash
     */
    public ContentLocatorKey(Bytes hash) {
	super(hash);
    }

    @Override
    public ContentLocatorKey toLocatorKey() {
	return this;
    }
}
