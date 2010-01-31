package org.yi.happy.archive;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.yi.happy.archive.block.BlockImpl;
import org.yi.happy.archive.key.FullKey;

/**
 * An output stream that stores blocks using the map strategy.
 * 
 * @author sarah dot a dot happy at gmail dot com
 * 
 */
public class KeyOutputStream extends OutputStream {
    /**
     * a layer in the output map, this is a list of keys and the offset from the
     * start of the layer of the map that they apply.
     * 
     * @author sarah dot a dot happy at gmail dot com
     */
    private static class Layer {
        /**
         * the offset in the stream of the first byte of the next key that will
         * be added to this layer.
         */
        public long offset = 0;

        /**
         * the data for this layer so far
         */
        public final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        /**
         * the only key in the layer (null if there are multiple)
         */
        public FullKey onlyKey;
    }

    private int splitSize = 1024 * 1024;

    /**
     * the layers of maps in the output, the higher the index the lower the
     * layer, the layers fill up lowest index first.
     */
    private List<Layer> layers = new ArrayList<Layer>();

    /**
     * the accumulating block in the stream.
     */
    private ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    /**
     * while open this is null, when the stream is closed this is the full key
     * to the base of the map.
     */
    private FullKey fullKey;

    private final StoreBlock store;

    /**
     * create with the storage object.
     * 
     * @param store
     *            where blocks are to be stored
     */
    public KeyOutputStream(StoreBlock store) {
        this.store = store;
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

        FullKey fullKey = storeBlock(data, false);

        putMap(0, fullKey, data.length);

        buffer.reset();
    }

    /**
     * store a block in the store.
     * 
     * @param data
     *            the data
     * @param isMap
     *            true if this is a map block
     * @return the full key of the stored block
     * @throws IOException
     */
    private FullKey storeBlock(byte[] data, boolean isMap) throws IOException {
        BlockImpl b = new BlockImpl();
        if (isMap) {
            b.addMeta("type", "map");
        }
        b.addMeta("size", "" + data.length);
        b.setBody(data);

        return store.storeBlock(b);
    }

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
    private void putMap(int index, FullKey fullKey, long size)
            throws IOException {
        /*
         * XXX this looks like it belongs to the layer. It also appears that the
         * layer should be a single link list that defines overflows, which
         * would make these algorithms recursive and cleaner.
         */

        if (index == layers.size()) {
            layers.add(new Layer());
        }

        Layer layer = layers.get(index);

        byte[] add = ByteString.toUtf8(fullKey + "\t" + layer.offset + "\n");

        if (layer.buffer.size() + add.length > splitSize) {
            flushMap(index);
        }

        if (layer.offset == 0) {
            layer.onlyKey = fullKey;
        } else {
            layer.onlyKey = null;
        }

        layer.buffer.write(add);
        layer.offset += size;
    }

    /**
     * flush a map layer.
     * 
     * @param index
     *            the layer to flush
     * @throws IOException
     */
    private void flushMap(int index) throws IOException {
        /*
         * XXX this looks like it belongs to Layer.
         */

        Layer layer = layers.get(index);
        if (layer.buffer.size() == 0) {
            throw new IllegalStateException("flusing empty map block");
        }

        if (layer.onlyKey == null) {
            byte[] data = layer.buffer.toByteArray();
            FullKey fullKey = storeBlock(data, true);
            putMap(index + 1, fullKey, layer.offset);
        } else {
            /*
             * if there is only one key in the map push it up as is
             */
            putMap(index + 1, layer.onlyKey, layer.offset);
        }

        layer.offset = 0;
        layer.onlyKey = null;
        layer.buffer.reset();
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
     * change the target size of the data area of the blocks.
     * 
     * @param size
     *            the size to split at
     */
    public void setSplitSize(int size) {
        splitSize = size;
    }

    /**
     * close the stream, flush the data, and get the final key.
     */
    @Override
    public void close() throws IOException {
        if (layers.size() == 0) {
            fullKey = storeBlock(buffer.toByteArray(), false);
            buffer.reset();
            return;
        }

        flushBlock();

        for (int i = 0; i < layers.size() - 1; i++) {
            flushMap(i);
        }

        Layer layer = layers.get(layers.size() - 1);
        fullKey = storeBlock(layer.buffer.toByteArray(), true);
        layer.buffer.reset();
    }
}
