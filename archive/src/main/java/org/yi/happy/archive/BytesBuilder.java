package org.yi.happy.archive;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * A builder for making Bytes objects and byte arrays.
 */
public class BytesBuilder {
    private final List<Bytes> parts = new ArrayList<Bytes>();
    private int size = 0;

    /**
     * Start blank.
     */
    public BytesBuilder() {
    }

    /**
     * Start with a value.
     * 
     * @param bytes
     *            the value to start with.
     */
    public BytesBuilder(Bytes bytes) {
        parts.add(bytes);
        size += bytes.getSize();
    }

    /**
     * Start with a value being the low eight bits of each entry in the data
     * array.
     * 
     * @param data
     *            the value to start with.
     */
    public BytesBuilder(int... data) {
        this(new Bytes(data));
    }

    /**
     * Start with a value being the low eight bits of each character in the
     * string.
     * 
     * @param data
     *            the value to start with.
     */
    public BytesBuilder(String data) {
        this(ByteString.toBytes(data));
    }

    /**
     * Start with a value.
     * 
     * @param data
     *            the value to start with.
     */
    public BytesBuilder(byte... data) {
        this(new Bytes(data));
    }

    /**
     * Get the byte array for this builder.
     * 
     * @return the byte array.
     */
    public byte[] createByteArray() {
        byte[] out = new byte[getSize()];
        getBytes(out);
        return out;
    }

    /**
     * Get an input stream for this builder.
     * 
     * @return an input stream that provides the bytes in this builder.
     */
    public ByteArrayInputStream createInputStream() {
        return new ByteArrayInputStream(createByteArray());
    }

    /**
     * Create a {@link Bytes} object for this builder.
     * 
     * @return the {@link Bytes} object.
     */
    public Bytes create() {
        if (parts.size() == 0) {
            return new Bytes();
        }
        if (parts.size() == 1) {
            return parts.get(0);
        }
        return new Bytes(createByteArray());
    }

    /**
     * copy the byte array for this builder into the given array. Overflow is
     * not checked.
     * 
     * @param out
     *            the byte array to copy this builder into.
     * @return the size consumed in the byte array.
     */
    public int getBytes(byte[] out) {
        int offset = 0;
        for (Bytes part : parts) {
            int size = part.getSize();

            part.getBytes(0, out, offset, size);

            offset += size;
        }
        return offset;
    }

    /**
     * Get the size of the builder.
     * 
     * @return the size of the builder.
     */
    public int getSize() {
        return size;
    }

    /**
     * Append data to this builder.
     * 
     * @param data
     *            the data to add to the builder.
     * @return this builder.
     */
    public BytesBuilder add(byte[] data) {
        return add(new Bytes(data));
    }

    /**
     * Append data to this builder.
     * 
     * @param data
     *            the bytes given in the lower 8 bits of each element in the
     *            array.
     * @return this builder.
     */
    public BytesBuilder add(int... data) {
        return add(new Bytes(data));
    }

    /**
     * Append data to this builder.
     * 
     * @param data
     *            the buffer the bytes are contained in.
     * @param offset
     *            the offset in the buffer to find the bytes.
     * @param size
     *            the length in the buffer of the bytes.
     * @return this builder.
     */
    public BytesBuilder add(byte[] data, int offset, int size) {
        return add(new Bytes(data, offset, size));
    }

    /**
     * Append data to this builder.
     * 
     * @param data
     *            the string to take the low 8 bits from each character to make
     *            the data.
     * @return this builder.
     */
    public BytesBuilder add(String data) {
        return add(ByteString.toBytes(data));
    }

    /**
     * Append data to this builder.
     * 
     * @param data
     *            the data to append.
     * @return this builder.
     */
    public BytesBuilder add(Bytes data) {
        parts.add(data);
        size += data.getSize();

        return this;
    }
}
