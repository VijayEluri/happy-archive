package org.yi.happy.crypt;

import org.junit.Assert;
import org.junit.Test;
import org.yi.happy.archive.key.HexDecode;

/**
 * test encryption and decryption of Rijndael, this test data comes from the old
 * feather crypto tests.
 */
public class RijndaelTest extends Assert {
    private static final String KEY128 = "0123456789abcdef"
	    + "0123456789abcdef";

    private static final String CLEAR128X2 = KEY128 + KEY128;

    private static final String ECB128X2 = "a1ee5608b33af054"
	    + "70858608d1de080f" + "a1ee5608b33af054" + "70858608d1de080f";

    /**
     * encrypt two 128 bit blocks with a 128 bit key, ECB mode
     */
    @Test
    public void testEncrypt128() {
	Rijndael r = new Rijndael(16, 16);

	byte[] key = HexDecode.decode(KEY128);
	byte[] have = HexDecode.decode(CLEAR128X2);
	byte[] want = HexDecode.decode(ECB128X2);

	r.setKey(key);
	r.encryptEcb(have);

	assertArrayEquals(want, have);
    }

    /**
     * decrypt two 128 bit blocks with a 128 bit key, ECB mode
     */
    @Test
    public void testDecrypt128() {
	Rijndael r = new Rijndael(16, 16);

	byte[] key = HexDecode.decode(KEY128);
	byte[] want = HexDecode.decode(CLEAR128X2);
	byte[] have = HexDecode.decode(ECB128X2);

	r.setKey(key);
	r.decryptEcb(have);

	assertArrayEquals(want, have);
    }

    private static final String KEY192 = "0123456789abcdef"
	    + "0123456789abcdef" + "0123456789abcdef";

    private static final String CLEAR192X2 = KEY192 + KEY192;

    private static final String ECB192X2 = "b5155708ec46611c"
	    + "73d259fd442a678e" + "e2f0ee0edc90e54e" + "b5155708ec46611c"
	    + "73d259fd442a678e" + "e2f0ee0edc90e54e";

    /**
     * encrypt two 192 bit blocks with a 192 bit key, ECB mode
     */
    @Test
    public void testEncrypt192() {
	byte[] key = HexDecode.decode(KEY192);
	byte[] have = HexDecode.decode(CLEAR192X2);
	byte[] want = HexDecode.decode(ECB192X2);

	Rijndael r = new Rijndael(24, 24);
	r.setKey(key);
	r.encryptEcb(have);

	assertArrayEquals(want, have);
    }

    /**
     * decrypt two 192 bit blocks with a 192 bit key, ECB mode
     */
    @Test
    public void testDecrypt192() {
	byte[] key = HexDecode.decode(KEY192);
	byte[] have = HexDecode.decode(ECB192X2);
	byte[] want = HexDecode.decode(CLEAR192X2);

	Rijndael r = new Rijndael(24, 24);
	r.setKey(key);
	r.decryptEcb(have);

	assertArrayEquals(want, have);
    }

    private static final String KEY256 = "0123456789abcdef"
	    + "0123456789abcdef" + "0123456789abcdef" + "0123456789abcdef";

    private static final String CLEAR256X2 = KEY256 + KEY256;

    private static final String ECB256X2 = "9300360fa9415a82"
	    + "d7324fec8c9e6a29" + "fed1579f14220215" + "10a9ea55106106c2"
	    + "9300360fa9415a82" + "d7324fec8c9e6a29" + "fed1579f14220215"
	    + "10a9ea55106106c2";

    /**
     * encrypt two 256 bit blocks with a 256 bit key, ECB mode
     */
    @Test
    public void testEncrypt256() {
	byte[] key = HexDecode.decode(KEY256);
	byte[] have = HexDecode.decode(CLEAR256X2);
	byte[] want = HexDecode.decode(ECB256X2);

	Rijndael r = new Rijndael(32, 32);
	r.setKey(key);
	r.encryptEcb(have);

	assertArrayEquals(want, have);
    }

