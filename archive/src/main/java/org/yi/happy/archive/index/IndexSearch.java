package org.yi.happy.archive.index;

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

import org.yi.happy.archive.key.LocatorKey;

import com.google.inject.Inject;

/**
 * The index search algorithm.
 */
public class IndexSearch {
    private final IndexStore index;

    /**
     * create the searcher with context.
     * 
     * @param index
     *            the index store.
     */
    @Inject
    public IndexSearch(IndexStore index) {
        this.index = index;
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
    public void search(Set<LocatorKey> want, Handler handler) throws IOException, InterruptedException {
        ExecutorService exec = Executors.newFixedThreadPool(2);
        try {
            /*
             * launch tasks
             */
            Queue<SearchVolume> searches = new ArrayDeque<SearchVolume>();
            for (String set : index.listVolumeSets()) {
                for (String name : index.listVolumeNames(set)) {
                    SearchVolume task = new SearchVolume(set, name, want);
                    task.setResult(exec.submit(task));
                    searches.add(task);
                }
            }

            /*
             * process results
             */
            while (searches.isEmpty() == false) {
                SearchVolume search = searches.remove();
                try {
                    for (IndexEntry entry : search.getResult()) {
                        handler.gotResult(search.getVolumeSet(), search.getVolumeName(), entry);
                    }
                } catch (ExecutionException e) {
                    handler.gotException(search.getVolumeSet(), search.getVolumeName(), e.getCause());
                }
            }
        } finally {
            exec.shutdownNow();
        }
    }

    /**
     * Search one volume for a set of keys
     * 
     * @param volumeSet
     *            the volume set name
     * @param volumeName
     *            the volume name
     * @param want
     *            the set of keys to search for
     * @return index entries matching any of the given keys
     * @throws IOException
     *             on read error
     */
    public List<IndexEntry> searchOne(String volumeSet, String volumeName, Set<LocatorKey> want) throws IOException {
        List<IndexEntry> out = new ArrayList<IndexEntry>();

        Reader in = index.open(volumeSet, volumeName);
        try {
            for (IndexEntry entry : new IndexIterator(in)) {
                if (!entry.getLoader().equals("plain")) {
                    continue;
                }
                if (want.contains(entry.getKey())) {
                    out.add(entry);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            throw new IOException("in " + volumeSet + "/" + volumeName, e);
        } finally {
            in.close();
        }
        return out;
    }

    private class SearchVolume implements Callable<List<IndexEntry>> {
        private final String volumeSet;
        private final String volumeName;
        private final Set<LocatorKey> want;
        private Future<List<IndexEntry>> result;

        public SearchVolume(String volumeSet, String volumeName, Set<LocatorKey> want) {
            this.volumeSet = volumeSet;
            this.volumeName = volumeName;
            this.want = want;
        }

        public void setResult(Future<List<IndexEntry>> result) {
            this.result = result;
        }

        @Override
        public List<IndexEntry> call() throws Exception {
            return searchOne(volumeSet, volumeName, want);
        }

        public String getVolumeSet() {
            return volumeSet;
        }

        public String getVolumeName() {
            return volumeName;
        }

        public List<IndexEntry> getResult() throws InterruptedException, ExecutionException {
            return result.get();
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
         * @param volumeSet
         *            the volume set.
         * @param volumeName
         *            the volume name.
         * @param result
         *            the search result.
         */
        void gotResult(String volumeSet, String volumeName, IndexEntry result);

        /**
         * Called for each search failure.
         * 
         * @param volumeSet
         *            the volume set.
         * @param volumeName
         *            the volume name.
         * @param cause
         *            the cause of the failure.
         */
        void gotException(String volumeSet, String volumeName, Throwable cause);
    }

    /**
     * @return the volume sets in the index
     * @throws IOException
     *             on read error
     */
    public List<String> listVolumeSets() throws IOException {
        return index.listVolumeSets();
    }

    /**
     * @param volumeSet
     *            the volume set in the index
     * @return the volume names in the volume set
     * @throws IOException
     *             on read error
     */
    public List<String> listVolumeNames(String volumeSet) throws IOException {
        return index.listVolumeNames(volumeSet);
    }
}
