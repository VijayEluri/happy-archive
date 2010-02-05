package org.yi.happy.archive;

import org.junit.Test;


public class CipherFactoryTest {
    /**
     * setting a bad cipher should fail right away
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBadCipher() {
	CipherFactory.create("bad");
    }
}
