package org.yi.happy.archive;

import java.io.InputStream;
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
@UsesArgs({ "volume-set", "volume-name" })
public class IndexSearchOneMain implements MainCommand {
    private List<String> args;
    private PrintStream out;
    private InputStream in;
    private IndexSearch index;

    @Inject
    public IndexSearchOneMain(@EnvArgs List<String> args, @EnvIn InputStream in, @EnvOut PrintStream out,
            IndexSearch index) {
        this.args = args;
        this.in = in;
        this.out = out;
        this.index = index;
    }

    @Override
    public void run() throws Exception {
        String volumeSet = args.get(0);
        String volumeName = args.get(1);
        Set<LocatorKey> want = IndexSearchMain.loadKeyList();

        List<IndexEntry> found = index.searchOne(volumeSet, volumeName, want);

        for (IndexEntry result : found) {
            out.println(volumeSet + "\t" + volumeName + "\t" + result.getName() + "\t" + result.getKey());
        }
    }
}
