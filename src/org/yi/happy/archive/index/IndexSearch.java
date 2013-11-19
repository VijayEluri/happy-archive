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
    public void search(Set<LocatorKey> want, Handler handler)
            throws IOException, InterruptedException {
        ExecutorService exec = Executors.newFixedThreadPool(2);

        /*
         * get the tasks
         */
        List<Callable<List<IndexSearchResult>>> tasks = getSearchTasks(want);

        /*
         * launch tasks
         */
        Queue<Future<List<IndexSearchResult>>> results = new ArrayDeque<Future<List<IndexSearchResult>>>();
        for (Callable<List<IndexSearchResult>> task : tasks) {
            results.add(exec.submit(task));
        }

        /*
         * process results
         */
        while (results.isEmpty() == false) {
            Future<List<IndexSearchResult>> result = results.remove();
            try {
                for (IndexSearchResult i : result.get()) {
                    handler.gotResult(i);
                }
            } catch (ExecutionException e) {
                handler.gotException(e.getCause());
            }
        }

        exec.shutdown();
    }

    private List<Callable<List<IndexSearchResult>>> getSearchTasks(
            final Set<LocatorKey> want) throws IOException {
        final List<Callable<List<IndexSearchResult>>> out;
        out = new ArrayList<Callable<List<IndexSearchResult>>>();
        for (String set : index.listVolumeSets()) {
            for (String name : index.listVolumeNames(set)) {
                SearchVolume task = new SearchVolume(set, name, want);
                out.add(task);
            }
        }
        return out;
    }

    private class SearchVolume implements Callable<List<IndexSearchResult>> {
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
        public List<IndexSearchResult> call() throws Exception {
            List<IndexSearchResult> out = new ArrayList<IndexSearchResult>();

            Reader in = index.open(volumeSet, volumeName);
            try {
                for (IndexEntry entry : new IndexIterator(in)) {
                    if (want.contains(entry.getKey())) {
                        out.add(new IndexSearchResult(volumeSet, volumeName,
                                entry.getName(), entry.getKey()));
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                throw new IOException("in " + volumeSet + "/" + volumeName, e);
            } finally {
                in.close();
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
        void gotResult(IndexSearchResult result);

        /**
         * Called for each search failure.
         * 
         * @param cause
         *            the cause of the failure.
         */
        void gotException(Throwable cause);
    }
}
