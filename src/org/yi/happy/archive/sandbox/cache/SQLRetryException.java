package org.yi.happy.archive.sandbox.cache;

import java.sql.SQLException;

public class SQLRetryException extends SQLException {

    /**
     * 
     */
    private static final long serialVersionUID = 4700710006728695841L;

    public SQLRetryException(SQLException cause) {
        setNextException(cause);
    }

    @Override
    public SQLException getCause() {
        return (SQLException) super.getNextException();
    }
}
