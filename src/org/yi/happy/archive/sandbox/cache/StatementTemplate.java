package org.yi.happy.archive.sandbox.cache;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * template for a basic statement.
 * 
 * @author sarah dot a dot happy at gmail dot com
 */
public abstract class StatementTemplate implements DBAction {
    public final void run(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        try {
            run(stmt);
        } catch (SQLException e) {
            if (retryOnDuplicate() && SqlUtil.isDuplicateKeyError(e)) {
                throw new SQLRetryException(e);
            }
            throw e;
        } finally {
            stmt.close();
        }
    }

    protected boolean retryOnDuplicate() {
        return false;
    }

    /**
     * work with the statement.
     * 
     * @param stmt
     *            the statement, it will be closed when this method returns.
     * @throws SQLException
     *             on error.
     */
    protected abstract void run(Statement stmt) throws SQLException;
}
