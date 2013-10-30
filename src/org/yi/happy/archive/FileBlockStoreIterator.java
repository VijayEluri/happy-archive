package org.yi.happy.archive;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

import org.yi.happy.annotate.GlobalFilesystem;
import org.yi.happy.archive.key.LocatorKey;
import org.yi.happy.archive.key.LocatorKeyParse;

/**
 * An iterator over a file block store.
 */
@GlobalFilesystem
public class FileBlockStoreIterator implements Iterator<LocatorKey> {
    /**
     * Set up an iterator over a file block store.
     * 
     * @param base
     *            the base of the store.
     */
    public FileBlockStoreIterator(File base) {
        String[] names = base.list();
        if (names == null) {
            throw new IllegalStateException();
        }
        Arrays.sort(names);
        for (String name : names) {
            File file = new File(base, name);
            if (!file.isDirectory()) {
                continue;
            }
            layer1.add(file);
        }
    }

    @Override
    public boolean hasNext() {
        while (level4.isEmpty() && hasNext3()) {
            File base = layer3.remove();
            String[] names = base.list();
            if (names == null) {
                continue;
            }
            Arrays.sort(names);
            for (String name : names) {
                String[] part = name.split("-", 2);
                LocatorKey key = LocatorKeyParse.parseLocatorKey(part[1],
                        part[0]);
                level4.add(key);
            }
        }
        return !level4.isEmpty();
    }

    private void expand(Queue<File> src, Queue<File> dst) {
        File base = src.remove();
        String[] names = base.list();
        if (names == null) {
            return;
        }
        Arrays.sort(names);
        for (String name : names) {
            File file = new File(base, name);
            if (!file.isDirectory()) {
                continue;
            }
            dst.add(file);
        }
    }

    private boolean hasNext3() {
        while (layer3.isEmpty() && hasNext2()) {
            expand(layer2, layer3);
        }
        return !layer3.isEmpty();
    }

    private boolean hasNext2() {
        while (layer2.isEmpty() && hasNext1()) {
            expand(layer1, layer2);
        }
        return !layer2.isEmpty();
    }

    private boolean hasNext1() {
        return !layer1.isEmpty();
    }

    @Override
    public LocatorKey next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return level4.remove();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    private Queue<File> layer1 = new ArrayDeque<File>();
    private Queue<File> layer2 = new ArrayDeque<File>();
    private Queue<File> layer3 = new ArrayDeque<File>();
    private Queue<LocatorKey> level4 = new ArrayDeque<LocatorKey>();

}
