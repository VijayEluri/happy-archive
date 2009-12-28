package org.yi.happy.archive;


public class VersionNotNumberException extends VerifyException {
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
