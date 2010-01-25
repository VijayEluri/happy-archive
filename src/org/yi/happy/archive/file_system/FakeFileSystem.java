package org.yi.happy.archive.file_system;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FakeFileSystem implements FileSystem {

    private Map<String, byte[]> files = new HashMap<String, byte[]>();

    @Override
    public void save(String name, byte[] bytes) throws IOException {
	files.put(name, bytes.clone());
    }

    @Override
    public byte[] load(String name) throws IOException {
	byte[] data = files.get(name);

	if (data == null) {
	    throw new FileNotFoundException();
	}

	return data.clone();
    }

    @Override
    public byte[] load(String name, int limit) throws IOException {
	byte[] data = files.get(name);

	if (data == null) {
	    throw new FileNotFoundException();
	}

	if (data.length > limit) {
	    throw new IOException();
	}

	return data.clone();
    }

    @Override
    public InputStream openInputStream(String name) throws IOException {
	return new ByteArrayInputStream(files.get(name));
    }

    @Override
    public String join(String base, String name) {
	return base + "/" + name;
    }

}
