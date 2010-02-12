package org.yi.happy.archive;

public class VersionNotNumberException extends IllegalArgumentException {
    /**
	 * 
	 */
    private static final long serialVersionUID = 2402922998376793056L;

    public VersionNotNumberException() {
	super();
    }

    public VersionNotNumberException(String message, Throwable cause) {
	super(message, cause);
    }

    public VersionNotNumberException(String message) {
	super(message);
    }

    public VersionNotNumberException(Throwable cause) {
	super(cause);
    }

}
