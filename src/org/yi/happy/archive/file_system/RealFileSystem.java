package org.yi.happy.archive.file_system;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.yi.happy.archive.Streams;

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

	@Override
	public void save(String name, byte[] bytes) throws IOException {
		FileOutputStream out = new FileOutputStream(name);
		try {
			out.write(bytes);
		} finally {
			out.close();
		}
	}
}
