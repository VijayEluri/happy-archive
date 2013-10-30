package org.yi.happy.archive;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import org.yi.happy.annotate.GlobalFilesystem;
import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.block.parser.EncodedBlockParse;
import org.yi.happy.archive.key.LocatorKey;
import org.yi.happy.archive.key.LocatorKeyParse;

/**
 * A block store that uses the file system to store the blocks.
 */
@GlobalFilesystem
public class FileBlockStore implements BlockStore {
    private final File base;

    /**
     * create a file system block store.
     * 
     * @param fs
     *            the file system to use.
     * @param base
     *            the base path to use.
     */
    public FileBlockStore(File base) {
        this.base = base;
    }

    @Override
    public void put(EncodedBlock block) throws IOException {
        LocatorKey key = block.getKey();
        File file = makeFile(key);
        file.getParentFile().mkdirs();
        FileOutputStream out = new FileOutputStream(file);
        try {
            out.write(block.asBytes());
        } finally {
            out.close();
        }
    }

    @Override
    public EncodedBlock get(LocatorKey key) throws IOException {
        File file = makeFile(key);
        try {
            FileInputStream in = new FileInputStream(file);
            try {
                return EncodedBlockParse.parse(Streams.load(in));
            } finally {
                in.close();
            }
        } catch (FileNotFoundException e) {
            return null;
        } catch (IllegalArgumentException e) {
            // TODO remove the corrupted file
            throw new DecodeException(e);
        }
    }

    @Override
    public <T extends Throwable> void visit(BlockStoreVisitor<T> visitor)
            throws T {
        visit(visitor, base, 3);
    }

    private <T extends Throwable> void visit(BlockStoreVisitor<T> visitor,
            File path, int levels) throws T {
        if (!path.isDirectory()) {
            return;
        }

        String[] names = path.list();
        if (names == null) {
            // should not happen
            return;
        }
        Arrays.sort(names);

        if (levels > 0) {
            for (String name : names) {
                visit(visitor, new File(path, name), levels - 1);
            }
            return;
        }

        for (String name : names) {
            String[] part = name.split("-", 2);
            LocatorKey key = LocatorKeyParse.parseLocatorKey(part[1], part[0]);
            visitor.accept(key);
        }
    }

    @Override
    public boolean contains(LocatorKey key) throws IOException {
        File file = makeFile(key);
        return file.exists();
    }

    private File makeFile(LocatorKey key) {
        String name = key.getHash() + "-" + key.getType();

        File file = base;
        file = new File(file, name.substring(0, 1));
        file = new File(file, name.substring(0, 2));
        file = new File(file, name.substring(0, 3));
        file = new File(file, name);
        return file;
    }

    @Override
    public void remove(LocatorKey key) throws IOException {
        File file = makeFile(key);

        if (!file.delete() && file.exists()) {
            throw new IOException();
        }
    }

    /**
     * get the modification time for a key in the store.
     * 
     * @param key
     *            the key to query.
     * @return the modification time, or 0l if the key is not in the store.
     * @throws IOException
     *             on error.
     */
    @Override
    public long getTime(LocatorKey key) throws IOException {
        File file = makeFile(key);
        return file.lastModified();
    }

    @Override
    public Iterator<LocatorKey> iterator() {
        return new FileBlockStoreIterator(base);
    }
}
