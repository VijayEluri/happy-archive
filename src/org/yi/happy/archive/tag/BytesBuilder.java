package org.yi.happy.archive.tag;

import java.io.ByteArrayInputStream;

import org.yi.happy.archive.ByteString;
import org.yi.happy.archive.Bytes;

/**
 * A builder for making Bytes objects and byte arrays.
 */
public class BytesBuilder {

    private class AddAction extends BytesBuilder {
        public AddAction(Bytes bytes) {
            super(bytes);
        }

        @Override
        public Bytes create() {
            byte[] out = createByteArray();
            return new Bytes(out);
        }

        @Override
        public byte[] createByteArray() {
            int size = getSize();
            byte[] out = new byte[size];
            getBytes(out);
            return out;
        }

        @Override
        public int getBytes(byte[] out) {
            int end = BytesBuilder.this.getBytes(out);
            bytes.getBytes(0, out, end, bytes.getSize());
            return end + bytes.getSize();
        }

        @Override
        public int getSize() {
            return BytesBuilder.this.getSize() + bytes.getSize();
        }
    }

    protected final Bytes bytes;

    /**
     * Start blank.
     */
    public BytesBuilder() {
        this(new Bytes());
    }

    /**
     * Start with a value.
     * 
     * @param bytes
     *            the value to start with.
     */
    public BytesBuilder(Bytes bytes) {
        this.bytes = bytes;
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
        return bytes.toByteArray();
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
        return bytes;
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
        bytes.getBytes(0, out, 0, bytes.getSize());
        return bytes.getSize();
    }

    /**
     * Get the size of the builder.
     * 
     * @return the size of the builder.
     */
    public int getSize() {
        return bytes.getSize();
    }

    /**
     * Make a builder that is the concatenation of the current value and the
     * given bytes.
     * 
     * @param data
     *            the data to add to the builder.
     * @return the new builder with the data added.
     */
    public BytesBuilder add(byte... data) {
        return new AddAction(new Bytes(data));
    }

    /**
     * Make a builder that is the concatenation of the current value and the
     * given bytes.
     * 
     * @param data
     *            the bytes given in the lower 8 bits of each element in the
     *            array.
     * @return the new builder with the data added.
     */
    public BytesBuilder add(int... data) {
        return new AddAction(new Bytes(data));
    }

    /**
     * Make a builder that is the concatenation of the current value and the
     * given bytes.
     * 
     * @param data
     *            the buffer the bytes are contained in.
     * @param offset
     *            the offset in the buffer to find the bytes.
     * @param size
     *            the length in the buffer of the bytes.
     * @return the new builder with the data added.
     */
    public BytesBuilder add(byte[] data, int offset, int size) {
        return new AddAction(new Bytes(data, offset, size));
    }

    /**
     * Make a builder that is the concatenation of the current value and the
     * given bytes.
     * 
     * @param data
     *            the string to take the low 8 bits from each character to make
     *            the data.
     * @return the new builder with the data added.
     */
    public BytesBuilder add(String data) {
        return add(ByteString.toBytes(data));
    }
}
