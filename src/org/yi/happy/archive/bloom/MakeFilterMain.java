package org.yi.happy.archive.bloom;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.BitSet;

import org.yi.happy.archive.ByteString;
import org.yi.happy.archive.LineCursor;
import org.yi.happy.archive.crypto.DigestFactory;
import org.yi.happy.archive.crypto.DigestProvider;
import org.yi.happy.archive.crypto.Digests;

public class MakeFilterMain {
    public static final int SIZE = 8 * 64 * 1024; /* 2 ^ 19 */
    // public static final int SIZE = 8 * 2048 * 1024; /* 2 ^ 24 */

    public static void main(String[] args) throws IOException {
        /*
         * make a bloom filter from an index
         */

        BitSet s = new BitSet(SIZE);

        System.out.println(s.size());

        DigestProvider d = DigestFactory.getProvider("sha-256");

        // FileInputStream in = new FileInputStream(
        // "/Users/happy/archive.d/index/offsite/2007-03-02-0931");
        FileInputStream in = new FileInputStream(
                "/Users/happy/archive.d/index/onsite/2008-12-11-2103");

        try {
            LineCursor l = new LineCursor(in);

            while (l.next()) {
                String k0 = l.get().split("\t")[2];
                byte[] hash = Digests.expandKey(d, ByteString.toUtf8(k0), 48);

                int h = 0;
                h |= hash[0] & 0xff;
                h <<= 8;
                h |= hash[1] & 0xff;
                h <<= 3;
                h |= hash[2] & 0x8;

                int h2 = 0;
                h2 |= hash[3] & 0xff;
                h2 <<= 8;
                h2 |= hash[4] & 0xff;
                h2 <<= 3;
                h2 |= hash[5] & 0x8;

                s.set(h);
                s.set(h2);
            }
        } finally {
            in.close();
        }

        in = new FileInputStream("/Users/happy/tmp/store.lst");
        try {
            LineCursor l = new LineCursor(in);

            while (l.next()) {
                byte[] hash = Digests.expandKey(d, ByteString.toUtf8(l.get()),
                        24);

                int h = 0;
                h <<= 8;
                h |= hash[0] & 0xff;
                h <<= 8;
                h |= hash[1] & 0xff;
                h <<= 3;
                h |= hash[2] & 0x8;

                int h2 = 0;
                h2 |= hash[3] & 0xff;
                h2 <<= 8;
                h2 |= hash[4] & 0xff;
                h2 <<= 3;
                h2 |= hash[5] & 0x8;

                if (s.get(h) && s.get(h2)) {
                    System.out.println(l.get());
                }
            }
        } finally {
            in.close();
        }
    }
}
