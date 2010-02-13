package org.yi.happy.archive;

import java.io.IOException;

public class DecodeException extends IOException {

    /**
     * 
     */
    private static final long serialVersionUID = 786463010291786940L;

    public DecodeException() {
    }

    public DecodeException(String message, Throwable cause) {
	super(message, cause);
    }

    public DecodeException(String message) {
	super(message);
    }

    public DecodeException(Throwable cause) {
	super(cause);
    }

}
