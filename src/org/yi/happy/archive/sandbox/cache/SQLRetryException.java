package org.yi.happy.archive.sandbox.cache;

import java.sql.SQLException;

/**
 * A {@link SQLException} where a retry is a good idea.
 */
public class SQLRetryException extends SQLException {

    /**
     * 
     */
    private static final long serialVersionUID = 4700710006728695841L;

    /**
     * create with a cause.
     * 
     * @param cause
     *            the cause.
     */
    public SQLRetryException(SQLException cause) {
        setNextException(cause);
    }

    @Override
    public SQLException getCause() {
        return super.getNextException();
    }
}
