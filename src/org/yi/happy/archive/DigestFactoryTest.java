package org.yi.happy.archive;

import java.security.MessageDigest;

import org.junit.Assert;
import org.junit.Test;
import org.yi.happy.archive.key.UnknownAlgorithmException;

/**
 * check functioning of {@link DigestFactory}
 * 
 * @author sarah dot a dot happy at gmail dot com
 * 
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

}
