package org.yi.happy.archive.index;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.yi.happy.archive.Base16;
import org.yi.happy.archive.Utf8NotSupportedError;
import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.crypto.DigestFactory;
import org.yi.happy.archive.crypto.DigestProvider;
import org.yi.happy.archive.crypto.Digests;

/**
 * Writes index lines on an {@link OutputStream}.
 */
public class IndexWriter {
    private final Writer out;
    private final DigestProvider digest;

    /**
     * Prepare to write lines on an {@link OutputStream}. The
     * {@link OutputStream} is flushed after every write.
     * 
     * @param out
     *            the {@link OutputStream}.
     */
    public IndexWriter(OutputStream out) {
        try {
            this.out = new OutputStreamWriter(out, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new Utf8NotSupportedError(e);
        }

        digest = DigestFactory.getProvider("sha-256");
    }

    /**
     * Write an index line.
     * 
     * @param name
     *            the name of the file.
     * @param loader
     *            the loader to use.
     * @param key
     *            the key of the block.
     * @param hash
     *            the hash of the normalized block.
     * @param size
     *            the size of the normalized block.
     * @throws IOException
     *             on error.
     */
    public void write(String name, String loader, String key, String hash,
            String size) throws IOException {
        out.write(name + "\t" + loader + "\t" + key + "\t" + hash + "\t" + size
                + "\n");
        out.flush();
    }

    /**
     * Write an index line.
     * 
     * @param name
     *            the name of the file.
     * @param loader
     *            the loader to use.
     * @param block
     *            the loaded block.
     * @throws IOException
     */
    public void write(String name, String loader, EncodedBlock block)
            throws IOException {
        String key = block.getKey().toString();

        byte[] data = block.asBytes();

        String hash = Base16.encode(Digests.digestData(digest, data));
        String size = Integer.toString(data.length);

        write(name, loader, key, hash, size);
    }
}
