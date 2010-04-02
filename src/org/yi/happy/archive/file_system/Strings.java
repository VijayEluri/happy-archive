package org.yi.happy.archive.file_system;

/**
 * Operations on {@link String}s.
 */
public class Strings {
    /**
     * Test if a string has content. A string has content if it is not null and
     * not zero length.
     * 
     * @param s
     *            the string to test.
     * @return true if the string has content.
     */
    public static boolean hasContent(String s) {
	if (s == null) {
	    return false;
	}
	if (s.length() == 0) {
	    return false;
	}
	return true;
    }
}
