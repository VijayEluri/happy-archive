package org.yi.happy.archive;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.yi.happy.archive.commandLine.UsesArgs;
import org.yi.happy.archive.commandLine.UsesIndex;
import org.yi.happy.archive.commandLine.UsesOutput;
import org.yi.happy.archive.commandLine.UsesStore;
import org.yi.happy.archive.index.IndexSearch;
import org.yi.happy.archive.index.IndexSearchResult;
import org.yi.happy.archive.key.LocatorKey;

/**
 * Make a candidate list from a local store and local index.
 */
@UsesStore
@UsesIndex
@UsesArgs({ "volume-set" })
@UsesOutput("key-list")
public class LocalCandidateListMain implements MainCommand {
    private final BlockStore store;
    private final IndexSearch indexSearch;
    private final PrintStream out;
    private final PrintStream err;
    private final List<String> args;

    /**
     * Set up the command to make a candidate list from a local store and local
     * index.
     * 
     * @param store
     *            the store.
     * @param indexSearch
     *            the index search interface.
     * @param out
     *            the output stream.
     * @param err
     *            the error stream.
     * @param args
     *            the non-option arguments.
     */
    public LocalCandidateListMain(BlockStore store, IndexSearch indexSearch,
            PrintStream out, PrintStream err, List<String> args) {
        this.store = store;
        this.indexSearch = indexSearch;
        this.out = out;
        this.err = err;
        this.args = args;
    }

    /**
     * Make a candidate list from a local store and local index.
     * 
     * @param env
     *            the command line.
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public void run() throws IOException, InterruptedException {
        final String volumeSet = args.get(0);

        /*
         * load list of keys in store.
         */
        final Set<LocatorKey> want = new HashSet<LocatorKey>();
        for (LocatorKey key : store) {
            want.add(key);
        }

        /*
         * find keys in index.
         */
        final Set<LocatorKey> have = new HashSet<LocatorKey>();
        final Set<LocatorKey> exists = new HashSet<LocatorKey>();
        indexSearch.search(want, new IndexSearch.Handler() {
            @Override
            public void gotResult(IndexSearchResult result) {
                exists.add(result.getKey());
                if (result.getVolumeSet().equals(volumeSet)) {
                    have.add(result.getKey());
                }
            }

            @Override
            public void gotException(Throwable cause) {
                err.println(cause.getMessage());
            }
        });

        /*
         * calculate candidates: first the keys that do not exist in the index,
         * then the keys that do exist but not in this set.
         */
        Set<LocatorKey> nowhere = new HashSet<LocatorKey>(want);
        nowhere.removeAll(exists);

        exists.removeAll(have);

        /*
         * sort the lists by creation time, and print them out.
         */
        {
            final Map<LocatorKey, Long> order = new HashMap<LocatorKey, Long>();
            for (LocatorKey i : nowhere) {
                order.put(i, store.getTime(i));
            }

            List<LocatorKey> out = new ArrayList<LocatorKey>(nowhere);
            Collections.sort(out, new Comparator<LocatorKey>() {
                @Override
                public int compare(LocatorKey o1, LocatorKey o2) {
                    return order.get(o1).compareTo(order.get(o2));
                }
            });

            for (LocatorKey i : out) {
                this.out.println(i);
            }
        }

        {
            final Map<LocatorKey, Long> order = new HashMap<LocatorKey, Long>();
            for (LocatorKey i : exists) {
                order.put(i, store.getTime(i));
            }

            List<LocatorKey> out = new ArrayList<LocatorKey>(exists);
            Collections.sort(out, new Comparator<LocatorKey>() {
                @Override
                public int compare(LocatorKey o1, LocatorKey o2) {
                    return order.get(o1).compareTo(order.get(o2));
                }
            });

            for (LocatorKey i : out) {
                this.out.println(i);
            }
        }
    }
}
