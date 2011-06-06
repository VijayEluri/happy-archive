package org.yi.happy.archive.tag;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class TagStreamIterator implements Iterable<Tag>, Iterator<Tag> {

    private final InputStream in;
    private final TagParser parser;
    private boolean eof;

    public TagStreamIterator(InputStream in) {
        this.in = in;
        this.parser = new TagParser();
        this.eof = false;
    }

    public Iterator<Tag> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        while (!eof && !parser.isReady()) {
            try {
                byte[] buff = new byte[8192];
                int len = in.read(buff);
                if (len == -1) {
                    eof = true;
                    parser.finish();
                } else {
                    parser.bytes(buff, 0, len);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return parser.isReady();
    }

    @Override
    public Tag next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return parser.get();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
