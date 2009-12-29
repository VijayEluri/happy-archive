package org.yi.happy.archive;

import org.yi.happy.archive.test_data.TestData;

/**
 * utility methods for the test cases
 */
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
	public static Block loadBlock(TestData name) throws LoadException {
        return BlockParse.load(name.getUrl());
    }

    /**
     * load the clear content block resource
     * 
     * @return the clear content block
     */
    public static Block loadClear() {
		return loadBlock(TestData.CLEAR_CONTENT);
    }
}
