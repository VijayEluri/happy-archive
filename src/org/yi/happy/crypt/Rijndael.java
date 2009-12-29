package org.yi.happy.crypt;

/**
 * an independent implementation from information at
 * 
 * http://csrc.nist.gov/CryptoToolkit/aes/rijndael/Rijndael-ammended.pdf
 */
public class Rijndael {

    /**
     * block size in bytes
     */
    private final int bs;

    /**
     * block size in words
     */
    private final int nb;

    /**
     * key size in bytes
     */
    private final int ks;

    /**
     * key size in words
     */
    private final int nk;

    /**
     * number of rounds
     */
    private final int nr;

    /**
     * the key schedule in 32bit words
     */
    private int[] w4;

    /**
     * initialize
     * 
     * @param bs
     *                the block size (bytes)
     * @param ks
     *                the key size (bytes)
     */
    public Rijndael(int bs, int ks) {
        if (bs != 16 && bs != 24 && bs != 32) {
            throw new IllegalArgumentException("bad block size: " + bs);
        }
        if (ks != 16 && ks != 24 && ks != 32) {
            throw new IllegalArgumentException("bad key size: " + ks);
        }
        this.bs = bs;
        nb = bs / 4;
        this.ks = ks;
        nk = ks / 4;
        nr = Math.max(nb, nk) + 6;
    }

    /**
     * set the key to use for encryption and decryption
     * 
     * @param key
     *                the key to use
     */
    public void setKey(byte[] key) {
        if (key.length != ks) {
            throw new IllegalArgumentException("wrong key length " + key.length
                    + " expected " + ks);
        }

        w4 = new int[nb * (nr + 1)];

        for (int i = 0; i < nk; i++) {
            w4[i] = bytesToInt(key, i * 4);
        }

        for (int i = nk; i < nb * (nr + 1); i++) {
            int p = w4[i - 1];
            if (i % nk == 0) {
                w4[i] = ((sbox[(p >> 16) & 0xff] ^ pow2[i / nk - 1]) << 24)
                        ^ ((sbox[(p >> 8) & 0xff] & 0xff) << 16)
                        ^ ((sbox[p & 0xff] & 0xff) << 8)
                        ^ (sbox[p >>> 24] & 0xff);
            } else if (i % nk == 4 && nk > 6) {
                w4[i] = (sbox[p >>> 24] << 24)
                        ^ ((sbox[(p >> 16) & 0xff] & 0xff) << 16)
                        ^ ((sbox[(p >> 8) & 0xff] & 0xff) << 8)
                        ^ (sbox[p & 0xff] & 0xff);
            } else {
                w4[i] = p;
            }
            w4[i] ^= w4[i - nk];
        }
    }

    /**
     * convert four bytes to an int, most significant byte first
     * 
     * @param b
     *                the bytes
     * @param o
     *                where to start
     * @return the int made of the four bytes
     */
    private static int bytesToInt(byte[] b, int o) {
        int i = ((b[o++] & 0xff) << 24);
        i ^= ((b[o++] & 0xff) << 16);
        i ^= ((b[o++] & 0xff) << 8);
        i ^= ((b[o++] & 0xff));
        return i;
    }

    /**
     * convert an int to four bytes, most significant byte first
     * 
     * @param i
     *                the int
     * @param b
     *                the bytes
     * @param o
     *                where to start
     */
    private static void intToBytes(int i, byte[] b, int o) {
        b[o++] = (byte) (i >> 24);
        b[o++] = (byte) (i >> 16);
        b[o++] = (byte) (i >> 8);
        b[o++] = (byte) (i);
    }

    /**
     * do an encrypt in ECB mode
     * 
     * @param data
     *                the data to encrypt
     */
    public void encryptEcb(byte[] data) {
        encrypt(data, 0, data.length, null);
    }

    /**
     * do an encrypt in CBC mode
     * 
     * @param data
     *                what to encrypt
     * @param iv
     *                the initial vector, updated for chaining
     */
    public void encryptCbc(byte[] data, byte[] iv) {
        encrypt(data, 0, data.length, iv);
    }

    /**
     * do an encrypt
     * 
     * @param data
     *                the block to encrypt in place
     * @param offset
     *                where in data to start
     * @param size
     *                how much of data to process
     * @param iv
     *                the initial vector for cbc mode, null for ecb mode
     * @throws IllegalArgumentException
     *                 if size is not a multiple of the block size;
     * @throws IllegalArgumentException
     *                 if iv is not null and not the same size as the block size
     */
    public void encrypt(byte[] data, int offset, int size, byte[] iv) {
        if (size < 0 || (size % bs) != 0) {
            throw new IllegalArgumentException(
                    "size is negative or not a multiple of the block size");
        }

        if (iv != null && iv.length != bs) {
            throw new IllegalArgumentException("bad initial vector size "
                    + iv.length + " expected " + bs);
        }

        if (nb == 4) {
            encrypt4(data, offset, size, iv);
            return;
        }

        if (nb == 6) {
            encrypt6(data, offset, size, iv);
            return;
        }

        if (nb == 8) {
            encrypt8(data, offset, size, iv);
            return;
        }

        throw new Error("nb == " + nb);
    }

    /**
     * encrypt with 16 byte (4 word) block
     * 
     * @param state
     *                the block
     * @param offset
     *                where in data to start
     * @param size
     *                how much of data to process
     */
    private void encrypt4(byte[] state, int offset, int size, byte[] iv) {
        int o0 = 0;
        int o1 = 0;
        int o2 = 0;
        int o3 = 0;

        if (iv != null) {
            o0 = bytesToInt(iv, 0);
            o1 = bytesToInt(iv, 4);
            o2 = bytesToInt(iv, 8);
            o3 = bytesToInt(iv, 12);
        }
        while (size > 0) {
            int s0 = bytesToInt(state, offset + 0);
            int s1 = bytesToInt(state, offset + 4);
            int s2 = bytesToInt(state, offset + 8);
            int s3 = bytesToInt(state, offset + 12);

            if (iv != null) {
                s0 ^= o0;
                s1 ^= o1;
                s2 ^= o2;
                s3 ^= o3;
            }

            int i0, i1, i2, i3;

            int r = 0;
            s0 ^= w4[r++];
            s1 ^= w4[r++];
            s2 ^= w4[r++];
            s3 ^= w4[r++];

            for (int i = nr; --i != 0;) {
                // shift 0,1,2,3
                i0 = (s0 >>> 24);
                i1 = (s1 >> 16) & 0xff;
                i2 = (s2 >> 8) & 0xff;
                i3 = (s3) & 0xff;

                o0 = t0[i0] ^ t1[i1] ^ t2[i2] ^ t3[i3] ^ w4[r++];

                i0 = (s1 >>> 24);
                i1 = (s2 >> 16) & 0xff;
                i2 = (s3 >> 8) & 0xff;
                i3 = (s0) & 0xff;

                o1 = t0[i0] ^ t1[i1] ^ t2[i2] ^ t3[i3] ^ w4[r++];

                i0 = (s2 >>> 24);
                i1 = (s3 >> 16) & 0xff;
                i2 = (s0 >> 8) & 0xff;
                i3 = (s1) & 0xff;

                o2 = t0[i0] ^ t1[i1] ^ t2[i2] ^ t3[i3] ^ w4[r++];

                i0 = (s3 >>> 24);
                i1 = (s0 >> 16) & 0xff;
                i2 = (s1 >> 8) & 0xff;
                i3 = (s2) & 0xff;

                o3 = t0[i0] ^ t1[i1] ^ t2[i2] ^ t3[i3] ^ w4[r++];

                s0 = o0;
                s1 = o1;
                s2 = o2;
                s3 = o3;
            }

            i0 = (s0 >>> 24);
            i1 = (s1 >> 16) & 0xff;
            i2 = (s2 >> 8) & 0xff;
            i3 = (s3) & 0xff;

            o0 = (sbox[i0] << 24) ^ ((sbox[i1] & 0xff) << 16)
                    ^ ((sbox[i2] & 0xff) << 8) ^ ((sbox[i3] & 0xff)) ^ w4[r++];

            i0 = (s1 >>> 24);
            i1 = (s2 >> 16) & 0xff;
            i2 = (s3 >> 8) & 0xff;
            i3 = (s0) & 0xff;

            o1 = ((sbox[i0]) << 24) ^ ((sbox[i1] & 0xff) << 16)
                    ^ ((sbox[i2] & 0xff) << 8) ^ ((sbox[i3] & 0xff)) ^ w4[r++];

            i0 = (s2 >>> 24);
            i1 = (s3 >> 16) & 0xff;
            i2 = (s0 >> 8) & 0xff;
            i3 = (s1) & 0xff;

            o2 = (((sbox[i0]) << 24) ^ ((sbox[i1] & 0xff) << 16)
                    ^ ((sbox[i2] & 0xff) << 8) ^ ((sbox[i3] & 0xff)))
                    ^ w4[r++];

            i0 = (s3 >>> 24);
            i1 = (s0 >> 16) & 0xff;
            i2 = (s1 >> 8) & 0xff;
            i3 = (s2) & 0xff;

            o3 = (((sbox[i0]) << 24) ^ ((sbox[i1] & 0xff) << 16)
                    ^ ((sbox[i2] & 0xff) << 8) ^ ((sbox[i3] & 0xff)))
                    ^ w4[r++];

            intToBytes(o0, state, offset + 0);
            intToBytes(o1, state, offset + 4);
            intToBytes(o2, state, offset + 8);
            intToBytes(o3, state, offset + 12);

            offset += 16;
            size -= 16;
        }

        if (iv != null) {
            intToBytes(o0, iv, 0);
            intToBytes(o1, iv, 4);
            intToBytes(o2, iv, 8);
            intToBytes(o3, iv, 12);
        }

    }

