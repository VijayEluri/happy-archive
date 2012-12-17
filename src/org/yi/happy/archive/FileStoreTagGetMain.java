package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.yi.happy.archive.commandLine.UsesInput;
import org.yi.happy.archive.commandLine.UsesNeed;
import org.yi.happy.archive.commandLine.UsesStore;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.FullKeyParse;
import org.yi.happy.archive.key.LocatorKey;
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
    private final NeedHandler needHandler;
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
     * @param needHandler
     *            what to do when there are blocks needed.
     * @param env
     *            the invocation environment.
     * @param out
     *            what to use for standard output.
     */
    public FileStoreTagGetMain(BlockStore store, FileSystem fs,
            WaitHandler waitHandler, InputStream in, NeedHandler needHandler) {
        this.store = store;
        this.fs = fs;
        this.waitHandler = waitHandler;
        this.in = in;
        this.needHandler = needHandler;
    }

    /**
     * run the program.
     * 
     * @param args
     *            store base path; request list.
     * @throws IOException
     */
    @Override
    public void run() throws IOException {
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
                /*
                 * TODO make sure that the parent directories exist
                 */

                restore.addFile(name, k);
                continue;
            }

            /*
             * TODO there is also a type=dir where the content is a tag list.
             */
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
        List<LocatorKey> keys = new ArrayList<LocatorKey>();
        for (FullKey key : reader.getPending()) {
            keys.add(key.toLocatorKey());
        }
        needHandler.post(keys);

        waitHandler.doWait(progress != reader.getProgress());
        progress = reader.getProgress();
    }

    private int progress;
}
