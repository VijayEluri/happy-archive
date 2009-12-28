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
    public NameLocatorKey(byte[] hash) {
        super(hash);
    }

    public String getType() {
        return KeyType.NAME_HASH;
    }
}