    /**
     * decrypt two 256 bit blocks with a 256 bit key, ECB mode
     */
    @Test
    public void testDecrypt256() {
	byte[] key = HexDecode.decode(KEY256);
	byte[] have = HexDecode.decode(ECB256X2);
	byte[] want = HexDecode.decode(CLEAR256X2);

	Rijndael r = new Rijndael(32, 32);
	r.setKey(key);
	r.decryptEcb(have);

	assertArrayEquals(want, have);
    }

    private static final String CBC128X2 = "a1ee5608b33af054"
	    + "70858608d1de080f" + "e4f1aa57e76ae103" + "28f9e014b0215ed4";

    /**
     * encrypt two 128 bit blocks with a 128 bit key, CBC mode
     */
    @Test
    public void testEncrypt128Cbc() {
	byte[] key = HexDecode.decode(KEY128);
	byte[] have = HexDecode.decode(CLEAR128X2);
	byte[] want = HexDecode.decode(CBC128X2);
	byte[] iv = new byte[16];

	Rijndael r = new Rijndael(16, 16);
	r.setKey(key);
	r.encryptCbc(have, iv);

	assertArrayEquals(want, have);
    }

    /**
     * decrypt two 128 bit blocks with a 128 bit key, CBC mode
     */
    @Test
    public void testDecrypt128Cbc() {
	byte[] key = HexDecode.decode(KEY128);
	byte[] have = HexDecode.decode(CBC128X2);
	byte[] want = HexDecode.decode(CLEAR128X2);
	byte[] iv = new byte[16];

	Rijndael r = new Rijndael(16, 16);
	r.setKey(key);
	r.decryptCbc(have, iv);

	assertArrayEquals(want, have);
    }

    private static final String CBC192X2 = "b5155708ec46611c"
	    + "73d259fd442a678e" + "e2f0ee0edc90e54e" + "8ec3e4512df8d73d"
	    + "4d118adc5e11e64f" + "ca3f26d8467d9f84";

    /**
     * encrypt two 192 bit blocks with a 192 bit key, CBC mode
     */
    @Test
    public void testEncrypt192Cbc() {
	byte[] key = HexDecode.decode(KEY192);
	byte[] have = HexDecode.decode(CLEAR192X2);
	byte[] want = HexDecode.decode(CBC192X2);
	byte[] iv = new byte[24];

	Rijndael r = new Rijndael(24, 24);
	r.setKey(key);
	r.encryptCbc(have, iv);

	assertArrayEquals(want, have);
    }

    /**
     * decrypt two 192 bit blocks with a 192 bit key, CBC mode
     */
    @Test
    public void testDecrypt192Cbc() {
	byte[] key = HexDecode.decode(KEY192);
	byte[] have = HexDecode.decode(CBC192X2);
	byte[] want = HexDecode.decode(CLEAR192X2);
	byte[] iv = new byte[24];

	Rijndael r = new Rijndael(24, 24);
	r.setKey(key);
	r.decryptCbc(have, iv);

	assertArrayEquals(want, have);
    }

    private static final String CBC256X2 = "9300360fa9415a82"
	    + "d7324fec8c9e6a29" + "fed1579f14220215" + "10a9ea55106106c2"
	    + "c089999dfb6cfc5b" + "accaac28f2848132" + "a3b880125bf0fe59"
	    + "a8893521a8863a2d";

    /**
     * encrypt two 256 bit blocks with a 256 bit key, CBC mode
     */
    @Test
    public void testEncrypt256Cbc() {
	byte[] key = HexDecode.decode(KEY256);
	byte[] have = HexDecode.decode(CLEAR256X2);
	byte[] want = HexDecode.decode(CBC256X2);
	byte[] iv = new byte[32];

	Rijndael r = new Rijndael(32, 32);
	r.setKey(key);
	r.encryptCbc(have, iv);

	assertArrayEquals(want, have);
    }

    /**
     * decrypt two 256 bit blocks with a 256 bit key, CBC mode
     */
    @Test
    public void testDecrypt256Cbc() {
	byte[] key = HexDecode.decode(KEY256);
	byte[] have = HexDecode.decode(CBC256X2);
	byte[] want = HexDecode.decode(CLEAR256X2);
	byte[] iv = new byte[32];

	Rijndael r = new Rijndael(32, 32);
	r.setKey(key);
	r.decryptCbc(have, iv);

	assertArrayEquals(want, have);
    }

}
