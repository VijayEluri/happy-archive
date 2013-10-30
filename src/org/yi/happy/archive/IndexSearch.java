package org.yi.happy.archive;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.key.LocatorKey;
import org.yi.happy.archive.key.LocatorKeyParse;

/**
 * The index search algorithm.
 */
public class IndexSearch {
    private final IndexStore index;

    /**
     * create the searcher with context.
     * 
     * @param fs
     *            the file system.
     * @param indexBase
     *            the base path of the index.
     */
    public IndexSearch(FileSystem fs, String indexBase) {
        this.index = new IndexStoreFileSystem(fs, indexBase);
    }

    /**
     * Run the search. This actually does the processing using two background
     * threads, but the results handler is called in the calling thread.
     * 
     * @param want
     *            what to search for
     * @param handler
     *            what to call with matches.
     * @throws IOException
     * @throws InterruptedException
     */
    public void search(Set<LocatorKey> want, Handler handler)
            throws IOException, InterruptedException {
        ExecutorService exec = Executors.newFixedThreadPool(2);

        /*
         * get the tasks
         */
        List<Callable<List<SearchResult>>> tasks = getSearchTasks(want);

        /*
         * launch tasks
         */
        Queue<Future<List<SearchResult>>> results = new ArrayDeque<Future<List<SearchResult>>>();
        for (Callable<List<SearchResult>> task : tasks) {
            results.add(exec.submit(task));
        }

        /*
         * process results
         */
        while (results.isEmpty() == false) {
            Future<List<SearchResult>> result = results.remove();
            try {
                for (SearchResult i : result.get()) {
                    handler.gotResult(i);
                }
            } catch (ExecutionException e) {
                handler.gotException(e.getCause());
            }
        }

        exec.shutdown();
    }

    private List<Callable<List<SearchResult>>> getSearchTasks(
            final Set<LocatorKey> want) throws IOException {
        final List<Callable<List<SearchResult>>> out;
        out = new ArrayList<Callable<List<SearchResult>>>();
        for (String set : index.listVolumeSets()) {
            for (String name : index.listVolumeNames(set)) {
                SearchVolume task = new SearchVolume(set, name, want);
                out.add(task);
            }
        }
        return out;
    }

    private class SearchVolume implements Callable<List<SearchResult>> {
        private final String volumeSet;
        private final String volumeName;
        private final Set<LocatorKey> want;

        public SearchVolume(String volumeSet, String volumeName,
                Set<LocatorKey> want) {
            this.volumeSet = volumeSet;
            this.volumeName = volumeName;
            this.want = want;
        }

        @Override
        public List<SearchResult> call() throws Exception {
            List<SearchResult> out = new ArrayList<SearchResult>();

            Reader in0 = index.open(volumeSet, volumeName);
            try {
                LineCursor in = new LineCursor(in0);
                while (in.next()) {
                    String[] line = in.get().split("\t", -1);
                    LocatorKey key = LocatorKeyParse.parseLocatorKey(line[2]);
                    if (want.contains(key)) {
                        String fileName = line[0];
                        out.add(new SearchResult(volumeSet, volumeName,
                                fileName, key));
                    }
                }
            } finally {
                in0.close();
            }
            return out;
        }
    }

    /**
     * The search result handler. This is called in the initiating thread for
     * each search result found.
     */
    public interface Handler {
        /**
         * Called for each index search result.
         * 
         * @param result
         *            the search result.
         */
        void gotResult(SearchResult result);

        /**
         * Called for each search failure.
         * 
         * @param cause
         *            the cause of the failure.
         */
        void gotException(Throwable cause);
    }

    /**
     * A search result data object.
     */
    public static class SearchResult {

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
        public SearchResult(String volumeSet, String volumeName,
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
    }
}
