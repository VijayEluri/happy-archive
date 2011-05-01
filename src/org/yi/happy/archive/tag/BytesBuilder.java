package org.yi.happy.archive.tag;

import org.yi.happy.archive.Bytes;

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

    public BytesBuilder() {
        this(new Bytes());
    }

    public BytesBuilder(Bytes bytes) {
        this.bytes = bytes;
    }

    public BytesBuilder(int... data) {
        this(new Bytes(data));
    }

    public byte[] createByteArray() {
        return bytes.toByteArray();
    }

    public Bytes create() {
        return bytes;
    }

    public int getBytes(byte[] out) {
        bytes.getBytes(0, out, 0, bytes.getSize());
        return bytes.getSize();
    }

    public int getSize() {
        return bytes.getSize();
    }

    public BytesBuilder add(byte... data) {
        return new AddAction(new Bytes(data));
    }

    public BytesBuilder add(int... data) {
        return new AddAction(new Bytes(data));
    }

    public BytesBuilder add(byte[] data, int offset, int size) {
        return new AddAction(new Bytes(data, offset, size));
    }

    // TODO make add methods for each constructor of Bytes

    // TODO make constructors for each constructor of Bytes
}
