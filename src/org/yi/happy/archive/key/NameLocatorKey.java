package org.yi.happy.archive.key;

import org.yi.happy.archive.Bytes;


/**
 * a name locator key.
 */
public final class NameLocatorKey extends AbstractLocatorKey implements
	LocatorKey {

    /**
     * create
     * 
     * @param hash
     *            the hash
     */
    public NameLocatorKey(Bytes hash) {
	super(hash);
    }

    public String getType() {
	return KeyType.NAME_HASH;
    }

    @Override
    public NameLocatorKey toLocatorKey() {
	return this;
    }
}
