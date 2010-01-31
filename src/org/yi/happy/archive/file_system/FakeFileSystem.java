package org.yi.happy.archive.file_system;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FakeFileSystem implements FileSystem {

    private Map<String, byte[]> files = new HashMap<String, byte[]>();

    private final byte[] DIR = new byte[0];

    @Override
    public void save(String name, byte[] bytes) throws IOException {
	if (name.contains("/")
		&& files.get(name.replaceAll("/[^/]*$", "")) != DIR) {
	    throw new FileNotFoundException("parent does not exist or is"
		    + " not a folder");
	}

	if (files.get(name) == DIR) {
	    throw new IOException();
	}

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

    @Override
    public boolean mkdir(String path) throws IOException {
	if (path.contains("/")
		&& files.get(path.replaceAll("/[^/]*$", "")) != DIR) {
	    throw new FileNotFoundException("parent does is not a folder");
	}

	byte[] cur = files.get(path);
	if (cur == DIR) {
	    return false;
	}

	if (cur != null) {
	    throw new IOException();
	}

	files.put(path, DIR);
	return true;
    }

    public void rename(String from, String to) throws IOException {
	/*
	 * if the source does not exist, throw
	 */
	if (!files.containsKey(from)) {
	    throw new FileNotFoundException();
	}

	/*
	 * if the parent of the destination does not exist, throw
	 */
	/*
	 * TODO extract parent check to a method
	 */
	if (to.contains("/") && files.get(to.replaceAll("/[^/]*$", "")) != DIR) {
	    throw new FileNotFoundException("parent is not a folder");
	}

	/*
	 * if the destination is a directory, throw
	 */
	if (files.get(to) == DIR) {
	    throw new IOException();
	}

	/*
	 * if the source is a directory and the target exists, throw
	 */
	if (files.get(from) == DIR && files.containsKey(to)) {
	    throw new IOException();
	}

	/*
	 * actually do the rename
	 */

	files.put(to, files.remove(from));
    }

    public boolean exists(String path) {
	return files.containsKey(path);
    }
}
