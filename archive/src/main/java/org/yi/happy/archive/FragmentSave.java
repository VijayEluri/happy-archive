package org.yi.happy.archive;

import java.io.IOException;

/**
 * Save fragments to named files. This is an interface to allow for overriding
 * by tests.
 */
public interface FragmentSave {
    /**
     * Save a fragment.
     * 
     * @param path
     *            the name of the entry.
     * @param fragment
     *            the fragment to save.
     * @throws IOException
     *             on error.
     */
    public void save(String path, Fragment fragment) throws IOException;

    /**
     * Close any open files.
     * 
     * @throws IOException
     *             on error.
     */
    public void close() throws IOException;
}