    /**
     * encrypt with 24 byte (6 word) block
     * 
     * @param state
     *                the block
     * @param offset
     *                where in data to start
     * @param size
     *                how much of data to process
     */
    private void encrypt6(byte[] state, int offset, int size, byte[] iv) {
        int o0 = 0;
        int o1 = 0;
        int o2 = 0;
        int o3 = 0;
        int o4 = 0;
        int o5 = 0;

        if (iv != null) {
            o0 = bytesToInt(iv, 0);
            o1 = bytesToInt(iv, 4);
            o2 = bytesToInt(iv, 8);
            o3 = bytesToInt(iv, 12);
            o4 = bytesToInt(iv, 16);
            o5 = bytesToInt(iv, 20);
        }

        while (size > 0) {
            int s0 = bytesToInt(state, offset + 0);
            int s1 = bytesToInt(state, offset + 4);
            int s2 = bytesToInt(state, offset + 8);
            int s3 = bytesToInt(state, offset + 12);
            int s4 = bytesToInt(state, offset + 16);
            int s5 = bytesToInt(state, offset + 20);

            if (iv != null) {
                s0 ^= o0;
                s1 ^= o1;
                s2 ^= o2;
                s3 ^= o3;
                s4 ^= o4;
                s5 ^= o5;
            }

            int i0, i1, i2, i3;

            int r = 0;
            s0 ^= w4[r++];
            s1 ^= w4[r++];
            s2 ^= w4[r++];
            s3 ^= w4[r++];
            s4 ^= w4[r++];
            s5 ^= w4[r++];

            for (int i = nr; --i != 0;) {
                // shift 0,1,2,3
                i0 = (s0 >>> 24);
                i1 = (s1 >> 16) & 0xff;
                i2 = (s2 >> 8) & 0xff;
                i3 = (s3) & 0xff;

                o0 = t0[i0] ^ t1[i1] ^ t2[i2] ^ t3[i3] ^ w4[r++];

                i0 = (s1 >>> 24);
                i1 = (s2 >> 16) & 0xff;
                i2 = (s3 >> 8) & 0xff;
                i3 = (s4) & 0xff;

                o1 = t0[i0] ^ t1[i1] ^ t2[i2] ^ t3[i3] ^ w4[r++];

                i0 = (s2 >>> 24);
                i1 = (s3 >> 16) & 0xff;
                i2 = (s4 >> 8) & 0xff;
                i3 = (s5) & 0xff;

                o2 = t0[i0] ^ t1[i1] ^ t2[i2] ^ t3[i3] ^ w4[r++];

                i0 = (s3 >>> 24);
                i1 = (s4 >> 16) & 0xff;
                i2 = (s5 >> 8) & 0xff;
                i3 = (s0) & 0xff;

                o3 = t0[i0] ^ t1[i1] ^ t2[i2] ^ t3[i3] ^ w4[r++];

                i0 = (s4 >>> 24);
                i1 = (s5 >> 16) & 0xff;
                i2 = (s0 >> 8) & 0xff;
                i3 = (s1) & 0xff;

                o4 = t0[i0] ^ t1[i1] ^ t2[i2] ^ t3[i3] ^ w4[r++];

                i0 = (s5 >>> 24);
                i1 = (s0 >> 16) & 0xff;
                i2 = (s1 >> 8) & 0xff;
                i3 = (s2) & 0xff;

                o5 = t0[i0] ^ t1[i1] ^ t2[i2] ^ t3[i3] ^ w4[r++];

                s0 = o0;
                s1 = o1;
                s2 = o2;
                s3 = o3;
                s4 = o4;
                s5 = o5;
            }

            i0 = (s0 >>> 24);
            i1 = (s1 >> 16) & 0xff;
            i2 = (s2 >> 8) & 0xff;
            i3 = (s3) & 0xff;

            o0 = (sbox[i0] << 24) ^ ((sbox[i1] & 0xff) << 16)
                    ^ ((sbox[i2] & 0xff) << 8) ^ ((sbox[i3] & 0xff)) ^ w4[r++];

            i0 = (s1 >>> 24);
            i1 = (s2 >> 16) & 0xff;
            i2 = (s3 >> 8) & 0xff;
            i3 = (s4) & 0xff;

            o1 = ((sbox[i0]) << 24) ^ ((sbox[i1] & 0xff) << 16)
                    ^ ((sbox[i2] & 0xff) << 8) ^ ((sbox[i3] & 0xff)) ^ w4[r++];

            i0 = (s2 >>> 24);
            i1 = (s3 >> 16) & 0xff;
            i2 = (s4 >> 8) & 0xff;
            i3 = (s5) & 0xff;

            o2 = (((sbox[i0]) << 24) ^ ((sbox[i1] & 0xff) << 16)
                    ^ ((sbox[i2] & 0xff) << 8) ^ ((sbox[i3] & 0xff)))
                    ^ w4[r++];

            i0 = (s3 >>> 24);
            i1 = (s4 >> 16) & 0xff;
            i2 = (s5 >> 8) & 0xff;
            i3 = (s0) & 0xff;

            o3 = (((sbox[i0]) << 24) ^ ((sbox[i1] & 0xff) << 16)
                    ^ ((sbox[i2] & 0xff) << 8) ^ ((sbox[i3] & 0xff)))
                    ^ w4[r++];

            i0 = (s4 >>> 24);
            i1 = (s5 >> 16) & 0xff;
            i2 = (s0 >> 8) & 0xff;
            i3 = (s1) & 0xff;

            o4 = (((sbox[i0]) << 24) ^ ((sbox[i1] & 0xff) << 16)
                    ^ ((sbox[i2] & 0xff) << 8) ^ ((sbox[i3] & 0xff)))
                    ^ w4[r++];

            i0 = (s5 >>> 24);
            i1 = (s0 >> 16) & 0xff;
            i2 = (s1 >> 8) & 0xff;
            i3 = (s2) & 0xff;

            o5 = (((sbox[i0]) << 24) ^ ((sbox[i1] & 0xff) << 16)
                    ^ ((sbox[i2] & 0xff) << 8) ^ ((sbox[i3] & 0xff)))
                    ^ w4[r++];

            intToBytes(o0, state, offset + 0);
            intToBytes(o1, state, offset + 4);
            intToBytes(o2, state, offset + 8);
            intToBytes(o3, state, offset + 12);
            intToBytes(o4, state, offset + 16);
            intToBytes(o5, state, offset + 20);

            offset += 24;
            size -= 24;
        }

        if (iv != null) {
            intToBytes(o0, iv, 0);
            intToBytes(o1, iv, 4);
            intToBytes(o2, iv, 8);
            intToBytes(o3, iv, 12);
            intToBytes(o4, iv, 16);
            intToBytes(o5, iv, 20);
        }
    }

