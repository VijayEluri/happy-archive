package org.yi.happy.archive;

import org.yi.happy.archive.test_data.TestData;

/**
 * utility methods for the test cases
 * 
 * @deprecated use the methods on the test data instead.
 */
@Deprecated
public class TestUtil {
    /**
     * load a block resource.
     * 
     * @param name
     *            the resource
     * @return the loaded block
     * @throws LoadException
     *             on loading errors
     */
    @Deprecated
    public static Block loadBlock(TestData name) throws LoadException {
	return name.getBlock();
    }

    /**
     * load the clear content block resource
     * 
     * @return the clear content block
     */
    @Deprecated
    public static Block loadClear() {
	return TestData.CLEAR_CONTENT.getBlock();
    }
}
