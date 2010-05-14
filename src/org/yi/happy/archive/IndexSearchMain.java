package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import org.yi.happy.annotate.EntryPoint;
import org.yi.happy.annotate.SmellsMessy;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.file_system.RealFileSystem;
import org.yi.happy.archive.key.LocatorKey;
import org.yi.happy.archive.key.LocatorKeyParse;

/**
 * search indexes for keys.
 */
public class IndexSearchMain {
    private final FileSystem fs;
    private final Writer out;

    /**
     * create with context.
     * 
     * @param fs
     *            the file system.
     * @param out
     *            the output.
     */
    public IndexSearchMain(FileSystem fs, Writer out) {
        this.fs = fs;
        this.out = out;
    }

    /**
     * invoke from the command line.
     * 
     * @param args
     * @throws IOException
     */
    @EntryPoint
    public static void main(String[] args) throws IOException {
        FileSystem fs = new RealFileSystem();
        Writer out = new OutputStreamWriter(System.out, "utf-8");

        new IndexSearchMain(fs, out).run(args);

        out.flush();
    }

    /**
     * run the index search.
     * 
     * @param args
     *            the command line arguments.
     * @throws IOException
     */
    @SmellsMessy
    public void run(String... args) throws IOException {
        if (args.length != 2) {
            out.write("use: index key-list\n");
            return;
        }

        Set<LocatorKey> want = loadRequestSet(args[1]);

        /*
         * scan each index in sequence listing matching entries.
         */
        List<List<SearchResult>> result = searchIndex(args[0], want);
        for (List<SearchResult> r : result) {
            for (SearchResult i : r) {
                out.write(i.toString());
                out.write("\n");
            }
        }
    }

    private List<List<SearchResult>> searchIndex(String path,
            Set<LocatorKey> want)
            throws IOException {
        List<List<SearchResult>> out = new ArrayList<List<SearchResult>>();

        List<String> volumeSets = new ArrayList<String>(fs.list(path));
        Collections.sort(volumeSets);
        for (String volumeSet : volumeSets) {
            List<String> volumeNames = new ArrayList<String>(fs.list(fs.join(
                    path, volumeSet)));
            Collections.sort(volumeNames);
            for (String volumeName : volumeNames) {
                String fileName = fs.join(fs.join(path, volumeSet),
                        volumeName);
                List<SearchResult> result = searchVolume(fileName, volumeSet,
                        volumeName, want);
                out.add(result);
            }
        }
        return out;
    }

    private List<SearchResult> searchVolume(String path, String volumeSet,
            String volumeName, Set<LocatorKey> want) throws IOException {
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

    private Set<LocatorKey> loadRequestSet(String path) throws IOException {
        Set<LocatorKey> out = new HashSet<LocatorKey>();

        InputStream in0 = fs.openInputStream(path);
        try {
            LineCursor in = new LineCursor(in0);
            while (in.next()) {
                out.add(LocatorKeyParse.parseLocatorKey(in.get()));
            }
        } finally {
            in0.close();
        }

        return out;
    }
}
