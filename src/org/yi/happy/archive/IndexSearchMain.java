package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.yi.happy.archive.IndexSearch.SearchResult;
import org.yi.happy.archive.commandLine.UsesIndex;
import org.yi.happy.archive.commandLine.UsesInput;
import org.yi.happy.archive.commandLine.UsesOutput;
import org.yi.happy.archive.key.LocatorKey;
import org.yi.happy.archive.key.LocatorKeyParse;

/**
 * search indexes for keys.
 */
@UsesIndex
@UsesInput("key-list")
@UsesOutput("result")
public class IndexSearchMain implements MainCommand {
    private final PrintStream out;
    private final IndexSearch indexSearch;
    private final PrintStream err;
    private final InputStream in;

    /**
     * create with context.
     * 
     * @param in
     *            the input stream.
     * @param out
     *            the output stream.
     * @param err
     *            the error stream.
     * @param indexSearch
     *            the index search interface.
     */
    public IndexSearchMain(InputStream in, PrintStream out, PrintStream err,
            IndexSearch indexSearch) {
        this.in = in;
        this.out = out;
        this.err = err;
        this.indexSearch = indexSearch;
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
    public void run() throws IOException, InterruptedException,
            ExecutionException {
        Set<LocatorKey> keys = loadKeyList();

        indexSearch.search(keys, new IndexSearch.Handler() {
            @Override
            public void gotResult(SearchResult result) {
                out.println(result);
            }

            @Override
            public void gotException(Throwable cause) {
                err.println(cause.getMessage());
            }
        });

        out.flush();
    }

    private Set<LocatorKey> loadKeyList() throws IOException {
        Set<LocatorKey> keys = new HashSet<LocatorKey>();
        LineCursor line = new LineCursor(in);
        while (line.next()) {
            keys.add(LocatorKeyParse.parseLocatorKey(line.get()));
        }
        return keys;
    }
}
