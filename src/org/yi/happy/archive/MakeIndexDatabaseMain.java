package org.yi.happy.archive;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.yi.happy.annotate.GlobalFilesystem;
import org.yi.happy.annotate.GlobalOutput;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.file_system.FileSystemFile;

/**
 * Experimental command to populate a HSQL database with indexes.
 */
@GlobalFilesystem
@GlobalOutput
public class MakeIndexDatabaseMain implements MainCommand {
    /**
     * make a database from all the indexes in /Users/happy/archive.d/. this
     * takes a very long time to run because of updating indexes so I think
     * another strategy should be tried.
     * 
     * @param env
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws IOException
     */
    @Override
    public void run() throws ClassNotFoundException, SQLException, IOException {
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

        FileSystem fs = new FileSystemFile();
        String path = "/Users/happy/archive.d/index";
        IndexStore index = new IndexStoreFileSystem(fs, path);
        for (String volumeSet : index.listVolumeSets()) {
            for (String volumeName : index.listVolumeNames(volumeSet)) {
                process(index, conn, volumeSet, volumeName);
            }
        }

        st.execute("shutdown");
        conn.close();
    }

    private static void process(IndexStore index, Connection conn,
            String volumeSet, String volumeName) throws IOException,
            SQLException {
        PreparedStatement st = conn.prepareStatement("insert into index_line("
                + "volume_set, volume_name," + " file_name, key)"
                + " values (?, ?, ?, ?)");

        Reader in0 = index.open(volumeSet, volumeName);
        try {
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
