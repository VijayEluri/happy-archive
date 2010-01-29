package org.yi.happy.archive.file_system;

import java.io.IOException;

public class RealFileSystemPlay {
    public static void main(String[] args) throws IOException {
	RealFileSystem fs = new RealFileSystem();
	fs.save("a/b", new byte[0]);
    }
}
