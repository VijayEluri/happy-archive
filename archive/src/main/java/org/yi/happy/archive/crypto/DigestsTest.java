package org.yi.happy.archive.crypto;

import org.junit.Assert;
import org.junit.Test;
import org.yi.happy.archive.Base16;

/**
 * tests for {@link Digests}.
 */
public class DigestsTest {
    /**
     * check that expanding a key actually works, make a key longer than the
     * hash.
     */
    @Test
    public void testMakeKey1() {
        DigestProvider digest = DigestFactory.getProvider("sha-256");
        byte[] data = "hi".getBytes();
        int size = 36;

        byte[] got = Digests.expandKey(digest, data, size);

        byte[] want = Base16.decode(("e6908025cd50ce380feecfeaedb70ba2c2f"
                + "701cc5e314b7b70ef5e1c04b0ec5838543551"));
        Assert.assertArrayEquals(want, got);
    }

    /**
     * expand an empty string to a key shorter than the digest.
     */
    @Test
    public void testMakeKey2() {
        DigestProvider digest = DigestFactory.getProvider("sha-256");
        byte[] data = new byte[0];
        int size = 16;

        byte[] got = Digests.expandKey(digest, data, size);

        byte[] want = Base16.decode("6e340b9cffb37a989ca544e6bb780a2c");
        Assert.assertArrayEquals(want, got);
    }

    /**
     * expand some data to the size of the digest.
     */
    @Test
    public void testMakeKey3() {
        DigestProvider digest = DigestFactory.getProvider("sha-256");
        byte[] data = "hi/1".getBytes();
        int size = 32;

        byte[] got = Digests.expandKey(digest, data, size);

        byte[] want = Base16.decode(("fe9682fa0c996d8e0a9b24cd5990ffb"
                + "ea3476d5d14847826e6af0a481b83cd75"));
        Assert.assertArrayEquals(want, got);
    }

    /**
     * expand some data to the size of the digest.
     */
    @Test
    public void testMakeKey4() {
        DigestProvider digest = DigestFactory.getProvider("sha-256");
        byte[] data = "hi".getBytes();
        int size = 32;

        byte[] got = Digests.expandKey(digest, data, size);

        byte[] want = Base16.decode(("e6908025cd50ce380feecfeaedb70ba2c2f"
                + "701cc5e314b7b70ef5e1c04b0ec58"));
        Assert.assertArrayEquals(want, got);
    }

}
