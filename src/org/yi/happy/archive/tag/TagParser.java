package org.yi.happy.archive.tag;

import java.util.ArrayDeque;
import java.util.Queue;

import org.yi.happy.archive.ByteString;

public class TagParser {
    private Queue<Tag> out = new ArrayDeque<Tag>();

    private BinaryHandler capture = new DefaultBinaryHandler() {
        private BytesBuilder data = null;
        private String key;
        private String value;
        private TagBuilder tag;

        @Override
        public void startRegion(String name) {
            if (name.equals("tag")) {
                tag = new TagBuilder();
            }

            if (tag != null && (name.equals("key") || name.equals("value"))) {
                data = new BytesBuilder();
            }
        }

        @Override
        public void bytes(byte[] buff, int offset, int length) {
            if (data == null) {
                return;
            }
            data = data.add(buff, offset, length);
        }

        @Override
        public void endRegion(String name) {
            if (data != null && name.equals("key")) {
                key = ByteString.fromUtf8(data.createByteArray());
                data = null;
                return;
            }

            if (data != null && name.equals("value")) {
                value = ByteString.fromUtf8(data.createByteArray());
                data = null;
                return;
            }

            if (tag != null && name.equals("field")) {
                tag = tag.add(key, value);
                key = null;
                value = null;
                return;
            }

            if (tag != null && name.equals("tag")) {
                out.add(tag.create());
                tag = null;
                return;
            }

            throw new IllegalStateException((tag == null ? "" : "[tag]")
                    + (data == null ? "" : "[data]") + name);
        }
    };

    BinaryHandler parse = new LineHandler(new TagPartHandler(capture));

    public void bytes(byte[] buff, int offset, int length) {
        parse.bytes(buff, offset, length);
    }

    public void finish() {
        parse.endStream();
    }

    public boolean isReady() {
        return out.isEmpty() == false;
    }

    public Tag get() {
        return out.remove();
    }

}
