package org.yi.happy.archive.file_system;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.yi.happy.archive.ByteString;

public class FakeFileSystemTest {
    private FakeFileSystem fake;
    private FileSystem real;

    @Before
    public void before() {
	fake = new FakeFileSystem();
	real = fake;
    }

    public void after() {
	fake = null;
	real = null;
    }

    @Test
    public void testLoad() throws IOException {
	fake.save("test.dat", new byte[0]);

	byte[] data = real.load("test.dat");

	assertArrayEquals(new byte[0], data);
    }

    @Test
    public void testLoad2() throws IOException {
	fake.save("test.dat", new byte[5]);

	byte[] data = real.load("test.dat");

	assertArrayEquals(new byte[5], data);
    }

    @Test(expected = IOException.class)
    public void testLoadLimit() throws IOException {
	fake.save("test.dat", ByteString.toUtf8("Hello\n"));

	real.load("test.dat", 5);
    }

    @Test
    public void testOpenInput() throws Exception {
	fake.save("test.dat", ByteString.toUtf8("Hello\n"));

	InputStream in = real.openInputStream("test.dat");

	assertNotNull(in);
	byte[] data = new byte[10];
	assertEquals(6, in.read(data));
    }
}
