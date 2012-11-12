package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.yi.happy.annotate.SmellsMessy;
import org.yi.happy.archive.IndexSearch.SearchResult;
import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.commandLine.UsesArgs;
import org.yi.happy.archive.commandLine.UsesIndex;
import org.yi.happy.archive.commandLine.UsesOutput;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.key.LocatorKey;
import org.yi.happy.archive.key.LocatorKeyParse;

/**
 * search indexes for keys.
 */
@UsesIndex
@UsesArgs("key-list")
@UsesOutput("result")
public class IndexSearchMain implements MainCommand {
    private final FileSystem fs;
    private final PrintStream out;

    /**
     * create with context.
     * 
     * @param fs
     *            the file system.
     * @param out
     *            the output.
     */
    public IndexSearchMain(FileSystem fs, PrintStream out) {
        this.fs = fs;
        this.out = out;
    }

    /**
     * run the index search.
     * 
     * @param env
     *            the command line arguments.
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Override
    @SmellsMessy
    public void run(Env env) throws IOException, InterruptedException,
            ExecutionException {
        Set<LocatorKey> want = loadRequestSet(env.getArgument(0));

        IndexSearch search = new IndexSearch(fs, env.getIndex());
        search.search(want, new IndexSearch.Handler() {
            @Override
            public void gotResult(SearchResult result) {
                out.println(result);
            }

            @Override
            public void gotException(Throwable cause) {
                System.err.println(cause.getMessage());
            }
        });

        out.flush();
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