    /**
     * encrypt with 32 byte (8 word) block
     * 
     * @param state
     *                the block
     * @param offset
     *                where in data to start
     * @param size
     *                how much of data to process
     */
    private void encrypt8(byte[] state, int offset, int size, byte[] iv) {
        int o0 = 0;
        int o1 = 0;
        int o2 = 0;
        int o3 = 0;
        int o4 = 0;
        int o5 = 0;
        int o6 = 0;
        int o7 = 0;

        if (iv != null) {
            o0 = bytesToInt(iv, 0);
            o1 = bytesToInt(iv, 4);
            o2 = bytesToInt(iv, 8);
            o3 = bytesToInt(iv, 12);
            o4 = bytesToInt(iv, 16);
            o5 = bytesToInt(iv, 20);
            o6 = bytesToInt(iv, 24);
            o7 = bytesToInt(iv, 28);
        }

        while (size > 0) {
            int s0 = bytesToInt(state, offset + 0);
            int s1 = bytesToInt(state, offset + 4);
            int s2 = bytesToInt(state, offset + 8);
            int s3 = bytesToInt(state, offset + 12);
            int s4 = bytesToInt(state, offset + 16);
            int s5 = bytesToInt(state, offset + 20);
            int s6 = bytesToInt(state, offset + 24);
            int s7 = bytesToInt(state, offset + 28);

            if (iv != null) {
                s0 ^= o0;
                s1 ^= o1;
                s2 ^= o2;
                s3 ^= o3;
                s4 ^= o4;
                s5 ^= o5;
                s6 ^= o6;
                s7 ^= o7;
            }

            int i0, i1, i2, i3;

            int r = 0;
            s0 ^= w4[r++];
            s1 ^= w4[r++];
            s2 ^= w4[r++];
            s3 ^= w4[r++];
            s4 ^= w4[r++];
            s5 ^= w4[r++];
            s6 ^= w4[r++];
            s7 ^= w4[r++];

            for (int i = nr; --i != 0;) {
                // shift 0,1,3,4
                i0 = (s0 >>> 24);
                i1 = (s1 >> 16) & 0xff;
                i2 = (s3 >> 8) & 0xff;
                i3 = (s4) & 0xff;

                o0 = t0[i0] ^ t1[i1] ^ t2[i2] ^ t3[i3] ^ w4[r++];

                i0 = (s1 >>> 24);
                i1 = (s2 >> 16) & 0xff;
                i2 = (s4 >> 8) & 0xff;
                i3 = (s5) & 0xff;

                o1 = t0[i0] ^ t1[i1] ^ t2[i2] ^ t3[i3] ^ w4[r++];

                i0 = (s2 >>> 24);
                i1 = (s3 >> 16) & 0xff;
                i2 = (s5 >> 8) & 0xff;
                i3 = (s6) & 0xff;

                o2 = t0[i0] ^ t1[i1] ^ t2[i2] ^ t3[i3] ^ w4[r++];

                i0 = (s3 >>> 24);
                i1 = (s4 >> 16) & 0xff;
                i2 = (s6 >> 8) & 0xff;
                i3 = (s7) & 0xff;

                o3 = t0[i0] ^ t1[i1] ^ t2[i2] ^ t3[i3] ^ w4[r++];

                i0 = (s4 >>> 24);
                i1 = (s5 >> 16) & 0xff;
                i2 = (s7 >> 8) & 0xff;
                i3 = (s0) & 0xff;

                o4 = t0[i0] ^ t1[i1] ^ t2[i2] ^ t3[i3] ^ w4[r++];

                i0 = (s5 >>> 24);
                i1 = (s6 >> 16) & 0xff;
                i2 = (s0 >> 8) & 0xff;
                i3 = (s1) & 0xff;

                o5 = t0[i0] ^ t1[i1] ^ t2[i2] ^ t3[i3] ^ w4[r++];

                i0 = (s6 >>> 24);
                i1 = (s7 >> 16) & 0xff;
                i2 = (s1 >> 8) & 0xff;
                i3 = (s2) & 0xff;

                o6 = t0[i0] ^ t1[i1] ^ t2[i2] ^ t3[i3] ^ w4[r++];

                i0 = (s7 >>> 24);
                i1 = (s0 >> 16) & 0xff;
                i2 = (s2 >> 8) & 0xff;
                i3 = (s3) & 0xff;

                o7 = t0[i0] ^ t1[i1] ^ t2[i2] ^ t3[i3] ^ w4[r++];

                s0 = o0;
                s1 = o1;
                s2 = o2;
                s3 = o3;
                s4 = o4;
                s5 = o5;
                s6 = o6;
                s7 = o7;
            }

            i0 = (s0 >>> 24);
            i1 = (s1 >> 16) & 0xff;
            i2 = (s3 >> 8) & 0xff;
            i3 = (s4) & 0xff;

            o0 = (sbox[i0] << 24) ^ ((sbox[i1] & 0xff) << 16)
                    ^ ((sbox[i2] & 0xff) << 8) ^ ((sbox[i3] & 0xff)) ^ w4[r++];

            i0 = (s1 >>> 24);
            i1 = (s2 >> 16) & 0xff;
            i2 = (s4 >> 8) & 0xff;
            i3 = (s5) & 0xff;

            o1 = ((sbox[i0]) << 24) ^ ((sbox[i1] & 0xff) << 16)
                    ^ ((sbox[i2] & 0xff) << 8) ^ ((sbox[i3] & 0xff)) ^ w4[r++];

            i0 = (s2 >>> 24);
            i1 = (s3 >> 16) & 0xff;
            i2 = (s5 >> 8) & 0xff;
            i3 = (s6) & 0xff;

            o2 = (((sbox[i0]) << 24) ^ ((sbox[i1] & 0xff) << 16)
                    ^ ((sbox[i2] & 0xff) << 8) ^ ((sbox[i3] & 0xff)))
                    ^ w4[r++];

            i0 = (s3 >>> 24);
            i1 = (s4 >> 16) & 0xff;
            i2 = (s6 >> 8) & 0xff;
            i3 = (s7) & 0xff;

            o3 = (((sbox[i0]) << 24) ^ ((sbox[i1] & 0xff) << 16)
                    ^ ((sbox[i2] & 0xff) << 8) ^ ((sbox[i3] & 0xff)))
                    ^ w4[r++];

            i0 = (s4 >>> 24);
            i1 = (s5 >> 16) & 0xff;
            i2 = (s7 >> 8) & 0xff;
            i3 = (s0) & 0xff;

            o4 = (((sbox[i0]) << 24) ^ ((sbox[i1] & 0xff) << 16)
                    ^ ((sbox[i2] & 0xff) << 8) ^ ((sbox[i3] & 0xff)))
                    ^ w4[r++];

            i0 = (s5 >>> 24);
            i1 = (s6 >> 16) & 0xff;
            i2 = (s0 >> 8) & 0xff;
            i3 = (s1) & 0xff;

            o5 = (((sbox[i0]) << 24) ^ ((sbox[i1] & 0xff) << 16)
                    ^ ((sbox[i2] & 0xff) << 8) ^ ((sbox[i3] & 0xff)))
                    ^ w4[r++];

            i0 = (s6 >>> 24);
            i1 = (s7 >> 16) & 0xff;
            i2 = (s1 >> 8) & 0xff;
            i3 = (s2) & 0xff;

            o6 = (((sbox[i0]) << 24) ^ ((sbox[i1] & 0xff) << 16)
                    ^ ((sbox[i2] & 0xff) << 8) ^ ((sbox[i3] & 0xff)))
                    ^ w4[r++];

            i0 = (s7 >>> 24);
            i1 = (s0 >> 16) & 0xff;
            i2 = (s2 >> 8) & 0xff;
            i3 = (s3) & 0xff;

            o7 = (((sbox[i0]) << 24) ^ ((sbox[i1] & 0xff) << 16)
                    ^ ((sbox[i2] & 0xff) << 8) ^ ((sbox[i3] & 0xff)))
                    ^ w4[r++];

            intToBytes(o0, state, offset + 0);
            intToBytes(o1, state, offset + 4);
            intToBytes(o2, state, offset + 8);
            intToBytes(o3, state, offset + 12);
            intToBytes(o4, state, offset + 16);
            intToBytes(o5, state, offset + 20);
            intToBytes(o6, state, offset + 24);
            intToBytes(o7, state, offset + 28);

            offset += 32;
            size -= 32;
        }

        if (iv != null) {
            intToBytes(o0, iv, 0);
            intToBytes(o1, iv, 4);
            intToBytes(o2, iv, 8);
            intToBytes(o3, iv, 12);
            intToBytes(o4, iv, 16);
            intToBytes(o5, iv, 20);
            intToBytes(o6, iv, 24);
            intToBytes(o7, iv, 28);
        }
    }

