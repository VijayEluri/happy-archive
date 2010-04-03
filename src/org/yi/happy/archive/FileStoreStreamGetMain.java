package org.yi.happy.archive;

import java.io.IOException;
import java.io.OutputStream;

import org.yi.happy.annotate.EntryPoint;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.file_system.RealFileSystem;
import org.yi.happy.archive.key.KeyParse;

/**
 * Fetch a stream, the blocks may not all available in the file store, so the
 * ones that are needed are put in a list, and the process continues to be
 * retried until all the needed blocks become available.
 */
public class FileStoreStreamGetMain {

    private final FileSystem fs;
    private final OutputStream out;
    private final WaitHandler waitHandler;

    /**
     * create.
     * 
     * @param fs
     *            the file system.
     * @param out
     *            where to send the stream.
     * @param waitHandler
     *            what to do when no blocks are ready.
     */
    public FileStoreStreamGetMain(FileSystem fs, OutputStream out,
	    WaitHandler waitHandler) {
	this.fs = fs;
	this.out = out;
	this.waitHandler = waitHandler;
    }

    /**
     * @param args
     *            file store base, request list, key to fetch
     * @throws IOException
     */
    @EntryPoint
    public static void main(String[] args) throws IOException {
	WaitHandler waitHandler = new WaitHandlerProgressiveDelay();

	FileSystem fs = new RealFileSystem();

	OutputStream out = System.out;

	new FileStoreStreamGetMain(fs, out, waitHandler).run(args);
    }

    /**
     * restore a stream.
     * 
     * @param args
     *            the block store, where to write the pending block list, the
     *            full key to fetch.
     * @throws IOException
     */
    public void run(String... args) throws IOException {
	FileBlockStore store = new FileBlockStore(fs, args[0]);
	pendingFile = args[1];
	
	KeyInputStream in = new KeyInputStream(KeyParse
		.parseFullKey(args[2]), new RetrieveBlockStorage(store),
		notReadyHandler);

	Streams.copy(in, out);
    }

    private String pendingFile;

    private NotReadyHandler notReadyHandler = new NotReadyHandler() {
	private int progress;

	@Override
	public void notReady(SplitReader reader) throws IOException {
	    PendingList.savePendingListFile(reader.getPending(), fs,
		    pendingFile);
	    waitHandler.doWait(progress != reader.getProgress());
	    progress = reader.getProgress();
	}
    };
}
