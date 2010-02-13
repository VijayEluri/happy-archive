package org.yi.happy.archive.block;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.junit.Test;
import org.yi.happy.annotate.MisplacedTest;
import org.yi.happy.archive.crypto.DigestFactory;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.KeyParse;
import org.yi.happy.archive.key.NameFullKey;
import org.yi.happy.archive.test_data.TestData;

public class EncodedBlockTest {
    @Test
    @MisplacedTest(ContentEncodedBlockTest.class)
    public void testDecodeOldContent() throws IOException {
	EncodedBlock b = TestData.KEY_OLD_CONTENT.getEncodedBlock();

	Block have = b.decode(TestData.KEY_OLD_CONTENT.getFullKey());

	Block want = TestData.CLEAR_CONTENT.getBlock();
	assertEquals(want, have);
    }

    @Test
    @MisplacedTest(ContentEncodedBlockTest.class)
    public void testDecodeContent() throws IOException {
	EncodedBlock b = TestData.KEY_CONTENT.getEncodedBlock();

	Block have = b.decode(TestData.KEY_CONTENT.getFullKey());

	Block want = TestData.CLEAR_CONTENT.getBlock();
	assertEquals(want, have);
    }

    @Test
    @MisplacedTest
    public void testDecodeName() throws IOException {
	EncodedBlock b = TestData.KEY_NAME.getEncodedBlock();

	Block have = b.decode(TestData.KEY_NAME.getFullKey());

	Block want = TestData.CLEAR_CONTENT.getBlock();
	assertEquals(want, have);
    }

    /**
     * decode a new content block with the wrong pass, the resulting block will
     * be able to be parsed but will be nonsense.
     * 
     * @throws IOException
     */
    @Test
    @MisplacedTest
    public void testContentDecodeBadKey() throws IOException {
	EncodedBlock b = TestData.KEY_CONTENT.getEncodedBlock();

	FullKey key = KeyParse.parseFullKey("content-hash:87c5f6fe"
		+ "4ea801c8eb227b8b218a0659c18ece76b7c200c645ab4364becf"
		+ "68d5:f6bd9f3b01b4ee40f60df2dc622f9d6f3aa38a5673a87e8"
		+ "20b40164e930edead");
	Block c = b.decode(key);

	assertFalse(TestData.CLEAR_CONTENT.getBlock().equals(c));
    }

    /**
     * decode a new content block with the wrong locator, but right pass, this
     * succeeds.
     * 
     * @throws IOException
     */
    @Test
    @MisplacedTest
    public void testContentDecodeBadLocator() throws IOException {
	EncodedBlock b = TestData.KEY_CONTENT.getEncodedBlock();

	FullKey key = KeyParse.parseFullKey("content-hash:87c5f6fe"
		+ "4ea801c8eb227b8b218a0659c18ece76b7c200c645ab4364becf"
		+ "68df:f6bd9f3b01b4ee40f60df2dc622f9d6f3aa38a5673a87e8"
		+ "20b40164e930edeac");
	Block have = b.decode(key);

	Block want = TestData.CLEAR_CONTENT.getBlock();
	assertEquals(want, have);
    }

    /**
     * Decode a name encoded block using the wrong key, with the new tolerant
     * parser the resulting block will parse but will be nonsense.
     * 
     * @throws IOException
     */
    @Test
    @MisplacedTest
    public void testNameDecodeBad() throws IOException {
	EncodedBlock b = TestData.KEY_NAME.getEncodedBlock();

	NameFullKey k = new NameFullKey(DigestFactory.getProvider("sha-256"),
		"test2");
	Block c = b.decode(k);

	assertFalse(TestData.CLEAR_CONTENT.getBlock().equals(c));
    }

    @Test
    @MisplacedTest
    public void testNameDecode() throws IOException {
	EncodedBlock b = TestData.KEY_NAME.getEncodedBlock();

	Block have = b.decode(TestData.KEY_NAME.getFullKey());

	Block want = TestData.CLEAR_CONTENT.getBlock();
	assertEquals(want, have);
    }
}
