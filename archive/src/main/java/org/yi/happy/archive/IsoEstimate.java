package org.yi.happy.archive;

/**
 * Estimate the size of an ISO9660 file system, assuming no extensions, no
 * directories, and every file has an exactly 8.3 file name.
 */
public class IsoEstimate {
    private int fileCount;
    private int dataSectors;

    private static final int sectorSize = 2048;

    /**
     * Add a file size to the estimate.
     * 
     * @param fileSize
     *            the file of the file to add to the estimate.
     */
    public void add(long fileSize) {
        fileCount += 1;
        dataSectors += (fileSize + 2048 - 1) / 2048;
    }

    /**
     * @return the number of file sizes added to the estimate.
     */
    public int getCount() {
        return fileCount;
    }

    /**
     * @return the total size of file data, in sectors.
     */
    public int getDataSectors() {
        return dataSectors;
    }

    /**
     * @return the size of the image in sectors.
     */
    public int getTotalSectors() {
        return 174 + fileCount / 42 + dataSectors;
    }

    /**
     * @return the size in bytes of a sector.
     */
    public int getSectorSize() {
        return sectorSize;
    }

    /**
     * @return the size in bytes of the image.
     */
    public long getSize() {
        return (long) getSectorSize() * (long) getTotalSectors();
    }

    /**
     * remove a file size from the estimate. This assumes that the file size was
     * indeed added to the estimate already.
     * 
     * @param fileSize
     *            the file size to remove.
     */
    public void remove(long fileSize) {
        fileCount -= 1;
        dataSectors -= (fileSize + 2048 - 1) / 2048;
    }

    /**
     * Get the size of the image in millions of bytes, rounding up.
     * 
     * @return the size of the image in millions of bytes.
     */
    public long getMegaSize() {
        return (getSize() + 999999l) / 1000000l;
    }

}
