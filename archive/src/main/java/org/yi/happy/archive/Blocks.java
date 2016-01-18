package org.yi.happy.archive;

/**
 * utility methods for dealing with blocks.
 */
public class Blocks {
    /**
     * The maximum expected size of an entire block (including meta-data) in
     * bytes. This is larger than any existing block.
     */
    public static int MAX_SIZE = 1024 * 1024 + 8 * 1024;
}
