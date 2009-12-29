package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;

public interface FileSystem {

	byte[] load(String name) throws IOException;

	InputStream openInputStream(String name) throws IOException;

	byte[] load(String name, int limit) throws IOException;

	void save(String name, byte[] bytes) throws IOException;

}
