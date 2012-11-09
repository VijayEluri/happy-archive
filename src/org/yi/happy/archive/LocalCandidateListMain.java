package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.zip.GZIPInputStream;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.yi.happy.annotate.DuplicatedLogic;
import org.yi.happy.annotate.SmellsMessy;
import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.file_system.RealFileSystem;
import org.yi.happy.archive.key.LocatorKey;
import org.yi.happy.archive.key.LocatorKeyParse;

/**
 * Make a candidate list from a local store and local index.
 */
public class LocalCandidateListMain {
    /**
     * Make a candidate list from a local store and local index.
     * 
     * @param args
     *            the command line.
     * @throws IOException
     * @throws InterruptedException
     */
    public static void launch(Env env) throws IOException,
            InterruptedException {
        if (env.hasNoStore() || env.hasNoIndex() || env.hasArgumentCount() != 1) {
            System.out.println("use: --store store --index index volume-set");
            return;
        }

        FileSystem fs = new RealFileSystem();

        FileBlockStore store = new FileBlockStore(fs, env.getStore());

        String indexBase = env.getIndex();

        final String volumeSet = env.getArgument(0);

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
        ExecutorService exec = Executors.newFixedThreadPool(2);

        Set<LocatorKey> have = new HashSet<LocatorKey>();
        Set<LocatorKey> exists = new HashSet<LocatorKey>();
        /*
         * scan each index in sequence listing matching entries.
         */
        Queue<Future<List<SearchResult>>> result = searchIndex(indexBase, want,
                exec);
        while (result.isEmpty() == false) {
            Future<List<SearchResult>> r = result.remove();
            try {
                for (SearchResult i : r.get()) {
                    exists.add(i.key);
                    if (i.volumeSet.equals(volumeSet)) {
                        have.add(i.key);
                    }
                }
            } catch (ExecutionException e) {
                System.err.println(e.getMessage());
            }
        }

        exec.shutdown();

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
                System.out.println(i);
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
                System.out.println(i);
            }
        }
    }

    @SmellsMessy
    @DuplicatedLogic("IndexSearchMain")
    private static Queue<Future<List<SearchResult>>> searchIndex(String path,
            final Set<LocatorKey> want, final ExecutorService exec)
            throws IOException {
        RealFileSystem fs = new RealFileSystem();
        final Queue<Future<List<SearchResult>>> out = new ArrayDeque<Future<List<SearchResult>>>();

        new IndexFileTree(fs, path).accept(new IndexFileTree.Visitor() {
            @Override
            public void visit(FileSystem fs, final String fileName,
                    final String volumeSet, final String volumeName)
                    throws IOException {
                Callable<List<SearchResult>> task = new Callable<List<SearchResult>>() {
                    @Override
                    public List<SearchResult> call() throws Exception {
                        return searchVolume(fileName, volumeSet, volumeName,
                                want);
                    }
                };
                out.add(exec.submit(task));
            }
        });
        return out;
    }

    @DuplicatedLogic("IndexSearchMain")
    private static List<SearchResult> searchVolume(String path,
            String volumeSet, String volumeName, Set<LocatorKey> want)
            throws IOException {
        RealFileSystem fs = new RealFileSystem();

        List<SearchResult> out = new ArrayList<SearchResult>();

        InputStream in0 = fs.openInputStream(path);
        try {
            if (volumeName.endsWith(".gz")) {
                in0 = new GZIPInputStream(in0);
                volumeName = volumeName.substring(0, volumeName.length() - 3);
            }
            LineCursor in = new LineCursor(in0);
            while (in.next()) {
                String[] line = in.get().split("\t", -1);
                LocatorKey key = LocatorKeyParse.parseLocatorKey(line[2]);
                if (want.contains(key)) {
                    String fileName = line[0];
                    out.add(new SearchResult(volumeSet, volumeName, fileName,
                            key));
                }
            }
        } finally {
            in0.close();
        }
        return out;
    }

    @DuplicatedLogic("IndexSearchMain")
    private static class SearchResult {
        private final String volumeSet;
        private final String volumeName;
        private final String fileName;
        private final LocatorKey key;

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
    }

    private static void showHelp(Options options, PrintStream out) {
        PrintWriter o = new PrintWriter(out);
        try {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printUsage(o, 80, "backup-list volume-set", options);
        } finally {
            o.flush();
        }
    }

}
