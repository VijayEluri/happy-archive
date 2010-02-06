package org.yi.happy.archive.crypto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.security.MessageDigest;

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
	MessageDigest d = DigestFactory.create("sha-256");
	Assert.assertEquals("sha-256", d.getAlgorithm());
	Assert.assertEquals(32, d.getDigestLength());
    }

    /**
     * get sha-512
     */
    @Test
    public void testSha512() {
	MessageDigest d = DigestFactory.create("sha-512");
	Assert.assertEquals("sha-512", d.getAlgorithm());
	Assert.assertEquals(64, d.getDigestLength());
    }

    /**
     * get md5
     */
    @Test
    public void testMd5() {
	MessageDigest d = DigestFactory.create("md5");
	Assert.assertEquals("md5", d.getAlgorithm());
	Assert.assertEquals(16, d.getDigestLength());
    }

    /**
     * get sha1
     */
    @Test
    public void testSha1() {
	MessageDigest d = DigestFactory.create("sha1");
	Assert.assertEquals("sha1", d.getAlgorithm());
	Assert.assertEquals(20, d.getDigestLength());
    }

    /**
     * get sha1
     */
    @Test
    public void testSha1a() {
	MessageDigest d = DigestFactory.create("Sha1");
	Assert.assertEquals("Sha1", d.getAlgorithm());
	Assert.assertEquals(20, d.getDigestLength());
    }

    /**
     * get bad
     */
    @Test(expected = UnknownAlgorithmException.class)
    public void testBad() {
	DigestFactory.create("bad");
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
