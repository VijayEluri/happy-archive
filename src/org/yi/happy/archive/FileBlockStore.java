package org.yi.happy.archive;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.yi.happy.annotate.DuplicatedLogic;
import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.block.parser.EncodedBlockParse;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.key.HashValue;
import org.yi.happy.archive.key.KeyParse;
import org.yi.happy.archive.key.LocatorKey;

/**
 * A block store that uses a file system to store the blocks.
 */
public class FileBlockStore implements BlockStore {

    private final FileSystem fs;
    private final String base;

    /**
     * create a file system block store.
     * 
     * @param fs
     *            the file system to use.
     * @param base
     *            the base path to use.
     */
    public FileBlockStore(FileSystem fs, String base) {
        this.fs = fs;
        this.base = base;
    }

    @DuplicatedLogic("making the file name")
    public void put(EncodedBlock b) throws IOException {
        LocatorKey key = b.getKey();
        String name = key.getHash() + "-" + key.getType();

        fs.mkdir(base);
        String fileName = fs.join(base, name.substring(0, 1));
        fs.mkdir(fileName);
        fileName = fs.join(fileName, name.substring(0, 2));
        fs.mkdir(fileName);
        fileName = fs.join(fileName, name.substring(0, 3));
        fs.mkdir(fileName);
        fileName = fs.join(fileName, name);

        fs.save(fileName, b.asBytes());
    }

    @Override
    @DuplicatedLogic("making the file name")
    public EncodedBlock get(LocatorKey key) throws IOException {
        String name = key.getHash() + "-" + key.getType();

        String fileName = fs.join(base, name.substring(0, 1));
        fileName = fs.join(fileName, name.substring(0, 2));
        fileName = fs.join(fileName, name.substring(0, 3));
        fileName = fs.join(fileName, name);

        try {
            return EncodedBlockParse.parse(fs.load(fileName));
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
            String path, int levels) throws T {
        if (levels == 0) {
            List<String> names = fs.list(path);
            Collections.sort(names);
            for (String name : names) {
                String[] part = name.split("-", 2);
                visitor.accept(KeyParse.parseLocatorKey(part[1], new HashValue(
                        part[0])));
            }
        }
        if (fs.isDir(path)) {
            List<String> names = fs.list(path);
            Collections.sort(names);
            for (String name : names) {
                name = fs.join(path, name);
                if (!fs.isDir(name)) {
                    continue;
                }
                visit(visitor, name, levels - 1);
            }
        }
    }

    @Override
    @DuplicatedLogic("making the file name")
    public boolean contains(LocatorKey key) throws IOException {
        String name = key.getHash() + "-" + key.getType();

        String fileName = fs.join(base, name.substring(0, 1));
        fileName = fs.join(fileName, name.substring(0, 2));
        fileName = fs.join(fileName, name.substring(0, 3));
        fileName = fs.join(fileName, name);

        return fs.exists(fileName);
    }

    @Override
    public void remove(LocatorKey key) throws IOException {
        String name = key.getHash() + "-" + key.getType();

        String fileName = fs.join(base, name.substring(0, 1));
        fileName = fs.join(fileName, name.substring(0, 2));
        fileName = fs.join(fileName, name.substring(0, 3));
        fileName = fs.join(fileName, name);

        fs.delete(fileName);
    }
}
