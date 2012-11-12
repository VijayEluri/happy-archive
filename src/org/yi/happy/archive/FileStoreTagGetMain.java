package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.commandLine.UsesInput;
import org.yi.happy.archive.commandLine.UsesNeed;
import org.yi.happy.archive.commandLine.UsesStore;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.FullKeyParse;
import org.yi.happy.archive.tag.RestoreManager;
import org.yi.happy.archive.tag.Tag;
import org.yi.happy.archive.tag.TagStreamIterator;

/**
 * A program to restore tags.
 */
@UsesStore
@UsesNeed
@UsesInput("tag-list")
public class FileStoreTagGetMain implements MainCommand {
    private final FileSystem fs;
    private final WaitHandler waitHandler;
    private final InputStream in;
    private String pendingFile;
    private final PrintStream err;
    private final BlockStore store;

    /**
     * initialize.
     * 
     * @param store
     *            the block store to use
     * @param fs
     *            the file system to use.
     * @param waitHandler
     *            what to do when waiting is needed.
     * @param in
     *            what to use for standard input.
     * @param err
     * @param out
     *            what to use for standard output.
     */
    public FileStoreTagGetMain(BlockStore store, FileSystem fs,
            WaitHandler waitHandler, InputStream in, PrintStream err) {
        this.store = store;
        this.fs = fs;
        this.waitHandler = waitHandler;
        this.in = in;
        this.err = err;
    }

    /**
     * run the program.
     * 
     * @param args
     *            store base path; request list.
     * @throws IOException
     */
    @Override
    public void run(Env env) throws IOException {
        pendingFile = env.getNeed();
        RestoreManager restore = new RestoreManager(fs,
                new RetrieveBlockStorage(store));

        for (Tag i : new TagStreamIterator(in)) {
            String name = i.get("name");
            if (name == null) {
                continue;
            }
            String type = i.get("type");
            if (type == null) {
                continue;
            }
            String data = i.get("data");
            if (data == null) {
                continue;
            }

            FullKey k;
            try {
                k = FullKeyParse.parseFullKey(data);
            } catch (IllegalArgumentException e) {
                continue;
            }

            if (type.equals("file")) {
                restore.addFile(name, k);
                continue;
            }
        }

        restore.step();
        while (!restore.isDone()) {
            notReady(restore);
            restore.step();
        }
    }

    private void notReady(RestoreManager reader) throws IOException {
        /*
         * XXX near duplicate from FileStoreFileGetMain.notReady(RestoreManager)
         */
        StringBuilder p = new StringBuilder();
        for (FullKey k : reader.getPending()) {
            p.append(k.toLocatorKey() + "\n");
        }
        fs.save(pendingFile + ".tmp", ByteString.toUtf8(p.toString()));
        fs.rename(pendingFile + ".tmp", pendingFile);

        waitHandler.doWait(progress != reader.getProgress());
        progress = reader.getProgress();
    }

    private int progress;
}
