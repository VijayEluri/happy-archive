package org.yi.happy.archive.index;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An index store that is entirely in memory, for testing.
 */
public class IndexStoreMemory implements IndexStore {

    private Map<String, Map<String, String>> data;

    /**
     * Set up blank.
     */
    public IndexStoreMemory() {
        this.data = new HashMap<String, Map<String, String>>();
    }

    /**
     * add a volume set to the store.
     * 
     * @param volumeSet
     *            the volume set name.
     */
    public void addVolumeSet(String volumeSet) {
        if (!data.containsKey(volumeSet)) {
            data.put(volumeSet, new HashMap<String, String>());
        }
    }

    /**
     * add a volume to the store.
     * 
     * @param volumeSet
     *            the volume set name.
     * @param volumeName
     *            the volume index name.
     * @param content
     *            the content of the index.
     */
    public void addVolume(String volumeSet, String volumeName, String content) {
        if (!data.containsKey(volumeSet)) {
            data.put(volumeSet, new HashMap<String, String>());
        }
        data.get(volumeSet).put(volumeName, content);
    }

    @Override
    public List<String> listVolumeSets() throws IOException {
        List<String> out = new ArrayList<String>(data.keySet());
        Collections.sort(out);
        return out;
    }

    @Override
    public List<String> listVolumeNames(String volumeSet) throws IOException {
        if (data.get(volumeSet) == null) {
            return Collections.emptyList();
        }
        List<String> out = new ArrayList<String>(data.get(volumeSet).keySet());
        Collections.sort(out);
        return out;
    }

    @Override
    public Reader open(String volumeSet, String volumeName) throws IOException {
        if (data.get(volumeSet) == null) {
            throw new FileNotFoundException();
        }
        if (data.get(volumeSet).get(volumeName) == null) {
            throw new FileNotFoundException();
        }
        return new StringReader(data.get(volumeSet).get(volumeName));
    }
}
