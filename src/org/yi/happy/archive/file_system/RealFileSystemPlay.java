package org.yi.happy.archive.file_system;

import java.io.File;
import java.io.IOException;

import org.yi.happy.annotate.GlobalFilesystem;

/**
 * Experiment with {@link RealFileSystem}.
 */
public class RealFileSystemPlay {
    /**
     * experiement with {@link RealFileSystem}.
     * 
     * @param args
     * @throws IOException
     */
    public static void main1(String[] args) throws IOException {
        RealFileSystem fs = new RealFileSystem();
        fs.mkdir("a");

        /*
         * save a temporary file
         */
        fs.save("a/a.tmp", new byte[] { 1 });

        /*
         * rename to a final file
         */
        fs.rename("a/a.tmp", "a/a");

        /*
         * load the final file
         */
        System.out.println(fs.load("a/a")[0]);

        /*
         * save a temporary file
         */
        fs.save("a/a.tmp", new byte[] { 2 });

        /*
         * rename to a final file
         */
        fs.rename("a/a.tmp", "a/a");

        /*
         * load the final file
         */
        System.out.println(fs.load("a/a")[0]);

        fs.mkdir("a/b");

        fs.rename("a/a", "a/b");
    }

    /**
     * 
     * @param args
     */
    @GlobalFilesystem
    public static void main(String[] args) {
        File f = new File("test/");

        System.out.println(f);
    }
}
