package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.yi.happy.annotate.EntryPoint;
import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.file_system.RealFileSystem;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.FullKeyParse;
import org.yi.happy.archive.tag.RestoreManager;
import org.yi.happy.archive.tag.Tag;
import org.yi.happy.archive.tag.TagStreamIterator;

/**
 * A program to restore tags.
 */
public class FileStoreTagGetMain {
    private final FileSystem fs;
    private final WaitHandler waitHandler;
    private final InputStream in;
    private String pendingFile;
    private final Writer out;

    /**
     * initialize.
     * 
     * @param fs
     *            the file system to use.
     * @param waitHandler
     *            what to do when waiting is needed.
     * @param in
     *            what to use for standard input.
     * @param out
     *            what to use for standard output.
     */
    public FileStoreTagGetMain(FileSystem fs, WaitHandler waitHandler,
            InputStream in, Writer out) {
        this.fs = fs;
        this.waitHandler = waitHandler;
        this.in = in;
        this.out = out;
    }

    /**
     * run the program.
     * 
     * @param args
     *            store base path; request list.
     * @throws IOException
     */
    @EntryPoint
    public void run(Env env) throws IOException {
        if (env.hasArguments() || env.hasNoStore() || env.hasNoNeed()) {
            out.write("use: --store store --need need.lst < tags\n");
            return;
        }

        FileBlockStore store = new FileBlockStore(fs, env.getStore());
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

    /**
     * invoke fetching file tags from standard in.
     * 
     * @param env
     *            The environment.
     * @throws IOException
     */
    public static void main(Env env) throws IOException {
        FileSystem fs = new RealFileSystem();
        WaitHandler waitHandler = new WaitHandlerProgressiveDelay();
        InputStream in = System.in;

        Writer out = new OutputStreamWriter(System.out, "UTF-8");
        try {
            new FileStoreTagGetMain(fs, waitHandler, in, out).run(env);
        } finally {
            out.flush();
        }
    }
}
