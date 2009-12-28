package org.yi.happy.archive.key;

/**
 * a content locator key
 */
public final class ContentLocatorKey extends AbstractLocatorKey implements
        LocatorKey {

    public String getType() {
        return KeyType.CONTENT_HASH;
    }

    /**
     * create
     * 
     * @param hash
     *            the hash
     */
    public ContentLocatorKey(byte[] hash) {
        super(hash);
    }
}