    /**
     * do a decrypt in ECB mode
     * 
     * @param data
     *                what to decrypt (in place)
     */
    public void decryptEcb(byte[] data) {
        decrypt(data, 0, data.length, null);
    }

    /**
     * do a decrypt in CBC mode
     * 
     * @param data
     *                what to encrypt
     * @param iv
     *                the initial vector, updated for chaining
     */
    public void decryptCbc(byte[] data, byte[] iv) {
        decrypt(data, 0, data.length, iv);
    }

    /**
     * invsere of encrypt (section 5.3)
     * 
     * @param data
     *                what to encrypt
     * @param offset
     *                where in data to start
     * @param size
     *                how much of data to process
     * @param iv
     *                the initial vector, null for ecb mode
     */
    public void decrypt(byte[] data, int offset, int size, byte[] iv) {
        if (size < 0 || (size % bs) != 0) {
            throw new IllegalArgumentException(
                    "size is negative or not a multiple of the block size");
        }

        if (iv != null && iv.length != bs) {
            throw new IllegalArgumentException("bad initial vector size "
                    + iv.length + " expected " + bs);
        }

        if (nb == 4) {
            decrypt4(data, offset, size, iv);
            return;
        }

        if (nb == 6) {
            decrypt6(data, offset, size, iv);
            return;
        }

        if (nb == 8) {
            decrypt8(data, offset, size, iv);
            return;
        }

        throw new Error("nb == " + nb);
    }

    /**
     * decrypt with 16 byte (4 word) block
     * 
     * @param state
     *                the block
     */
    private void decrypt4(byte[] state, int offset, int size, byte[] iv) {
        int iv00 = 0;
        int iv01 = 0;
        int iv02 = 0;
        int iv03 = 0;

        if (iv != null) {
            iv00 = bytesToInt(iv, 0);
            iv01 = bytesToInt(iv, 4);
            iv02 = bytesToInt(iv, 8);
            iv03 = bytesToInt(iv, 12);
        }

        while (size > 0) {
            int s0 = bytesToInt(state, offset + 0);
            int s1 = bytesToInt(state, offset + 4);
            int s2 = bytesToInt(state, offset + 8);
            int s3 = bytesToInt(state, offset + 12);

            int iv10 = 0;
            int iv11 = 0;
            int iv12 = 0;
            int iv13 = 0;

            if (iv != null) {
                iv10 = s0;
                iv11 = s1;
                iv12 = s2;
                iv13 = s3;
            }

            int i0, i1, i2, i3, o0, o1, o2, o3;

            int r = w4.length;

            // add round key

            s3 ^= w4[--r];
            s2 ^= w4[--r];
            s1 ^= w4[--r];
            s0 ^= w4[--r];

            // inv shift row + inv byte sub

            i0 = (s3 >>> 24);
            i1 = (s2 >> 16) & 0xff;
            i2 = (s1 >> 8) & 0xff;
            i3 = (s0) & 0xff;

            o3 = (((invSbox[i0]) << 24) ^ ((invSbox[i1] & 0xff) << 16)
                    ^ ((invSbox[i2] & 0xff) << 8) ^ ((invSbox[i3] & 0xff)));

            i0 = (s2 >>> 24);
            i1 = (s1 >> 16) & 0xff;
            i2 = (s0 >> 8) & 0xff;
            i3 = (s3) & 0xff;

            o2 = (((invSbox[i0]) << 24) ^ ((invSbox[i1] & 0xff) << 16)
                    ^ ((invSbox[i2] & 0xff) << 8) ^ ((invSbox[i3] & 0xff)));

            i0 = (s1 >>> 24);
            i1 = (s0 >> 16) & 0xff;
            i2 = (s3 >> 8) & 0xff;
            i3 = (s2) & 0xff;

            o1 = ((invSbox[i0]) << 24) ^ ((invSbox[i1] & 0xff) << 16)
                    ^ ((invSbox[i2] & 0xff) << 8) ^ ((invSbox[i3] & 0xff));

            i0 = (s0 >>> 24);
            i1 = (s3 >> 16) & 0xff;
            i2 = (s2 >> 8) & 0xff;
            i3 = (s1) & 0xff;

            o0 = (invSbox[i0] << 24) ^ ((invSbox[i1] & 0xff) << 16)
                    ^ ((invSbox[i2] & 0xff) << 8) ^ ((invSbox[i3] & 0xff));

            // add round key

            o3 ^= w4[--r];
            o2 ^= w4[--r];
            o1 ^= w4[--r];
            o0 ^= w4[--r];

            for (int i = nr; --i != 0;) {
                // inv mix columns

                i0 = (o3 >>> 24);
                i1 = (o3 >> 16) & 0xff;
                i2 = (o3 >> 8) & 0xff;
                i3 = (o3) & 0xff;

                s3 = invT0[i0] ^ invT1[i1] ^ invT2[i2] ^ invT3[i3];

                i0 = (o2 >>> 24);
                i1 = (o2 >> 16) & 0xff;
                i2 = (o2 >> 8) & 0xff;
                i3 = (o2) & 0xff;

                s2 = invT0[i0] ^ invT1[i1] ^ invT2[i2] ^ invT3[i3];

                i0 = (o1 >>> 24);
                i1 = (o1 >> 16) & 0xff;
                i2 = (o1 >> 8) & 0xff;
                i3 = (o1) & 0xff;

                s1 = invT0[i0] ^ invT1[i1] ^ invT2[i2] ^ invT3[i3];

                i0 = (o0 >>> 24);
                i1 = (o0 >> 16) & 0xff;
                i2 = (o0 >> 8) & 0xff;
                i3 = (o0) & 0xff;

                s0 = invT0[i0] ^ invT1[i1] ^ invT2[i2] ^ invT3[i3];

                // inv shift row + inv byte sub

                i0 = (s3 >>> 24);
                i1 = (s2 >> 16) & 0xff;
                i2 = (s1 >> 8) & 0xff;
                i3 = (s0) & 0xff;

                o3 = (((invSbox[i0]) << 24) ^ ((invSbox[i1] & 0xff) << 16)
                        ^ ((invSbox[i2] & 0xff) << 8) ^ ((invSbox[i3] & 0xff)));

                i0 = (s2 >>> 24);
                i1 = (s1 >> 16) & 0xff;
                i2 = (s0 >> 8) & 0xff;
                i3 = (s3) & 0xff;

                o2 = (((invSbox[i0]) << 24) ^ ((invSbox[i1] & 0xff) << 16)
                        ^ ((invSbox[i2] & 0xff) << 8) ^ ((invSbox[i3] & 0xff)));

                i0 = (s1 >>> 24);
                i1 = (s0 >> 16) & 0xff;
                i2 = (s3 >> 8) & 0xff;
                i3 = (s2) & 0xff;

                o1 = ((invSbox[i0]) << 24) ^ ((invSbox[i1] & 0xff) << 16)
                        ^ ((invSbox[i2] & 0xff) << 8) ^ ((invSbox[i3] & 0xff));

                i0 = (s0 >>> 24);
                i1 = (s3 >> 16) & 0xff;
                i2 = (s2 >> 8) & 0xff;
                i3 = (s1) & 0xff;

                o0 = (invSbox[i0] << 24) ^ ((invSbox[i1] & 0xff) << 16)
                        ^ ((invSbox[i2] & 0xff) << 8) ^ ((invSbox[i3] & 0xff));

                // add round key

                o3 ^= w4[--r];
                o2 ^= w4[--r];
                o1 ^= w4[--r];
                o0 ^= w4[--r];
            }

            if (iv != null) {
                o0 ^= iv00;
                o1 ^= iv01;
                o2 ^= iv02;
                o3 ^= iv03;

                iv00 = iv10;
                iv01 = iv11;
                iv02 = iv12;
                iv03 = iv13;
            }

            intToBytes(o0, state, offset + 0);
            intToBytes(o1, state, offset + 4);
            intToBytes(o2, state, offset + 8);
            intToBytes(o3, state, offset + 12);

            offset += 16;
            size -= 16;
        }

        if (iv != null) {
            intToBytes(iv00, iv, 0);
            intToBytes(iv01, iv, 4);
            intToBytes(iv02, iv, 8);
            intToBytes(iv03, iv, 12);
        }
    }

