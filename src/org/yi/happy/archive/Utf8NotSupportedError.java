package org.yi.happy.archive;

/**
 * An error for when the UTF-8 encoding is not available. This is an error
 * because this encoding should always be available.
 */
public class Utf8NotSupportedError extends Error {

    /**
     * 
     */
    private static final long serialVersionUID = 4912312557295705312L;

    public Utf8NotSupportedError() {
	super();
    }

    public Utf8NotSupportedError(String message, Throwable cause) {
	super(message, cause);
    }

    public Utf8NotSupportedError(String message) {
	super(message);
    }

    public Utf8NotSupportedError(Throwable cause) {
	super(cause);
    }
}
