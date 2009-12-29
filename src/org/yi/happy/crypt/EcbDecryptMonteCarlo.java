package org.yi.happy.crypt;

import java.io.PrintStream;

import org.yi.happy.archive.key.HexEncode;

public class EcbDecryptMonteCarlo {
	private static PrintStream out;

	public static void main(String[] args) {
		out = System.out;

		run(16);
		run(24);
		run(32);
	}

	private static void run(int keySize) {
		Rijndael t = new Rijndael(16, keySize);

		record("KEYSIZE", keySize * 8);

		byte[] key = new byte[keySize];
		byte[] pt = new byte[16];

		for (int i = 0; i < 400; i++) {
			record();
			record("I", i);
			record("KEY", key);
			record("CT", pt);

			t.setKey(key);

			byte[] prev = null;
			for (int j = 0; j < 10000; j++) {
				prev = pt.clone();
				t.decryptEcb(pt);
			}

			record("PT", pt);

			int k = 0;
			if (keySize > pt.length) {
				int j = pt.length * 2 - keySize;
				for (; j < pt.length; j++) {
					key[k++] ^= prev[j];
				}
			}
			for (int j = 0; j < pt.length; j++) {
				key[k++] ^= pt[j];
			}
		}

		record();
		record();
	}

	private static void record() {
		out.println();
	}

	private static void record(String string, byte[] bytes) {
		out.println(string + "=" + HexEncode.encode(bytes).toUpperCase());
	}

	private static void record(String string, int i) {
		out.println(string + "=" + i);
	}
}
