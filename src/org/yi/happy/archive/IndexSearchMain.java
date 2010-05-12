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

        Set<LocatorKey> want = new HashSet<LocatorKey>();

        InputStream in0 = fs.openInputStream(args[1]);
        try {
            LineCursor in = new LineCursor(in0);
            while (in.next()) {
                want.add(LocatorKeyParse.parseLocatorKey(in.get()));
            }
        } finally {
            in0.close();
        }

        /*
         * scan each index in sequence listing matching entries.
         */
        List<String> volumeSets = new ArrayList<String>(fs.list(args[0]));
        Collections.sort(volumeSets);
        for (String volumeSet : volumeSets) {
            List<String> volumeNames = new ArrayList<String>(fs.list(fs.join(
                    args[0], volumeSet)));
            Collections.sort(volumeNames);
            for (String volumeName : volumeNames) {
                String fileName = fs.join(fs.join(args[0], volumeSet),
                        volumeName);
                in0 = fs.openInputStream(fileName);
                try {
                    if (volumeName.endsWith(".gz")) {
                        in0 = new GZIPInputStream(in0);
                        volumeName = volumeName.substring(0, volumeName
                                .length() - 3);
                    }
                    LineCursor in = new LineCursor(in0);
                    while (in.next()) {
                        String[] line = in.get().split("\t", -1);
                        if (want.contains(LocatorKeyParse.parseLocatorKey(line[2]))) {
                            out.write(volumeSet + "\t" + volumeName + "\t"
                                    + line[0] + "\t" + line[2] + "\n");
                        }
                    }
                } finally {
                    in0.close();
                }
            }
        }
    }
}
