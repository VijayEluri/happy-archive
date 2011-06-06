package org.yi.happy.archive.tag;

import org.yi.happy.archive.ByteString;

/**
 * Capture tag objects out of a marked binary stream. Expects ( tag ( field (
 * key, value ) ) ). Emits {@link Tag} objects.
 */
public class TagCapture extends DefaultBinaryHandler {
    private final TagStreamVisitor handler;

    /**
     * set up the tag capture.
     * 
     * @param handler
     *            where to send the captured tags.
     */
    public TagCapture(TagStreamVisitor handler) {
        this.handler = handler;
    }

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
            handler.accept(tag.create());
            tag = null;
            return;
        }

        throw new IllegalStateException((tag == null ? "" : "[tag]")
                + (data == null ? "" : "[data]") + name);
    }
}