    /**
     * decrypt with 24 byte (6 word) block
     * 
     * @param state
     *                the block
     */
    private void decrypt6(byte[] state, int offset, int size, byte[] iv) {
        int iv00 = 0;
        int iv01 = 0;
        int iv02 = 0;
        int iv03 = 0;
        int iv04 = 0;
        int iv05 = 0;

        if (iv != null) {
            iv00 = bytesToInt(iv, 0);
            iv01 = bytesToInt(iv, 4);
            iv02 = bytesToInt(iv, 8);
            iv03 = bytesToInt(iv, 12);
            iv04 = bytesToInt(iv, 16);
            iv05 = bytesToInt(iv, 20);
        }

        while (size > 0) {
            int s0 = bytesToInt(state, offset + 0);
            int s1 = bytesToInt(state, offset + 4);
            int s2 = bytesToInt(state, offset + 8);
            int s3 = bytesToInt(state, offset + 12);
            int s4 = bytesToInt(state, offset + 16);
            int s5 = bytesToInt(state, offset + 20);

            int iv10 = 0;
            int iv11 = 0;
            int iv12 = 0;
            int iv13 = 0;
            int iv14 = 0;
            int iv15 = 0;

            if (iv != null) {
                iv10 = s0;
                iv11 = s1;
                iv12 = s2;
                iv13 = s3;
                iv14 = s4;
                iv15 = s5;
            }

            int i0, i1, i2, i3, o0, o1, o2, o3, o4, o5;

            int r = w4.length;

            // add round key

            s5 ^= w4[--r];
            s4 ^= w4[--r];
            s3 ^= w4[--r];
            s2 ^= w4[--r];
            s1 ^= w4[--r];
            s0 ^= w4[--r];

            // inv shift row + inv byte sub

            i0 = (s5 >>> 24);
            i1 = (s4 >> 16) & 0xff;
            i2 = (s3 >> 8) & 0xff;
            i3 = (s2) & 0xff;

            o5 = (((invSbox[i0]) << 24) ^ ((invSbox[i1] & 0xff) << 16)
                    ^ ((invSbox[i2] & 0xff) << 8) ^ ((invSbox[i3] & 0xff)));

            i0 = (s4 >>> 24);
            i1 = (s3 >> 16) & 0xff;
            i2 = (s2 >> 8) & 0xff;
            i3 = (s1) & 0xff;

            o4 = (((invSbox[i0]) << 24) ^ ((invSbox[i1] & 0xff) << 16)
                    ^ ((invSbox[i2] & 0xff) << 8) ^ ((invSbox[i3] & 0xff)));

            i0 = (s3 >>> 24);
            i1 = (s2 >> 16) & 0xff;
            i2 = (s1 >> 8) & 0xff;
            i3 = (s0) & 0xff;

            o3 = (((invSbox[i0]) << 24) ^ ((invSbox[i1] & 0xff) << 16)
                    ^ ((invSbox[i2] & 0xff) << 8) ^ ((invSbox[i3] & 0xff)));

            i0 = (s2 >>> 24);
            i1 = (s1 >> 16) & 0xff;
            i2 = (s0 >> 8) & 0xff;
            i3 = (s5) & 0xff;

            o2 = (((invSbox[i0]) << 24) ^ ((invSbox[i1] & 0xff) << 16)
                    ^ ((invSbox[i2] & 0xff) << 8) ^ ((invSbox[i3] & 0xff)));

            i0 = (s1 >>> 24);
            i1 = (s0 >> 16) & 0xff;
            i2 = (s5 >> 8) & 0xff;
            i3 = (s4) & 0xff;

            o1 = ((invSbox[i0]) << 24) ^ ((invSbox[i1] & 0xff) << 16)
                    ^ ((invSbox[i2] & 0xff) << 8) ^ ((invSbox[i3] & 0xff));

            i0 = (s0 >>> 24);
            i1 = (s5 >> 16) & 0xff;
            i2 = (s4 >> 8) & 0xff;
            i3 = (s3) & 0xff;

            o0 = (invSbox[i0] << 24) ^ ((invSbox[i1] & 0xff) << 16)
                    ^ ((invSbox[i2] & 0xff) << 8) ^ ((invSbox[i3] & 0xff));

            // add round key

            o5 ^= w4[--r];
            o4 ^= w4[--r];
            o3 ^= w4[--r];
            o2 ^= w4[--r];
            o1 ^= w4[--r];
            o0 ^= w4[--r];

            for (int i = nr; --i != 0;) {
                // inv mix columns

                i0 = (o5 >>> 24);
                i1 = (o5 >> 16) & 0xff;
                i2 = (o5 >> 8) & 0xff;
                i3 = (o5) & 0xff;

                s5 = invT0[i0] ^ invT1[i1] ^ invT2[i2] ^ invT3[i3];

                i0 = (o4 >>> 24);
                i1 = (o4 >> 16) & 0xff;
                i2 = (o4 >> 8) & 0xff;
                i3 = (o4) & 0xff;

                s4 = invT0[i0] ^ invT1[i1] ^ invT2[i2] ^ invT3[i3];

                i0 = (o3 >>> 24);
                i1 = (o3 >> 16) & 0xff;
                i2 = (o3 >> 8) & 0xff;
                i3 = (o3) & 0xff;

                s3 = invT0[i0] ^ invT1[i1] ^ invT2[i2] ^ invT3[i3];

                i0 = (o2 >>> 24);
                i1 = (o2 >> 16) & 0xff;
                i2 = (o2 >> 8) & 0xff;
                i3 = (o2) & 0xff;

                s2 = invT0[i0] ^ invT1[i1] ^ invT2[i2] ^ invT3[i3];

                i0 = (o1 >>> 24);
                i1 = (o1 >> 16) & 0xff;
                i2 = (o1 >> 8) & 0xff;
                i3 = (o1) & 0xff;

                s1 = invT0[i0] ^ invT1[i1] ^ invT2[i2] ^ invT3[i3];

                i0 = (o0 >>> 24);
                i1 = (o0 >> 16) & 0xff;
                i2 = (o0 >> 8) & 0xff;
                i3 = (o0) & 0xff;

                s0 = invT0[i0] ^ invT1[i1] ^ invT2[i2] ^ invT3[i3];

                // inv shift row + inv byte sub

                i0 = (s5 >>> 24);
                i1 = (s4 >> 16) & 0xff;
                i2 = (s3 >> 8) & 0xff;
                i3 = (s2) & 0xff;

                o5 = (((invSbox[i0]) << 24) ^ ((invSbox[i1] & 0xff) << 16)
                        ^ ((invSbox[i2] & 0xff) << 8) ^ ((invSbox[i3] & 0xff)));

                i0 = (s4 >>> 24);
                i1 = (s3 >> 16) & 0xff;
                i2 = (s2 >> 8) & 0xff;
                i3 = (s1) & 0xff;

                o4 = (((invSbox[i0]) << 24) ^ ((invSbox[i1] & 0xff) << 16)
                        ^ ((invSbox[i2] & 0xff) << 8) ^ ((invSbox[i3] & 0xff)));

                i0 = (s3 >>> 24);
                i1 = (s2 >> 16) & 0xff;
                i2 = (s1 >> 8) & 0xff;
                i3 = (s0) & 0xff;

                o3 = (((invSbox[i0]) << 24) ^ ((invSbox[i1] & 0xff) << 16)
                        ^ ((invSbox[i2] & 0xff) << 8) ^ ((invSbox[i3] & 0xff)));

                i0 = (s2 >>> 24);
                i1 = (s1 >> 16) & 0xff;
                i2 = (s0 >> 8) & 0xff;
                i3 = (s5) & 0xff;

                o2 = (((invSbox[i0]) << 24) ^ ((invSbox[i1] & 0xff) << 16)
                        ^ ((invSbox[i2] & 0xff) << 8) ^ ((invSbox[i3] & 0xff)));

                i0 = (s1 >>> 24);
                i1 = (s0 >> 16) & 0xff;
                i2 = (s5 >> 8) & 0xff;
                i3 = (s4) & 0xff;

                o1 = ((invSbox[i0]) << 24) ^ ((invSbox[i1] & 0xff) << 16)
                        ^ ((invSbox[i2] & 0xff) << 8) ^ ((invSbox[i3] & 0xff));

                i0 = (s0 >>> 24);
                i1 = (s5 >> 16) & 0xff;
                i2 = (s4 >> 8) & 0xff;
                i3 = (s3) & 0xff;

                o0 = (invSbox[i0] << 24) ^ ((invSbox[i1] & 0xff) << 16)
                        ^ ((invSbox[i2] & 0xff) << 8) ^ ((invSbox[i3] & 0xff));

                // add round key

                o5 ^= w4[--r];
                o4 ^= w4[--r];
                o3 ^= w4[--r];
                o2 ^= w4[--r];
                o1 ^= w4[--r];
                o0 ^= w4[--r];
            }

            if (iv != null) {
                o0 ^= iv00;
                o1 ^= iv01;
                o2 ^= iv02;
                o3 ^= iv03;
                o4 ^= iv04;
                o5 ^= iv05;

                iv00 = iv10;
                iv01 = iv11;
                iv02 = iv12;
                iv03 = iv13;
                iv04 = iv14;
                iv05 = iv15;
            }

            intToBytes(o0, state, offset + 0);
            intToBytes(o1, state, offset + 4);
            intToBytes(o2, state, offset + 8);
            intToBytes(o3, state, offset + 12);
            intToBytes(o4, state, offset + 16);
            intToBytes(o5, state, offset + 20);

            offset += 24;
            size -= 24;
        }

        if (iv != null) {
            intToBytes(iv00, iv, 0);
            intToBytes(iv01, iv, 4);
            intToBytes(iv02, iv, 8);
            intToBytes(iv03, iv, 12);
            intToBytes(iv04, iv, 16);
            intToBytes(iv05, iv, 20);
        }
    }

