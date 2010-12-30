package org.yi.happy.archive.sandbox.cache;

import java.sql.SQLException;

/**
 * SQL Functions that don't fit.
 */
public class SqlUtil {

    /**
     * check if an exception is a duplicate key error.
     * 
     * @param e
     *            the exception to check.
     * @return true if the exception is a duplicate key error
     */
    public static boolean isDuplicateKeyError(SQLException e) {
        for (; e != null; e = e.getNextException()) {
            String state = e.getSQLState();
    
            if (state == null) {
                continue;
            }
    
            if (state.equals("23505")) {
                return true;
            }
        }
    
        return false;
    }

}
