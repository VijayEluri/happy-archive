package org.yi.happy.archive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.yi.happy.annotate.DuplicatedLogic;
import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.commandLine.UsesIndex;
import org.yi.happy.archive.commandLine.UsesOutput;
import org.yi.happy.archive.commandLine.UsesStore;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.key.LocatorKey;

/**
 * Make a candidate list from a local store and local index.
 */
@UsesStore
@UsesIndex
@UsesOutput("key-list")
@DuplicatedLogic("LocalCandidateListMain")
public class CriticalListMain implements MainCommand {
    private final BlockStore store;
    private final Env env;
    private final FileSystem fs;

    /**
     * Set up the command to make a candidate list from a local store and local
     * index.
     * 
     * @param store
     *            the store.
     * @param env
     *            the invocation environment.
     */
    public CriticalListMain(BlockStore store, FileSystem fs, Env env) {
        this.store = store;
        this.fs = fs;
        this.env = env;
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
    public void run() throws IOException,
            InterruptedException {
        String indexBase = env.getIndex();

        /*
         * load list of keys in store.
         */
        final Set<LocatorKey> want = new HashSet<LocatorKey>();
        store.visit(new BlockStoreVisitor<RuntimeException>() {
            @Override
            public void accept(LocatorKey key) throws RuntimeException {
                want.add(key);
            }
        });

        /*
         * find keys in index.
         */
        final Set<LocatorKey> exists = new HashSet<LocatorKey>();
        IndexSearch search = new IndexSearch(fs, indexBase);
        search.search(want, new IndexSearch.Handler() {
            @Override
            public void gotResult(IndexSearch.SearchResult result) {
                exists.add(result.getKey());
            }

            @Override
            public void gotException(Throwable cause) {
                System.err.println(cause.getMessage());
            }

        });

        /*
         * calculate candidates: first the keys that do not exist in the index,
         * then the keys that do exist but not in this set.
         */
        Set<LocatorKey> nowhere = new HashSet<LocatorKey>(want);
        nowhere.removeAll(exists);

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
                System.out.println(i);
            }
        }
    }
}
