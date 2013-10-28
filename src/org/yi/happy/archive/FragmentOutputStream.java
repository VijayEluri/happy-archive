package org.yi.happy.archive;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Write fragments to an {@link OutputStream}. This deals with overlap and gaps.
 */
public class FragmentOutputStream {
    private OutputStream out;
    private long written;

    /**
     * set up to write to an {@link OutputStream}.
     * 
     * @param out
     *            the {@link OutputStream} to write to.
     */
    public FragmentOutputStream(OutputStream out) {
        this.out = out;
        this.written = 0;
    }

    /**
     * Write out a fragment. If the position is ahead of the data written a gap
     * of null bytes is made. If the position is behind of the data written the
     * part that is in the already written part of the stream is skipped.
     * 
     * @param position
     *            where in the stream the fragment is located.
     * @param bytes
     *            the array to find the data in.
     * @param offset
     *            the offset into bytes to find that data.
     * @param length
     *            the length of the data.
     * @throws IOException
     *             on errors.
     */
    private void write(long position, byte[] bytes, int offset, int length)
            throws IOException {
        /*
         * there is a gap between the already written part of the stream and the
         * fragment.
         */
        if (written < position) {
            long gap = position - written;
            byte[] pad;
            if (gap > 8192) {
                pad = new byte[8192];
            } else {
                pad = new byte[(int) gap];
            }

            while (gap > pad.length) {
                out.write(pad);
                written += pad.length;
                gap -= pad.length;
            }

            out.write(pad, 0, (int) gap);
            written += gap;
        }

        long overlap = written - position;

        /*
         * the fragment is entirely in the already written part of the stream.
         */
        if (overlap > length) {
            return;
        }

        /*
         * the fragment is partially in the already written part of the stream.
         */
        if (overlap > 0) {
            offset += overlap;
            length -= overlap;
            position += overlap;
        }

        /*
         * write out the visible fragment.
         */
        out.write(bytes, offset, length);
        this.written += length;
    }

    /**
     * Write out a fragment. If the position is ahead of the data written a gap
     * of null bytes is made. If the position is behind of the data written the
     * part that is in the already written part of the stream is skipped.
     * 
     * @param position
     *            where in the stream the fragment is located.
     * @param bytes
     *            the content of the fragment.
     * @throws IOException
     *             on errors.
     */
    public void write(long position, Bytes bytes) throws IOException {
        write(position, bytes.toByteArray(), 0, bytes.getSize());
    }

    /**
     * Write out a fragment. If the position is ahead of the data written a gap
     * of null bytes is made. If the position is behind of the data written the
     * part that is in the already written part of the stream is skipped.
     * 
     * @param fragment
     *            the fragment.
     * @throws IOException
     *             on errors.
     */
    public void write(Fragment fragment) throws IOException {
        write(fragment.getOffset(), fragment.getData());
    }

}
