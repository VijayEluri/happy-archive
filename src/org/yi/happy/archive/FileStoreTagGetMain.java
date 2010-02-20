package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;

import org.yi.happy.annotate.EntryPoint;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.file_system.RealFileSystem;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.KeyParse;
import org.yi.happy.archive.tag.RestoreManager;
import org.yi.happy.archive.tag.Tag;
import org.yi.happy.archive.tag.TagParse;

/**
 * A program to restore tags.
 */
public class FileStoreTagGetMain {
    private final FileSystem fs;
    private final WaitHandler waitHandler;
    private final InputStream in;
    private String pendingFile;

    /**
     * initialize.
     * 
     * @param fs
     *            the file system to use.
     * @param waitHandler
     *            what to do when waiting is needed.
     * @param in
     *            what to use for standard input.
     */
    public FileStoreTagGetMain(FileSystem fs, WaitHandler waitHandler,
	    InputStream in) {
	this.fs = fs;
	this.waitHandler = waitHandler;
	this.in = in;
    }

    /**
     * run the program.
     * 
     * @param args
     *            store base path; request list.
     * @throws IOException
     */
    @EntryPoint
    public void run(String... args) throws IOException {
	FileBlockStore store = new FileBlockStore(fs, args[0]);
	pendingFile = args[1];
	RestoreManager restore = new RestoreManager(fs,
		new RetrieveBlockStorage(store));

	byte[] buff = Streams.load(in);
	for (Tag i : TagParse.parse(buff)) {
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
		k = KeyParse.parseFullKey(data);
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
     * @param args
     *            the store; the request list.
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
	FileSystem fs = new RealFileSystem();
	WaitHandler waitHandler = new WaitHandlerProgressiveDelay();
	InputStream in = System.in;

	new FileStoreTagGetMain(fs, waitHandler, in).run(args);
    }
}
