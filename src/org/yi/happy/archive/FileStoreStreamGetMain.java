package org.yi.happy.archive;

import org.yi.happy.annotate.EntryPoint;

/**
 * Fetch a stream, the blocks may not all available in the file store, so the
 * ones that are needed are put in a list, and the process continues to be
 * retried until all the needed blocks become available.
 */
public class FileStoreStreamGetMain {

    /**
     * @param args
     *            file store base, request list, key to fetch
     */
    @EntryPoint
    public static void main(String[] args) {

    }
}
