package org.yi.happy.archive.crypto;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * tests for {@link CipherFactory}.
 */
public class CipherFactoryTest {
    /**
     * creating a bad cipher should fail.
     */
    @Test(expected = UnknownAlgorithmException.class)
    public void testBadCipher() {
	CipherFactory.create("bad");
    }

    /**
     * check that unknown algorithms can still get providers.
     */
    @Test
    public void testBadProvider() {
	CipherProvider got = CipherFactory.getProvider("bad");

	assertEquals("bad", got.getAlgorithm());
    }

    /**
     * check that unknown algorithms do not get implementations.
     */
    @Test(expected = UnknownAlgorithmException.class)
    public void testBadProvider2() {
	CipherProvider got = CipherFactory.getProvider("bad");

	got.get();
    }
}
