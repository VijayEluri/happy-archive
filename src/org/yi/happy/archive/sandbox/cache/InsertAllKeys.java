package org.yi.happy.archive.sandbox.cache;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.yi.happy.archive.LineCursor;

/**
 * An experiment where all the keys are loaded into a HSQLDB.
 */
public class InsertAllKeys {
    /**
     * create the key table and insert all the keys into it.
     * 
     * @param args
     * @throws Exception
     */
    public static void main1(String[] args) throws Exception {
        run(new DBAction() {
            @Override
            public void run(Connection conn) throws SQLException {
                new StatementTemplate() {
                    @Override
                    protected void run(Statement stmt) throws SQLException {
                        stmt.execute("create cached table key(id identity,"
                                + " key varchar(255) not null unique)");
                    }
                }.run(conn);

                PreparedStatement st = conn
                        .prepareStatement("insert into key(key) values(?)");

                int count = 0;
                long time = 0;

                try {
                    FileReader in = new FileReader("/Users/happy/tmp/keys.txt");
                    LineCursor l = new LineCursor(in);
                    while (l.next()) {
                        long now = System.currentTimeMillis();
                        if (now - time > 3000) {
                            time = now;
                            System.out.println(time + " " + count);
                        }

                        st.setString(1, l.get());
                        st.execute();
                        count++;

                    }
                    in.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private static void run(DBAction cmd) throws Exception {
        Class.forName(org.hsqldb.jdbcDriver.class.getName());
        Connection conn = DriverManager.getConnection(
                "jdbc:hsqldb:file:/Users/happy/tmp/testdb", "sa", "");
        try {
            cmd.run(conn);
        } finally {
            try {
                Statement st = conn.createStatement();
                st.execute("shutdown");
            } finally {
                conn.close();
            }
        }
    }

    /**
     * list the keys with ids
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        final PrintWriter out = new PrintWriter(new FileWriter(
                "/Users/happy/tmp/keys-2.txt"));
        run(new DBAction() {
            @Override
            public void run(Connection conn) throws SQLException {
                PreparedStatement stmt = conn
                        .prepareStatement("select id, key from key order by key limit 1000");
                ResultSet rs = stmt.executeQuery();
                String key = null;
                boolean more = false;
                while (rs.next()) {
                    int id = rs.getInt(1);
                    key = rs.getString(2);
                    more = true;

                    out.println(key + "\t" + id);
                }
                rs.close();

                stmt = conn
                        .prepareStatement("select id, key from key where key > ? order by key limit 1000");
                while (more) {
                    stmt.setString(1, key);
                    rs = stmt.executeQuery();
                    more = false;
                    while (rs.next()) {
                        int id = rs.getInt(1);
                        key = rs.getString(2);
                        more = true;

                        out.println(key + "\t" + id);
                    }
                    rs.close();
                }
            }
        });
        out.close();
    }
}
