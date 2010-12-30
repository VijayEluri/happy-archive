package org.yi.happy.archive.sandbox.cache;

import java.sql.SQLException;

public class SqlUtil {

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
