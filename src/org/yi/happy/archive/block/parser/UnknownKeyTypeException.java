package org.yi.happy.archive.block.parser;

import org.yi.happy.archive.VerifyException;

public class UnknownKeyTypeException extends VerifyException {

    /**
     * 
     */
    private static final long serialVersionUID = -904570696945022977L;

    public UnknownKeyTypeException() {
	super();
    }

    public UnknownKeyTypeException(String message, Throwable cause) {
	super(message, cause);
    }

    public UnknownKeyTypeException(String message) {
	super(message);
    }

    public UnknownKeyTypeException(Throwable cause) {
	super(cause);
    }

}
