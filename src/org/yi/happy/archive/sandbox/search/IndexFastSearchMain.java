package org.yi.happy.archive.sandbox.search;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.yi.happy.archive.ByteParse;
import org.yi.happy.archive.ByteString;
import org.yi.happy.archive.LineCursor;
import org.yi.happy.archive.block.parser.Range;
import org.yi.happy.metric.SimpleTimer;

/**
 * Experimental binary tree index searching.
 */
public class IndexFastSearchMain {
    /**
     * The chunk size to read.
     */
    private static final int PAGE_SIZE = 4096;

    /**
     * Launch.
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        new IndexFastSearchMain().run();
    }

    private void run() throws IOException {

        /*
         * we are going to search a large sorted text files for lines that have
         * a first field that matches a large list of text lines.
         */

        final String LARGE_INDEX = "/Users/happy/archive.d/sort/offsite/2007-03-02-0931";
        final String SEARCH_LIST = "/Users/happy/tmp/store.lst";

        /*
         * start iterating the search list.
         */
        FileInputStream search0 = new FileInputStream(SEARCH_LIST);
        try {
            RandomAccessFile index = new RandomAccessFile(LARGE_INDEX, "r");
            try {
                SimpleTimer time = new SimpleTimer();
                try {
                    LineCursor search = new LineCursor(search0);
                    run(index, search);
                } finally {
                    System.out.println(time);
                }
            } finally {
                index.close();
            }
        } finally {
            search0.close();
        }
    }

    private void run(RandomAccessFile index, LineCursor search)
            throws IOException {
        byte[] data = read(index, PAGE_SIZE);

        Range rest = new Range(0, data.length);
        List<String> lines = new ArrayList<String>();
        while (true) {
            Range lineEnd = ByteParse.findNewLine(data, rest);
            if (lineEnd.getLength() == 0) {
                break;
            }
            Range line = rest.before(lineEnd);
            rest = rest.after(lineEnd);

            lines.add(ByteString.fromUtf8(data, line.getOffset(), line
                    .getLength()));
        }

        // Fragment headEnd = new Fragment(rest.getOffset(), new Bytes(data,
        // rest
        // .getOffset(), rest.getLength()));

        /*
         * 
         */
    }

    private byte[] read(RandomAccessFile file, int size) throws IOException {
        byte[] out = new byte[size];
        int len = file.read(out);
        if (len == -1) {
            len = 0;
        }
        if (len != out.length) {
            out = Arrays.copyOf(out, len);
        }
        return out;
    }
}
