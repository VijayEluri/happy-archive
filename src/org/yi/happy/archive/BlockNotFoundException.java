package org.yi.happy.archive;

/**
 * thrown when a block is not found. This exception is likely caused by forces
 * outside of this program and not programmer error, therefore it should be
 * checked.
 * 
 * @author sarah dot a dot happy at gmail dot com
 */
public class BlockNotFoundException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1792897636663264550L;

    public BlockNotFoundException() {
        super();
    }

    public BlockNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlockNotFoundException(String message) {
        super(message);
    }

    public BlockNotFoundException(Throwable cause) {
        super(cause);
    }

}
