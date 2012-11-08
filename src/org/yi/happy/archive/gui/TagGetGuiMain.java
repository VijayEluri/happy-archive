package org.yi.happy.archive.gui;

import java.awt.Container;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.yi.happy.annotate.SmellsMessy;
import org.yi.happy.archive.tag.Tag;
import org.yi.happy.archive.tag.TagStreamIterator;

@SmellsMessy
public class TagGetGuiMain implements Runnable {

    private JScrollPane comp;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new TagGetGuiMain());
    }

    @Override
    public void run() {
        /*
         * TODO show the basic GUI on the screen, from the notebook.
         */
        JFrame frame = new JFrame("Tag Get");
        Container c = frame.getContentPane();
        comp = new JScrollPane(new JLabel("loading..."));
        c.add(comp);

        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                load();
            }
        }).run();

    }

    @SmellsMessy
    public void load() {
        try {
            ArrayList<Tag> data = new ArrayList<Tag>();

            InputStream in = new FileInputStream(
                    "/Users/happy/tmp/files.feather");
            try {
                TagStreamIterator it = new TagStreamIterator(in);
                for (Tag i : it) {
                    data.add(i);
                }
            } finally {
                in.close();
            }

            SwingUtilities.invokeLater(new LoadedFile(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class LoadedFile implements Runnable {

        private final ArrayList<Tag> data;

        public LoadedFile(ArrayList<Tag> data) {
            this.data = data;
        }

        @Override
        @SmellsMessy
        public void run() {
            String[][] d = new String[data.size()][];
            for (int i = 0; i < d.length; i++) {
                d[i] = new String[3];
                d[i][0] = data.get(i).get("name");
                d[i][1] = data.get(i).get("type");
                d[i][2] = data.get(i).get("data");
            }
            String[] c = new String[3];
            c[0] = "name";
            c[1] = "type";
            c[2] = "data";

            JTable view = new JTable(d, c);
            view.setShowGrid(true);
            view.sizeColumnsToFit(-1);
            comp.setViewportView(view);
        }
    }
}
