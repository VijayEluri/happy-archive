package org.yi.happy.archive.crypto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Assert;
import org.junit.Test;

/**
 * check functioning of {@link DigestFactory}
 */
public class DigestFactoryTest {
    /**
     * get sha-256
     */
    @Test
    public void testSha256() {
	DigestProvider d = DigestFactory.getProvider("sha-256");
	Assert.assertEquals("sha-256", d.getAlgorithm());
	Assert.assertEquals(32, d.get().getDigestLength());
    }

    /**
     * get sha-512
     */
    @Test
    public void testSha512() {
	DigestProvider d = DigestFactory.getProvider("sha-512");
	Assert.assertEquals("sha-512", d.getAlgorithm());
	Assert.assertEquals(64, d.get().getDigestLength());
    }

    /**
     * get md5
     */
    @Test
    public void testMd5() {
	DigestProvider d = DigestFactory.getProvider("md5");
	Assert.assertEquals("md5", d.getAlgorithm());
	Assert.assertEquals(16, d.get().getDigestLength());
    }

    /**
     * get sha1
     */
    @Test
    public void testSha1() {
	DigestProvider d = DigestFactory.getProvider("sha1");
	Assert.assertEquals("sha1", d.getAlgorithm());
	Assert.assertEquals(20, d.get().getDigestLength());
    }

    /**
     * get sha1
     */
    @Test
    public void testSha1a() {
	DigestProvider d = DigestFactory.getProvider("Sha1");
	Assert.assertEquals("Sha1", d.getAlgorithm());
	Assert.assertEquals(20, d.get().getDigestLength());
    }

    /**
     * provide bad
     */
    @Test
    public void testProvideBad1() {
	DigestProvider got = DigestFactory.getProvider("bad");

	assertNotNull(got);
    }

    /**
     * provide bad
     */
    @Test(expected = UnknownAlgorithmException.class)
    public void testProvideBad2() {
	DigestProvider got = DigestFactory.getProvider("bad");

	got.get();
    }

    /**
     * provide bad
     */
    @Test
    public void testProvideGood() {
	DigestProvider got = DigestFactory.getProvider("sha1");

	assertNotNull(got);
	assertEquals("sha1", got.getAlgorithm());

	assertNotNull(got.get());
    }
}
