package org.yi.happy.archive.key;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.yi.happy.archive.test_data.TestData;

public class NameFullKeyTest {
    /**
     * convert a full name key to a locator key
     */
    @Test
    public void testToLocatorKey2() {
	Key key = TestData.KEY_NAME.getFullKey();
	assertTrue(key instanceof NameFullKey);

	LocatorKey have = key.toLocatorKey();
	LocatorKey want = TestData.KEY_NAME.getLocatorKey();
	assertEquals(want, have);
    }
}
