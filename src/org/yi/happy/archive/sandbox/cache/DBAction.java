package org.yi.happy.archive.sandbox.cache;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * a command that runs on a connection.
 * 
 * @author sarah dot a dot happy at gmail dot com
 */
public interface DBAction {

    /**
     * run this command.
     * 
     * @param conn
     *            the connection to run on.
     * @throws SQLException
     *             on error.
     */
    void run(Connection conn) throws SQLException;
}
