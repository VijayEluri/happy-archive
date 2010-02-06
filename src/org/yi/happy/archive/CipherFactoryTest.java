package org.yi.happy.archive;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.yi.happy.archive.key.UnknownAlgorithmException;


public class CipherFactoryTest {
    /**
     * setting a bad cipher should fail right away
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBadCipher() {
	CipherFactory.create("bad");
    }

    @Test
    public void testBadProvider() {
	CipherProvider got = CipherFactory.getProvider("bad");

	assertEquals("bad", got.getAlgorithm());
    }

    @Test(expected = UnknownAlgorithmException.class)
    public void testBadProvider2() {
	CipherProvider got = CipherFactory.getProvider("bad");

	got.get();
    }
}
