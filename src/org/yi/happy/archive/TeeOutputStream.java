package org.yi.happy.archive;

import java.io.IOException;
import java.io.OutputStream;

/**
 * An output stream that writes to two output streams.
 */
public class TeeOutputStream extends OutputStream {
    private final OutputStream out1;
    private final OutputStream out2;

    /**
     * create an output stream that writes to two output streams.
     * 
     * @param out1
     *            the first stream to write to.
     * @param out2
     *            the second stream to write to.
     */
    public TeeOutputStream(OutputStream out1, OutputStream out2) {
        try {
            this.out1 = out1;
        } finally {
            this.out2 = out2;
        }
    }

    @Override
    public void write(int b) throws IOException {
        try {
            out1.write(b);
        } finally {
            out2.write(b);
        }
    }

    @Override
    public void write(byte[] b) throws IOException {
        try {
            out1.write(b);
        } finally {
            out2.write(b);
        }
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        try {
            out1.write(b, off, len);
        } finally {
            out2.write(b, off, len);
        }
    }

    @Override
    public void flush() throws IOException {
        try {
            out1.flush();
        } finally {
            out2.flush();
        }
    }

    @Override
    public void close() throws IOException {
        try {
            out1.close();
        } finally {
            out2.close();
        }
    }
}
