package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.yi.happy.archive.commandLine.UsesArgs;
import org.yi.happy.archive.commandLine.UsesIndexStore;
import org.yi.happy.archive.commandLine.UsesInput;
import org.yi.happy.archive.commandLine.UsesOutput;
import org.yi.happy.archive.index.IndexEntry;
import org.yi.happy.archive.index.IndexIterator;
import org.yi.happy.archive.index.IndexStore;
import org.yi.happy.archive.key.LocatorKey;
import org.yi.happy.archive.key.LocatorKeyParse;

@UsesIndexStore
@UsesInput("key-list")
@UsesOutput("result")
@UsesArgs({ "volume-set" })
public class IndexSearchFirstMain implements MainCommand {

    private List<String> args;
    private PrintStream out;
    private InputStream in;
    private IndexStore index;

    public IndexSearchFirstMain(List<String> args, InputStream in, PrintStream out, IndexStore index) {
        this.args = args;
        this.in = in;
        this.out = out;
        this.index = index;
    }

    @Override
    public void run() throws Exception {
        String volumeSet = args.get(0);
        Set<LocatorKey> want = loadKeyList();

        for (String volumeName : index.listVolumeNames(volumeSet)) {
            List<IndexEntry> found = scanVolume(volumeSet, volumeName, want);

            if (found.isEmpty()) {
                continue;
            }

            for (IndexEntry result : found) {
                out.println(volumeSet + "\t" + volumeName + "\t" + result.getName() + "\t" + result.getKey());
            }

            break;
        }
    }

    private List<IndexEntry> scanVolume(String volumeSet, String volumeName, Set<LocatorKey> want) throws IOException {
        List<IndexEntry> out = new ArrayList<>();

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

    private Set<LocatorKey> loadKeyList() throws IOException {
        Set<LocatorKey> keys = new HashSet<LocatorKey>();
        for (String line : new LineIterator(in)) {
            keys.add(LocatorKeyParse.parseLocatorKey(line));
        }
        return keys;
    }
}