    /**
     * decrypt with 32 byte (8 word) block
     * 
     * @param state
     *                the block
     */
    private void decrypt8(byte[] state, int offset, int size, byte[] iv) {
        int iv00 = 0;
        int iv01 = 0;
        int iv02 = 0;
        int iv03 = 0;
        int iv04 = 0;
        int iv05 = 0;
        int iv06 = 0;
        int iv07 = 0;

        if (iv != null) {
            iv00 = bytesToInt(iv, 0);
            iv01 = bytesToInt(iv, 4);
            iv02 = bytesToInt(iv, 8);
            iv03 = bytesToInt(iv, 12);
            iv04 = bytesToInt(iv, 16);
            iv05 = bytesToInt(iv, 20);
            iv06 = bytesToInt(iv, 24);
            iv07 = bytesToInt(iv, 28);
        }

        while (size > 0) {
            int s0 = bytesToInt(state, offset + 0);
            int s1 = bytesToInt(state, offset + 4);
            int s2 = bytesToInt(state, offset + 8);
            int s3 = bytesToInt(state, offset + 12);
            int s4 = bytesToInt(state, offset + 16);
            int s5 = bytesToInt(state, offset + 20);
            int s6 = bytesToInt(state, offset + 24);
            int s7 = bytesToInt(state, offset + 28);

            int iv10 = 0;
            int iv11 = 0;
            int iv12 = 0;
            int iv13 = 0;
            int iv14 = 0;
            int iv15 = 0;
            int iv16 = 0;
            int iv17 = 0;

            if (iv != null) {
                iv10 = s0;
                iv11 = s1;
                iv12 = s2;
                iv13 = s3;
                iv14 = s4;
                iv15 = s5;
                iv16 = s6;
                iv17 = s7;
            }

            int i0, i1, i2, i3, o0, o1, o2, o3, o4, o5, o6, o7;

            int r = w4.length;

            // add round key
            s7 ^= w4[--r];
            s6 ^= w4[--r];
            s5 ^= w4[--r];
            s4 ^= w4[--r];
            s3 ^= w4[--r];
            s2 ^= w4[--r];
            s1 ^= w4[--r];
            s0 ^= w4[--r];

            // inv shift row + inv byte sub

            i0 = (s7 >>> 24);
            i1 = (s6 >> 16) & 0xff;
            i2 = (s4 >> 8) & 0xff;
            i3 = (s3) & 0xff;

            o7 = (((invSbox[i0]) << 24) ^ ((invSbox[i1] & 0xff) << 16)
                    ^ ((invSbox[i2] & 0xff) << 8) ^ ((invSbox[i3] & 0xff)));

            i0 = (s6 >>> 24);
            i1 = (s5 >> 16) & 0xff;
            i2 = (s3 >> 8) & 0xff;
            i3 = (s2) & 0xff;

            o6 = (((invSbox[i0]) << 24) ^ ((invSbox[i1] & 0xff) << 16)
                    ^ ((invSbox[i2] & 0xff) << 8) ^ ((invSbox[i3] & 0xff)));

            i0 = (s5 >>> 24);
            i1 = (s4 >> 16) & 0xff;
            i2 = (s2 >> 8) & 0xff;
            i3 = (s1) & 0xff;

            o5 = (((invSbox[i0]) << 24) ^ ((invSbox[i1] & 0xff) << 16)
                    ^ ((invSbox[i2] & 0xff) << 8) ^ ((invSbox[i3] & 0xff)));

            i0 = (s4 >>> 24);
            i1 = (s3 >> 16) & 0xff;
            i2 = (s1 >> 8) & 0xff;
            i3 = (s0) & 0xff;

            o4 = (((invSbox[i0]) << 24) ^ ((invSbox[i1] & 0xff) << 16)
                    ^ ((invSbox[i2] & 0xff) << 8) ^ ((invSbox[i3] & 0xff)));

            i0 = (s3 >>> 24);
            i1 = (s2 >> 16) & 0xff;
            i2 = (s0 >> 8) & 0xff;
            i3 = (s7) & 0xff;

            o3 = (((invSbox[i0]) << 24) ^ ((invSbox[i1] & 0xff) << 16)
                    ^ ((invSbox[i2] & 0xff) << 8) ^ ((invSbox[i3] & 0xff)));

            i0 = (s2 >>> 24);
            i1 = (s1 >> 16) & 0xff;
            i2 = (s7 >> 8) & 0xff;
            i3 = (s6) & 0xff;

            o2 = (((invSbox[i0]) << 24) ^ ((invSbox[i1] & 0xff) << 16)
                    ^ ((invSbox[i2] & 0xff) << 8) ^ ((invSbox[i3] & 0xff)));

            i0 = (s1 >>> 24);
            i1 = (s0 >> 16) & 0xff;
            i2 = (s6 >> 8) & 0xff;
            i3 = (s5) & 0xff;

            o1 = ((invSbox[i0]) << 24) ^ ((invSbox[i1] & 0xff) << 16)
                    ^ ((invSbox[i2] & 0xff) << 8) ^ ((invSbox[i3] & 0xff));

            i0 = (s0 >>> 24);
            i1 = (s7 >> 16) & 0xff;
            i2 = (s5 >> 8) & 0xff;
            i3 = (s4) & 0xff;

            o0 = (invSbox[i0] << 24) ^ ((invSbox[i1] & 0xff) << 16)
                    ^ ((invSbox[i2] & 0xff) << 8) ^ ((invSbox[i3] & 0xff));

            // add round key

            o7 ^= w4[--r];
            o6 ^= w4[--r];
            o5 ^= w4[--r];
            o4 ^= w4[--r];
            o3 ^= w4[--r];
            o2 ^= w4[--r];
            o1 ^= w4[--r];
            o0 ^= w4[--r];

            for (int i = nr; --i != 0;) {
                // inv mix columns

                i0 = (o7 >>> 24);
                i1 = (o7 >> 16) & 0xff;
                i2 = (o7 >> 8) & 0xff;
                i3 = (o7) & 0xff;

                s7 = invT0[i0] ^ invT1[i1] ^ invT2[i2] ^ invT3[i3];

                i0 = (o6 >>> 24);
                i1 = (o6 >> 16) & 0xff;
                i2 = (o6 >> 8) & 0xff;
                i3 = (o6) & 0xff;

                s6 = invT0[i0] ^ invT1[i1] ^ invT2[i2] ^ invT3[i3];

                i0 = (o5 >>> 24);
                i1 = (o5 >> 16) & 0xff;
                i2 = (o5 >> 8) & 0xff;
                i3 = (o5) & 0xff;

                s5 = invT0[i0] ^ invT1[i1] ^ invT2[i2] ^ invT3[i3];

                i0 = (o4 >>> 24);
                i1 = (o4 >> 16) & 0xff;
                i2 = (o4 >> 8) & 0xff;
                i3 = (o4) & 0xff;

                s4 = invT0[i0] ^ invT1[i1] ^ invT2[i2] ^ invT3[i3];

                i0 = (o3 >>> 24);
                i1 = (o3 >> 16) & 0xff;
                i2 = (o3 >> 8) & 0xff;
                i3 = (o3) & 0xff;

                s3 = invT0[i0] ^ invT1[i1] ^ invT2[i2] ^ invT3[i3];

                i0 = (o2 >>> 24);
                i1 = (o2 >> 16) & 0xff;
                i2 = (o2 >> 8) & 0xff;
                i3 = (o2) & 0xff;

                s2 = invT0[i0] ^ invT1[i1] ^ invT2[i2] ^ invT3[i3];

                i0 = (o1 >>> 24);
                i1 = (o1 >> 16) & 0xff;
                i2 = (o1 >> 8) & 0xff;
                i3 = (o1) & 0xff;

                s1 = invT0[i0] ^ invT1[i1] ^ invT2[i2] ^ invT3[i3];

                i0 = (o0 >>> 24);
                i1 = (o0 >> 16) & 0xff;
                i2 = (o0 >> 8) & 0xff;
                i3 = (o0) & 0xff;

                s0 = invT0[i0] ^ invT1[i1] ^ invT2[i2] ^ invT3[i3];

                // inv shift row + inv byte sub

                i0 = (s7 >>> 24);
                i1 = (s6 >> 16) & 0xff;
                i2 = (s4 >> 8) & 0xff;
                i3 = (s3) & 0xff;

                o7 = (((invSbox[i0]) << 24) ^ ((invSbox[i1] & 0xff) << 16)
                        ^ ((invSbox[i2] & 0xff) << 8) ^ ((invSbox[i3] & 0xff)));

                i0 = (s6 >>> 24);
                i1 = (s5 >> 16) & 0xff;
                i2 = (s3 >> 8) & 0xff;
                i3 = (s2) & 0xff;

                o6 = (((invSbox[i0]) << 24) ^ ((invSbox[i1] & 0xff) << 16)
                        ^ ((invSbox[i2] & 0xff) << 8) ^ ((invSbox[i3] & 0xff)));

                i0 = (s5 >>> 24);
                i1 = (s4 >> 16) & 0xff;
                i2 = (s2 >> 8) & 0xff;
                i3 = (s1) & 0xff;

                o5 = (((invSbox[i0]) << 24) ^ ((invSbox[i1] & 0xff) << 16)
                        ^ ((invSbox[i2] & 0xff) << 8) ^ ((invSbox[i3] & 0xff)));

                i0 = (s4 >>> 24);
                i1 = (s3 >> 16) & 0xff;
                i2 = (s1 >> 8) & 0xff;
                i3 = (s0) & 0xff;

                o4 = (((invSbox[i0]) << 24) ^ ((invSbox[i1] & 0xff) << 16)
                        ^ ((invSbox[i2] & 0xff) << 8) ^ ((invSbox[i3] & 0xff)));

                i0 = (s3 >>> 24);
                i1 = (s2 >> 16) & 0xff;
                i2 = (s0 >> 8) & 0xff;
                i3 = (s7) & 0xff;

                o3 = (((invSbox[i0]) << 24) ^ ((invSbox[i1] & 0xff) << 16)
                        ^ ((invSbox[i2] & 0xff) << 8) ^ ((invSbox[i3] & 0xff)));

                i0 = (s2 >>> 24);
                i1 = (s1 >> 16) & 0xff;
                i2 = (s7 >> 8) & 0xff;
                i3 = (s6) & 0xff;

                o2 = (((invSbox[i0]) << 24) ^ ((invSbox[i1] & 0xff) << 16)
                        ^ ((invSbox[i2] & 0xff) << 8) ^ ((invSbox[i3] & 0xff)));

                i0 = (s1 >>> 24);
                i1 = (s0 >> 16) & 0xff;
                i2 = (s6 >> 8) & 0xff;
                i3 = (s5) & 0xff;

                o1 = ((invSbox[i0]) << 24) ^ ((invSbox[i1] & 0xff) << 16)
                        ^ ((invSbox[i2] & 0xff) << 8) ^ ((invSbox[i3] & 0xff));

                i0 = (s0 >>> 24);
                i1 = (s7 >> 16) & 0xff;
                i2 = (s5 >> 8) & 0xff;
                i3 = (s4) & 0xff;

                o0 = (invSbox[i0] << 24) ^ ((invSbox[i1] & 0xff) << 16)
                        ^ ((invSbox[i2] & 0xff) << 8) ^ ((invSbox[i3] & 0xff));

                // add round key

                o7 ^= w4[--r];
                o6 ^= w4[--r];
                o5 ^= w4[--r];
                o4 ^= w4[--r];
                o3 ^= w4[--r];
                o2 ^= w4[--r];
                o1 ^= w4[--r];
                o0 ^= w4[--r];
            }

            if (iv != null) {
                o0 ^= iv00;
                o1 ^= iv01;
                o2 ^= iv02;
                o3 ^= iv03;
                o4 ^= iv04;
                o5 ^= iv05;
                o6 ^= iv06;
                o7 ^= iv07;

                iv00 = iv10;
                iv01 = iv11;
                iv02 = iv12;
                iv03 = iv13;
                iv04 = iv14;
                iv05 = iv15;
                iv06 = iv16;
                iv07 = iv17;
            }

            intToBytes(o0, state, offset + 0);
            intToBytes(o1, state, offset + 4);
            intToBytes(o2, state, offset + 8);
            intToBytes(o3, state, offset + 12);
            intToBytes(o4, state, offset + 16);
            intToBytes(o5, state, offset + 20);
            intToBytes(o6, state, offset + 24);
            intToBytes(o7, state, offset + 28);

            offset += 32;
            size -= 32;
        }

        if (iv != null) {
            intToBytes(iv00, iv, 0);
            intToBytes(iv01, iv, 4);
            intToBytes(iv02, iv, 8);
            intToBytes(iv03, iv, 12);
            intToBytes(iv04, iv, 16);
            intToBytes(iv05, iv, 20);
            intToBytes(iv06, iv, 24);
            intToBytes(iv07, iv, 28);
        }
    }

