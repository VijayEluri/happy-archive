package org.yi.happy.archive.tag;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.yi.happy.archive.Bytes;

public class BytesBuilderTest {
    @Test
    public void testBlank() {
        Bytes have = new BytesBuilder().create();

        Bytes want = new Bytes();
        assertEquals(want, have);
    }

    @Test
    public void testByteArray() {
        byte[] data = { 0, 1, 2 };
        Bytes have = new BytesBuilder().add(data).create();

        Bytes want = new Bytes(data);
        assertEquals(want, have);
    }

    @Test
    public void testTwoByteArray() {

        byte[] data = { 0, 1, 2 };
        BytesBuilder b = new BytesBuilder().add(data);
        data[0] = 3;
        data[1] = 4;
        data[2] = 5;
        b = b.add(data);
        Bytes have = b.create();

        Bytes want = new Bytes(0, 1, 2, 3, 4, 5);
        assertEquals(want, have);
    }

    @Test
    public void testCreateBytes() {

        Bytes have = new BytesBuilder(0).add(1).create();

        Bytes want = new Bytes(0, 1);
        assertEquals(want, have);
    }

    @Test
    public void testSlice() {
        BytesBuilder b = new BytesBuilder();
        byte[] data = { 2, 3, 5 };

        b = b.add(data, 0, 1);
        b = b.add(data, 2, 1);
        Bytes have = b.create();

        Bytes want = new Bytes(2, 5);
        assertEquals(want, have);
    }
}
