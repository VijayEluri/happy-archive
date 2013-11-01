package org.yi.happy.archive;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.yi.happy.archive.block.DataBlock;
import org.yi.happy.archive.block.GenericBlock;
import org.yi.happy.archive.key.FullKey;

/**
 * An output stream that stores blocks using the map strategy.
 */
public class KeyOutputStream extends OutputStream {
    private int splitSize = 1024 * 1024;

    /**
     * the accumulating block in the stream. This will only be empty before data
     * starts being written, and after a close.
     */
    private ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    /**
     * while open this is null, when the stream is closed this is the full key
     * to the base of the map.
     */
    private FullKey fullKey;

    /**
     * where to send blocks for encoding and storage.
     */
    private final ClearBlockTarget target;

    /**
     * create with the storage object.
     * 
     * @param target
     *            where blocks are to be stored
     */
    public KeyOutputStream(ClearBlockTarget target) {
        this.target = target;
    }

    /**
     * write a single byte to the stream.
     */
    @Override
    public void write(int b) throws IOException {
        if (fullKey != null) {
            throw new ClosedException();
        }

        if (buffer.size() == splitSize) {
            flushBlock();
        }

        buffer.write(b);
    }

    /**
     * write a block of bytes to the stream.
     */
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if (fullKey != null) {
            throw new ClosedException();
        }

        int left = splitSize - buffer.size();

        while (len > left) {
            buffer.write(b, off, left);
            flushBlock();

            off += left;
            len -= left;
            left = splitSize;
        }

        buffer.write(b, off, len);
    }

    /**
     * flush the data block
     * 
     * @throws IOException
     */
    private void flushBlock() throws IOException {
        byte[] data = buffer.toByteArray();

        FullKey fullKey = storeDataBlock(data);

        putMap(fullKey, data.length);

        buffer.reset();
    }

    /**
     * store a map block in the store.
     * 
     * @param data
     *            the data
     * @param isMap
     *            true if this is a map block
     * @return the full key of the stored block
     * @throws IOException
     *             on error.
     */
    private FullKey storeMapBlock(byte[] data) throws IOException {
        /*
         * XXX there should be a map block type that I could just store.
         */
        GenericBlock b = GenericBlock.create(new Bytes(data), "type", "map",
                "size", "" + data.length);

        return target.put(b);
    }

    /**
     * Store a data block in the store.
     * 
     * @param data
     *            the bytes for the data block.
     * @return the full key of the stored block.
     * @throws IOException
     *             on error.
     */
    private FullKey storeDataBlock(byte[] data) throws IOException {
        return target.put(new DataBlock(new Bytes(data)));
    }

    /**
     * put a key into the map blocks.
     * 
     * @param fullKey
     *            the key to add to the maps.
     * @param size
     *            the size of the data the key represents.
     * @throws IOException
     *             on error.
     */
    private void putMap(FullKey fullKey, long size) throws IOException {
        if (maps == null) {
            maps = new Layer();
        }

        maps.put(fullKey, size);
    }

    /**
     * get the full key for the data that was written.
     * 
     * @return the full key of the written data.
     * @throws IllegalStateException
     *             if the stream is not closed.
     */
    public FullKey getFullKey() {
        if (fullKey == null) {
            throw new IllegalStateException("stream not closed");
        }
        return fullKey;
    }

    /**
     * change the target size of the data area of the blocks. This can only be
     * done before data is written to this stream.
     * 
     * @param size
     *            the size to split at
     */
    public void setSplitSize(int size) {
        if (fullKey != null) {
            throw new IllegalStateException();
        }

        if (buffer.size() != 0) {
            throw new IllegalStateException();
        }

        splitSize = size;
    }

    /**
     * close the stream, flush the data, and get the final key.
     */
    @Override
    public void close() throws IOException {
        if (fullKey != null) {
            return;
        }

        if (maps == null) {
            fullKey = storeDataBlock(buffer.toByteArray());
            buffer = null;
            return;
        }

        /*
         * there will always be data in the buffer here
         */

        flushBlock();

        while (maps.parent != null) {
            maps.flush();
            maps = maps.parent;
        }

        fullKey = storeMapBlock(maps.map.toByteArray());
        maps = null;
    }

    /**
     * the map block tree that is being built, it is a single link list from the
     * leaf up to the root for the currently open path.
     */
    private Layer maps = null;

    /**
     * a layer in the output map, this is a list of keys and the offset from the
     * start of the layer of the map that they apply.
     * 
     * In the final output of a large data stream there will be a tree of
     * layers, the tree of layers is built from the bottom up, as the top layer
     * gets full a new parent layer is added.
     */
    private class Layer {
        /**
         * the total size of all the keys in this layer.
         */
        public long totalSize = 0;

        /**
         * the data for this layer so far
         */
        public final ByteArrayOutputStream map = new ByteArrayOutputStream();

        /**
         * the only key in the layer (null if there are multiple)
         */
        public FullKey onlyKey;

        public Layer parent;

        /**
         * put a key into the map
         * 
         * @param index
         *            the layer of the map to put it in
         * @param fullKey
         *            the key to add
         * @param size
         *            how much data is represented by this key
         * @throws IOException
         */
        public void put(FullKey fullKey, long size) throws IOException {
            byte[] add = ByteString.toUtf8(fullKey + "\t" + totalSize + "\n");

            if (map.size() + add.length > splitSize) {
                flush();
            }

            if (totalSize == 0) {
                onlyKey = fullKey;
            } else {
                onlyKey = null;
            }

            map.write(add);
            totalSize += size;
        }

        /**
         * flush a map layer.
         * 
         * @param index
         *            the layer to flush
         * @throws IOException
         */
        public void flush() throws IOException {
            if (map.size() == 0) {
                throw new IllegalStateException("flusing empty map block");
            }

            if (onlyKey != null) {
                /*
                 * if there is only one key in the map push it up as is, this is
                 * a special case for close.
                 */
                if (parent == null) {
                    throw new IllegalStateException();
                }
                parent.put(onlyKey, totalSize);
            } else {
                FullKey fullKey = storeMapBlock(map.toByteArray());
                if (parent == null) {
                    parent = new Layer();
                }
                parent.put(fullKey, totalSize);
            }

            /*
             * prepare for the next key to be stored.
             */
            totalSize = 0;
            onlyKey = null;
            map.reset();
        }
    }
}
