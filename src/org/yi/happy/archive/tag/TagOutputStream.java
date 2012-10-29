package org.yi.happy.archive.tag;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import org.yi.happy.archive.ByteString;

/**
 * An output stream wrapper for writing out {@link Tag}s.
 */
public class TagOutputStream {
    private final OutputStream out;

    /**
     * Create an output stream wrapper for writing out {@link Tag}s.
     * 
     * @param out
     *            the stream to write to.
     */
    public TagOutputStream(OutputStream out) {
        this.out = out;
    }

    /**
     * Write a {@link Tag} to the underlying stream.
     * 
     * @param tag
     *            the {@link Tag} to write.
     * @throws IOException
     */
    public void write(Tag tag) throws IOException {
        Set<String> fields = tag.getFields();
        for (String field : fields) {
            out.write(ByteString.toUtf8(field + "=" + tag.get(field)
                    + "\n"));
        }
        out.write(ByteString.toUtf8("\n"));
    }
}
