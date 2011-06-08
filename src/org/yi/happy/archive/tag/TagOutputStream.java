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

    public TagOutputStream(OutputStream out) {
        this.out = out;
    }

    public void write(Tag tag) throws IOException {
        Set<String> fields = tag.getFields();
        for (String field : fields) {
            out.write(ByteString.toUtf8(field + "=" + tag.get(field)
                    + "\n"));
        }
        out.write(ByteString.toUtf8("\n"));
    }
}
