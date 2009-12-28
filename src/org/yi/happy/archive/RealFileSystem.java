package org.yi.happy.archive;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class RealFileSystem implements FileSystem {

	@Override
	public byte[] load(String name) throws IOException {
		FileInputStream in = new FileInputStream(name);
		try {
			return Streams.load(in);
		} finally {
			in.close();
		}
	}

	@Override
	public byte[] load(String name, int limit) throws IOException {
		FileInputStream in = new FileInputStream(name);
		try {
			return Streams.load(in, limit);
		} finally {
			in.close();
		}
	}

	@Override
	public InputStream openInputStream(String name) throws IOException {
		return new FileInputStream(name);
	}

}
