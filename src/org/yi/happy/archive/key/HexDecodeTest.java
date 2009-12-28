package org.yi.happy.archive.key;

import org.junit.Assert;
import org.junit.Test;

/**
 * exercise HexDecode and HexEncode
 */
public class HexDecodeTest extends Assert {
	private static final byte[] dataByte = { (byte) 0xff, (byte) 0xfe, 0x00 };

    private static final String dataHex = "fffe00";

    private static final String dataHex2 = "ffFe00";

    /**
     * turn hex into bytes
     * 
     */
    @Test
    public void testHexToByte() {
        byte[] e = dataByte;

        byte[] h = HexDecode.decode(dataHex);

        assertArrayEquals(e, h);
    }

    /**
     * turn hex with mixed case into bytes
     * 
     */
    @Test
    public void testHexToByte2() {
        byte[] e = dataByte;

        byte[] h = HexDecode.decode(dataHex2);

        assertArrayEquals(e, h);
    }

    /**
     * turn bytes into hex
     * 
     */
    @Test
    public void testByteToHex() {
        String e = dataHex;

        String h = HexEncode.encode(dataByte);

        assertEquals(e, h);
    }
}
