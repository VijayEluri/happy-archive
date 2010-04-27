package org.yi.happy.archive.search;

import java.util.SortedMap;
import java.util.TreeMap;

public class LineCache {
    private String first = null;
    private SortedMap<LongRange, String> lines = new TreeMap<LongRange, String>();

    public String getFirst() {
        return first;
    }

    public void putFirst(String line) {
        this.first = line;
    }

    public String get(long position) {
        SortedMap<LongRange, String> head = lines.headMap(new LongRange(position + 1, 0));
        if (head.isEmpty()) {
            return null;
        }
        LongRange key = head.lastKey();
        if (key.getOffset() <= position && key.getEnd() > position) {
            return lines.get(key);
        }
        return null;
    }

    public void put(long start, long end, String line) {
        lines.put(new LongRange(start, end - start), line);
    }

}
