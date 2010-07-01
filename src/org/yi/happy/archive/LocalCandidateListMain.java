package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.zip.GZIPInputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.yi.happy.annotate.DuplicatedLogic;
import org.yi.happy.annotate.EntryPoint;
import org.yi.happy.annotate.SmellsMessy;
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
    @EntryPoint
    @DuplicatedLogic("IndexSearchMain index search")
    public static void main(String[] args) throws IOException,
            InterruptedException {
        FileSystem fs = new RealFileSystem();

        Options options = new Options()

        .addOption(null, "store", true, "location of the store")

        .addOption(null, "index", true, "location of the indexes");

        CommandLine cmd;
        try {
            cmd = new GnuParser().parse(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
            showHelp(options, System.out);
            return;
        }

        /*
         * get store location.
         */
        if (cmd.getOptionValue("store") == null) {
            showHelp(options, System.out);
            return;
        }

        FileBlockStore store = new FileBlockStore(fs, cmd
                .getOptionValue("store"));

        /*
         * get index location.
         */
        String indexBase = cmd.getOptionValue("index");

        if (cmd.getArgs().length != 1) {
            showHelp(options, System.out);
            return;
        }

        final String volumeSet = cmd.getArgs()[0];

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

        for (LocatorKey i : nowhere) {
            System.out.println(i);
        }
        for (LocatorKey i : exists) {
            System.out.println(i);
        }
    }

    @SmellsMessy
    @DuplicatedLogic("IndexSearchMain")
    private static Queue<Future<List<SearchResult>>> searchIndex(String path,
            final Set<LocatorKey> want, ExecutorService exec)
            throws IOException {
        RealFileSystem fs = new RealFileSystem();
        Queue<Future<List<SearchResult>>> out = new ArrayDeque<Future<List<SearchResult>>>();

        List<String> volumeSets = new ArrayList<String>(fs.list(path));
        Collections.sort(volumeSets);
        for (final String volumeSet : volumeSets) {
            if (!fs.isDir(fs.join(path, volumeSet))) {
                continue;
            }
            List<String> volumeNames = new ArrayList<String>(fs.list(fs.join(
                    path, volumeSet)));
            Collections.sort(volumeNames);
            for (final String volumeName : volumeNames) {
                if (volumeName.startsWith(".")) {
                    continue;
                }
                final String fileName = fs.join(fs.join(path, volumeSet),
                        volumeName);

                Callable<List<SearchResult>> task = new Callable<List<SearchResult>>() {

                    @Override
                    public List<SearchResult> call() throws Exception {
                        return searchVolume(fileName, volumeSet, volumeName,
                                want);
                    }
                };
                out.add(exec.submit(task));
            }
        }
        return out;
    }

    @DuplicatedLogic("IndexSearchMain")
    private static List<SearchResult> searchVolume(String path,
            String volumeSet,
            String volumeName, Set<LocatorKey> want) throws IOException {
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
            formatter.printUsage(o, 80, "backup-list volume-set",
                    options);
        } finally {
            o.flush();
        }
    }
}
