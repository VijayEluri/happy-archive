package org.yi.happy.archive;

import java.io.IOException;
import java.io.InterruptedIOException;

import org.yi.happy.annotate.EntryPoint;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.file_system.RandomOutputFile;
import org.yi.happy.archive.file_system.RealFileSystem;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.KeyParse;

@EntryPoint
public class FileStoreFileGetMain {
    public static void main(String[] args) throws IOException {
	FileSystem fs = new RealFileSystem();

	/*
	 * XXX duplicate of the wait handler in FileStoreStreamGetMain, they
	 * should be extracted to a shared class.
	 */
	WaitHandler waitHandler = new WaitHandler() {
	    private int delay = 1;
	    private int lastDelay = 0;

	    @Override
	    public void doWait(boolean progress) throws IOException {
		try {
		    if (progress) {
			delay = 1;
			lastDelay = 0;

			Thread.sleep(delay * 1000);
			return;
		    }

		    Thread.sleep(delay * 1000);

		    int nextDelay = delay + lastDelay;
		    lastDelay = delay;
		    delay = nextDelay;

		    if (delay > 60) {
			delay = 60;
		    }
		} catch (InterruptedException e) {
		    throw new InterruptedIOException();
		}
	    }
	};

	new FileStoreFileGetMain(fs, waitHandler).run(args);
    }

    public FileStoreFileGetMain(FileSystem fs, WaitHandler waitHandler) {
	this.fs = fs;
	this.waitHandler = waitHandler;
    }

    private FileSystem fs;
    private String pendingFile;

    public void run(String... args) throws IOException {
	/*
	 * arguments: store, request, key, output
	 */
	FileBlockStore store = new FileBlockStore(fs, args[0]);
	pendingFile = args[1];
	FullKey key = KeyParse.parseFullKey(args[2]);
	RandomOutputFile out = fs.openRandomOutputFile(args[3]);

	SplitReader r = new SplitReader(key, new RetrieveBlockStorage(store));
	while (!r.isDone()) {
	    Fragment f = r.fetchAny();
	    if (f == null) {
		notReady(r);
		continue;
	    }
	    out.setPosition(f.getOffset());
	    out.write(f.getData().toByteArray());
	}
	out.close();
    }

    private void notReady(SplitReader reader) throws IOException {
	StringBuilder p = new StringBuilder();
	for (FullKey k : reader.getPending()) {
	    p.append(k.toLocatorKey() + "\n");
	}
	fs.save(pendingFile + ".tmp", ByteString.toUtf8(p.toString()));
	fs.rename(pendingFile + ".tmp", pendingFile);

	waitHandler.doWait(progress != reader.getProgress());
	progress = reader.getProgress();
    }

    int progress;

    private WaitHandler waitHandler;
}
