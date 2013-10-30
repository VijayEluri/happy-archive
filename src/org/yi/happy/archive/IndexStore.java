package org.yi.happy.archive;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * A collection of volume indexes.
 */
public interface IndexStore {
    /**
     * List all the volume sets in this collection.
     * 
     * @return a list of all the volume sets in this collection.
     * @throws IOException
     *             on error.
     */
    List<String> listVolumeSets() throws IOException;

    /**
     * List all the volume index names in a volume set.
     * 
     * @param volumeSet
     *            the volume set name.
     * @return a list of all the volume indexes in this volume set.
     * @throws IOException
     *             on error.
     */
    List<String> listVolumeNames(String volumeSet) throws IOException;

    /**
     * Open a volume index for reading.
     * 
     * @param volumeSet
     *            the volume set name.
     * @param volumeName
     *            the volume index name.
     * @return an open reader.
     * @throws IOException
     *             on error.
     */
    Reader open(String volumeSet, String volumeName) throws IOException;
}
