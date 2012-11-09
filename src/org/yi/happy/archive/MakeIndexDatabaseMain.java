package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.zip.GZIPInputStream;

import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.file_system.RealFileSystem;

/**
 * Experimental command to populate a HSQL database with indexes.
 */
public class MakeIndexDatabaseMain {
    /**
     * make a database from all the indexes in /Users/happy/archive.d/. this
     * takes a very long time to run because of updating indexes so I think
     * another strategy should be tried.
     * 
     * @param args
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws IOException
     */
    public static void launch(Env env) throws ClassNotFoundException,
            SQLException, IOException {
        /*
         * XXX hacked together, needs to be cleaned
         */

        Class.forName(org.hsqldb.jdbcDriver.class.getName());

        final Connection conn = DriverManager.getConnection(
                "jdbc:hsqldb:/Users/happy/archive.d/index-db", "sa", "");

        Statement st = conn.createStatement();

        st.execute("create cached table index_line ("
                + "volume_set varchar(64) not null, "
                + "volume_name varchar(64) not null, "
                + "file_name varchar(255) not null, "
                + "key varchar(255) not null)");

        st.execute("create index key_index on index_line(key)");

        conn.setAutoCommit(false);

        FileSystem fs = new RealFileSystem();
        String path = "/Users/happy/archive.d/index";

        new IndexFileTree(fs, path).accept(new IndexFileTree.Visitor() {
            @Override
            public void visit(FileSystem fs, String fileName, String volumeSet,
                    String volumeName) throws IOException {
                try {
                    process(fs, conn, fileName, volumeSet, volumeName);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        st.execute("shutdown");
        conn.close();
    }

    private static void process(FileSystem fs, Connection conn,
            String fileName, String volumeSet, String volumeName)
            throws IOException, SQLException {
        PreparedStatement st = conn.prepareStatement("insert into index_line("
                + "volume_set, volume_name," + " file_name, key)"
                + " values (?, ?, ?, ?)");

        InputStream in0 = fs.openInputStream(fileName);
        try {
            if (fileName.endsWith(".gz")) {
                in0 = new GZIPInputStream(in0);
            }
            LineCursor in = new LineCursor(in0);
            while (in.next()) {
                String[] line = in.get().split("\t", -1);

                if (!line[1].equals("plain")) {
                    continue;
                }

                // insert into database
                st.setString(1, volumeSet);
                st.setString(2, volumeName);
                st.setString(3, line[0]);
                st.setString(4, line[2]);
                st.execute();

                count++;
                if (count % 10000 == 0) {
                    System.out.println(System.currentTimeMillis() + ": "
                            + count);
                }
            }
        } finally {
            in0.close();
        }

        st.close();

        conn.commit();
    }

    /**
     * The number of records inserted so far.
     */
    public static volatile int count = 0;
}
