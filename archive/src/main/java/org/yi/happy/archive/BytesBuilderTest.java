package org.yi.happy.archive;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link BytesBuilder}.
 */
public class BytesBuilderTest {
    /**
     * Make sure the blank case works.
     */
    @Test
    public void testStartBlank() {
        Bytes have = new BytesBuilder().create();

        Bytes want = new Bytes();
        assertEquals(want, have);
    }

    /**
     * Build a Bytes value from an array that gets modified.
     */
    @Test
    public void testModifyAndAddAgain() {

        byte[] data = { 0, 1, 2 };
        BytesBuilder b = new BytesBuilder().add(data);
        data[0] = 3;
        data[1] = 4;
        data[2] = 5;
        b.add(data);
        Bytes have = b.create();

        Bytes want = new Bytes(0, 1, 2, 3, 4, 5);
        assertEquals(want, have);
    }

    /**
     * Build a Bytes value from bytes.
     */
    @Test
    public void testCreateBytes() {

        Bytes have = new BytesBuilder(0).add(1).create();

        Bytes want = new Bytes(0, 1);
        assertEquals(want, have);
    }

    /**
     * Build up a Bytes value from slices of an array.
     */
    @Test
    public void testSlice() {
        BytesBuilder b = new BytesBuilder();

        byte[] data = { 2, 3, 5 };
        b.add(data, 0, 1);
        b.add(data, 2, 1);

        Bytes have = b.create();

        Bytes want = new Bytes(2, 5);
        assertEquals(want, have);
    }

    /**
     * build up a Bytes value using text and binary.
     */
    @Test
    public void testText() {
        BytesBuilder b = new BytesBuilder();
        b.add("test").add(0x0d, 0x0a);
        b.add("more").add(0);

        Bytes want = new Bytes('t', 'e', 's', 't', 0x0d, 0x0a, 'm', 'o', 'r',
                'e', 0);
        assertEquals(want, b.create());
    }
}
