package org.yi.happy.archive;

import java.io.PrintStream;
import java.util.List;
import java.util.Set;

import org.yi.happy.archive.commandLine.UsesArgs;
import org.yi.happy.archive.commandLine.UsesIndexStore;
import org.yi.happy.archive.commandLine.UsesInput;
import org.yi.happy.archive.commandLine.UsesOutput;
import org.yi.happy.archive.index.IndexEntry;
import org.yi.happy.archive.index.IndexSearch;
import org.yi.happy.archive.key.LocatorKey;

import com.google.inject.Inject;

@UsesIndexStore
@UsesInput("key-list")
@UsesOutput("result")
@UsesArgs({ "volume-set" })
public class IndexSearchFirstMain implements MainCommand {

    private List<String> args;
    private PrintStream out;
    private IndexSearch index;

    @Inject
    public IndexSearchFirstMain(@EnvArgs List<String> args, IndexSearch index) {
        this.args = args;
        this.out = System.out;
        this.index = index;
    }

    @Override
    public void run() throws Exception {
        String volumeSet = args.get(0);
        Set<LocatorKey> want = IndexSearchMain.loadKeyList();

        for (String volumeName : index.listVolumeNames(volumeSet)) {
            List<IndexEntry> found = index.searchOne(volumeSet, volumeName, want);

            if (found.isEmpty()) {
                continue;
            }

            for (IndexEntry result : found) {
                out.println(volumeSet + "\t" + volumeName + "\t" + result.getName() + "\t" + result.getKey());
            }

            break;
        }
    }
}
