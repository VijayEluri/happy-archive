package org.yi.happy.archive;

import org.yi.happy.archive.key.LocatorKey;

/**
 * A search result data object.
 */
public class IndexSearchResult {
    private final String volumeSet;
    private final String volumeName;
    private final String fileName;
    private final LocatorKey key;

    /**
     * create.
     * 
     * @param volumeSet
     *            the volume set.
     * @param volumeName
     *            the volume name.
     * @param fileName
     *            the file name.
     * @param key
     *            the locator key.
     */
    public IndexSearchResult(String volumeSet, String volumeName,
            String fileName, LocatorKey key) {
        this.volumeSet = volumeSet;
        this.volumeName = volumeName;
        this.fileName = fileName;
        this.key = key;
    }

    @Override
    public String toString() {
        return volumeSet + "\t" + volumeName + "\t" + fileName + "\t" + key;
    }

    /**
     * @return the volume set for this entry.
     */
    public String getVolumeSet() {
        return volumeSet;
    }

    /**
     * @return the volume name for this entry.
     */
    public String getVolumeName() {
        return volumeName;
    }

    /**
     * @return the file name within the volume for this entry.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @return the key for this entry.
     */
    public LocatorKey getKey() {
        return key;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((fileName == null) ? 0 : fileName.hashCode());
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result
                + ((volumeName == null) ? 0 : volumeName.hashCode());
        result = prime * result
                + ((volumeSet == null) ? 0 : volumeSet.hashCode());
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
        IndexSearchResult other = (IndexSearchResult) obj;
        if (fileName == null) {
            if (other.fileName != null)
                return false;
        } else if (!fileName.equals(other.fileName))
            return false;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        if (volumeName == null) {
            if (other.volumeName != null)
                return false;
        } else if (!volumeName.equals(other.volumeName))
            return false;
        if (volumeSet == null) {
            if (other.volumeSet != null)
                return false;
        } else if (!volumeSet.equals(other.volumeSet))
            return false;
        return true;
    }
}