    /**
     * the byte substitution transform
     */
    private static final byte[] sbox;

    /**
     * inverse of sbox
     */
    private static final byte[] invSbox;

    /**
     * sbox + mix columns transformation column 1
     */
    private static final int[] t0;

    /**
     * sbox + mix columns transformation column 2
     */
    private static final int[] t1;

    /**
     * sbox + mix columns transformation column 3
     */
    private static final int[] t2;

    /**
     * sbox + mix columns transformation column 4
     */
    private static final int[] t3;

    /**
     * inverse mix columns transformation column 1
     */
    private static final int[] invT0;

    /**
     * inverse mix columns transformation column 2
     */
    private static final int[] invT1;

    /**
     * inverse mix columns transformation column 3
     */
    private static final int[] invT2;

    /**
     * inverse mix columns transformation column 4
     */
    private static final int[] invT3;

    /**
     * powers of two
     */
    private static final byte[] pow2;

    static {
        byte[] mul2 = new byte[0x100];
        for (int i = 0; i < mul2.length; i++) {
            mul2[i] = (byte) ((i << 1) ^ ((i & 0x80) == 0 ? 0 : 0x1b));
        }

        byte[] mulInv = new byte[0x100];
        mulInv[0] = 0;
        outer: for (int i = 1; i < 0x100; i++) {
            if (mulInv[i] != 0) {
                continue;
            }
            for (int j = i; j < 0x100; j++) {
                // out = mul(i, j)
                int x = j;
                int out = 0;
                for (int k = 0; k < 8; k++) {
                    if ((i & (0x1 << k)) != 0) {
                        out ^= x;
                    }
                    x = mul2[((byte) x) & 0xff];
                }
                out &= 0xff;

                if (out == 1) {
                    mulInv[i] = (byte) j;
                    mulInv[j] = (byte) i;
                    continue outer;
                }
            }
            throw new Error("no inverse: " + i);
        }

        pow2 = new byte[16];
        pow2[0] = 1;
        for (int i = 1; i < pow2.length; i++) {
            pow2[i] = mul2[pow2[i - 1] & 0xff];
        }

        byte[] mul4 = new byte[0x100];
        for (int i = 0; i < mul4.length; i++) {
            mul4[i] = mul2[mul2[i] & 0xff];
        }

        byte[] mul8 = new byte[0x100];
        for (int i = 0; i < mul8.length; i++) {
            mul8[i] = mul2[mul4[i] & 0xff];
        }

        byte[] mul09 = new byte[0x100];
        // 1001
        for (int i = 0; i < mul09.length; i++) {
            mul09[i] = (byte) (mul8[i] ^ i);
        }

        byte[] mul0b = new byte[0x100];
        // 1011
        for (int i = 0; i < mul0b.length; i++) {
            mul0b[i] = (byte) (mul09[i] ^ mul2[i]);
        }

        byte[] mul0d = new byte[0x100];
        // 1101
        for (int i = 0; i < mul0d.length; i++) {
            mul0d[i] = (byte) (mul8[i] ^ mul4[i] ^ i);
        }

        byte[] mul0e = new byte[0x100];
        // 1110
        for (int i = 0; i < mul0e.length; i++) {
            mul0e[i] = (byte) (mul8[i] ^ mul4[i] ^ mul2[i]);
        }

        sbox = new byte[0x100];
        invSbox = new byte[0x100];
        for (int i = 0; i < sbox.length; i++) {
            byte b = mulInv[((byte) i) & 0xff];

            boolean x0 = (b & 0x01) != 0;
            boolean x1 = (b & 0x02) != 0;
            boolean x2 = (b & 0x04) != 0;
            boolean x3 = (b & 0x08) != 0;
            boolean x4 = (b & 0x10) != 0;
            boolean x5 = (b & 0x20) != 0;
            boolean x6 = (b & 0x40) != 0;
            boolean x7 = (b & 0x80) != 0;

            boolean y0 = x0 ^ x4 ^ x5 ^ x6 ^ x7 ^ true;
            boolean y1 = x1 ^ x5 ^ x6 ^ x7 ^ x0 ^ true;
            boolean y2 = x2 ^ x6 ^ x7 ^ x0 ^ x1 ^ false;
            boolean y3 = x3 ^ x7 ^ x0 ^ x1 ^ x2 ^ false;
            boolean y4 = x4 ^ x0 ^ x1 ^ x2 ^ x3 ^ false;
            boolean y5 = x5 ^ x1 ^ x2 ^ x3 ^ x4 ^ true;
            boolean y6 = x6 ^ x2 ^ x3 ^ x4 ^ x5 ^ true;
            boolean y7 = x7 ^ x3 ^ x4 ^ x5 ^ x6 ^ false;

            sbox[i] = (byte) ((y0 ? 0x01 : 0) | (y1 ? 0x02 : 0)
                    | (y2 ? 0x04 : 0) | (y3 ? 0x08 : 0) | (y4 ? 0x10 : 0)
                    | (y5 ? 0x20 : 0) | (y6 ? 0x40 : 0) | (y7 ? 0x80 : 0));
            invSbox[sbox[i] & 0xff] = (byte) i;
        }

        t0 = new int[0x100];
        t1 = new int[0x100];
        t2 = new int[0x100];
        t3 = new int[0x100];
        for (int i = 0; i < 0x100; i++) {
            int a = sbox[i] & 0xff;
            int o0 = (mul2[a] & 0xff);
            int o1 = a;
            int o2 = a;
            int o3 = (mul2[a] & 0xff) ^ a;
            t0[i] = o0 << 24 | o1 << 16 | o2 << 8 | o3;
            t1[i] = o3 << 24 | o0 << 16 | o1 << 8 | o2;
            t2[i] = o2 << 24 | o3 << 16 | o0 << 8 | o1;
            t3[i] = o1 << 24 | o2 << 16 | o3 << 8 | o0;
        }

        invT0 = new int[0x100];
        invT1 = new int[0x100];
        invT2 = new int[0x100];
        invT3 = new int[0x100];
        for (int i = 0; i < 0x100; i++) {
            int a = i;
            int o0 = (mul0e[a] & 0xff);
            int o1 = (mul09[a] & 0xff);
            int o2 = (mul0d[a] & 0xff);
            int o3 = (mul0b[a] & 0xff);
            invT0[i] = o0 << 24 | o1 << 16 | o2 << 8 | o3;
            invT1[i] = o3 << 24 | o0 << 16 | o1 << 8 | o2;
            invT2[i] = o2 << 24 | o3 << 16 | o0 << 8 | o1;
            invT3[i] = o1 << 24 | o2 << 16 | o3 << 8 | o0;
        }
    }
}
