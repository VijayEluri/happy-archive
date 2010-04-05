package org.yi.happy.archive.key;


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
    public NameLocatorKey(HashValue hash) {
        super(hash);
    }

    @Override
    public String getType() {
        return KeyType.NAME_HASH;
    }

    @Override
    public NameLocatorKey toLocatorKey() {
        return this;
    }
}
