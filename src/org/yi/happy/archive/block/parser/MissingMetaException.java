package org.yi.happy.archive.block.parser;


public class MissingMetaException extends IllegalArgumentException {

    /**
     * 
     */
    private static final long serialVersionUID = -6231320154182629358L;

    public MissingMetaException() {
	super();
    }

    public MissingMetaException(String message, Throwable cause) {
	super(message, cause);
    }

    public MissingMetaException(String message) {
	super(message);
    }

    public MissingMetaException(Throwable cause) {
	super(cause);
    }

}
