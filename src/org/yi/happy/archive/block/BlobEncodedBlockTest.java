package org.yi.happy.archive.block;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.yi.happy.annotate.SmellsMessy;
import org.yi.happy.archive.ByteString;
import org.yi.happy.archive.Bytes;
import org.yi.happy.archive.crypto.CipherFactory;
import org.yi.happy.archive.crypto.DigestFactory;
import org.yi.happy.archive.key.BlobLocatorKey;
import org.yi.happy.archive.key.HexDecode;

@SmellsMessy
public class BlobEncodedBlockTest {
    @Test
    public void test1() {
	Block block = new BlobEncodedBlock(
		DigestFactory.getProvider("sha-256"), CipherFactory
			.getProvider("null"), new Bytes(ByteString
			.toUtf8("test")));

	Map<String, String> want = new HashMap<String, String>();
	want.put("key-type", "blob");
	want
		.put("key",
			"321f47896a9ae31c24d307120f2736686d3258d55a4c3890a35ec594049bd49d");
	want.put("cipher", "null");
	want.put("digest", "sha-256");
	want.put("size", "4");
	assertEquals(want, block.getMeta());
    }

    @Test
    public void test2() {
	Block block = new BlobEncodedBlock(new BlobLocatorKey(HexDecode
		.decode("321f47896a9ae31c24d307120f2736686d32"
			+ "58d55a4c3890a35ec594049bd49d")), DigestFactory
		.getProvider("sha-256"), CipherFactory.getProvider("null"),
		new Bytes(ByteString.toUtf8("test")));

	assertEquals("blob", block.getMeta().get("key-type"));
	assertEquals(
		"321f47896a9ae31c24d307120f2736686d3258d55a4c3890a35ec594049bd49d",
		block.getMeta().get("key"));
	assertEquals("null", block.getMeta().get("cipher"));
	assertEquals("sha-256", block.getMeta().get("digest"));
	assertEquals("4", block.getMeta().get("size"));
    }

    @Test
    public void testAsBytes() {
	Block block = new BlobEncodedBlock(
		DigestFactory.getProvider("sha-256"), CipherFactory
			.getProvider("null"), new Bytes(ByteString
			.toUtf8("test")));

	byte[] expect = ByteString.toUtf8("key-type: blob\r\n"
		+ "key: 321f47896a9ae31c24d307120f2736686d3258d55a4c3890a35ec5"
		+ "94049bd49d\r\n" + "digest: sha-256\r\n" + "cipher: null\r\n"
		+ "size: 4\r\n" + "\r\n" + "test");

	assertArrayEquals(expect, block.asBytes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test3() {
	new BlobEncodedBlock(new BlobLocatorKey(HexDecode.decode("912c1323"
		+ "a2d91e557db3ceade00289b8a4350ed0b336ae25a92dcf75167fe163")),
		DigestFactory.getProvider("sha-256"), CipherFactory
			.getProvider("null"), new Bytes(new byte[] { 0x01 }));
    }
}
