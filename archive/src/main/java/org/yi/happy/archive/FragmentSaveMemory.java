package org.yi.happy.archive;

import java.util.HashMap;
import java.util.Map;

/**
 * Save fragments to in memory byte arrays.
 */
public class FragmentSaveMemory implements FragmentSave {

    private Map<String, byte[]> data;

    /**
     * Set up.
     */
    public FragmentSaveMemory() {
        data = new HashMap<String, byte[]>();
    }

    /**
     * Get an entry.
     * 
     * @param path
     *            the name of the entry.
     * @return the entry, or null if it does not exist.
     */
    public byte[] get(String path) {
        return data.get(path);
    }

    @Override
    public void save(String path, Fragment fragment) {
        if (path == null) {
            throw new NullPointerException();
        }

        byte[] data = this.data.get(path);

        if (data == null) {
            data = new byte[(int) (fragment.getOffset() + fragment.getSize())];
        }

        if (data.length < fragment.getOffset() + fragment.getSize()) {
            byte[] tmp = new byte[(int) (fragment.getOffset() + fragment
                    .getSize())];
            System.arraycopy(data, 0, tmp, 0, data.length);
            data = tmp;
        }

        fragment.getData().getBytes(data, (int) fragment.getOffset());

        this.data.put(path, data);
    }

    @Override
    public void close() {
        // nothing
    }

}
