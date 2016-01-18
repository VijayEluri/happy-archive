package org.yi.happy.archive.index;

import org.yi.happy.archive.key.LocatorKey;

/**
 * An entry (line) in a volume index.
 */
public final class IndexEntry {

    private final String name;
    private final String loader;
    private final LocatorKey key;
    private final String hash;
    private final String size;

    /**
     * @param name
     *            the name of the file on the volume.
     * @param loader
     *            the loader to use.
     * @param key
     *            the key of the block.
     * @param hash
     *            the hash of the normalized block.
     * @param size
     *            the size of the normalized block.
     */
    public IndexEntry(String name, String loader, LocatorKey key, String hash,
            String size) {
        this.name = name;
        this.loader = loader;
        this.key = key;
        this.hash = hash;
        this.size = size;
    }

    /**
     * @return the name of the file on the volume.
     */
    public String getName() {
        return name;
    }

    /**
     * @return the loader to use.
     */
    public String getLoader() {
        return loader;
    }

    /**
     * @return the key of the block.
     */
    public LocatorKey getKey() {
        return key;
    }

    /**
     * @return the hash of the normalized block.
     */
    public String getHash() {
        return hash;
    }

    /**
     * @return the size of the normalized block.
     */
    public String getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "IndexEntry [name=" + name + ", loader=" + loader + ", key="
                + key + ", hash=" + hash + ", size=" + size + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((hash == null) ? 0 : hash.hashCode());
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((loader == null) ? 0 : loader.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((size == null) ? 0 : size.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        IndexEntry other = (IndexEntry) obj;
        if (hash == null) {
            if (other.hash != null)
                return false;
        } else if (!hash.equals(other.hash))
            return false;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        if (loader == null) {
            if (other.loader != null)
                return false;
        } else if (!loader.equals(other.loader))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (size == null) {
            if (other.size != null)
                return false;
        } else if (!size.equals(other.size))
            return false;
        return true;
    }
}
