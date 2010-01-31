package org.yi.happy.archive;

public class Blocks {
    /**
     * The maximum size of a block in bytes. This is much larger than any
     * existing block.
     */
    public static int MAX_SIZE = 4 * 1024 * 1024;

    public static byte[] getEndl() {
	return new byte[] { 0x0d, 0x0a };
    }
}
