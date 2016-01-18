package org.yi.happy.archive.gui;

import java.awt.Container;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import org.yi.happy.archive.Main;
import org.yi.happy.archive.MainCommand;
import org.yi.happy.archive.gui.RestoreSummary.RestoreSummaryLine;
import org.yi.happy.archive.restore.RestoreEngine;

/**
 * A graphical interface for doing a restore.
 */
public class RestoreGuiMain implements MainCommand {
    /**
     * Entry point.
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Main.main(new String[] { "restore-gui" });
    }

    /**
     * Inject.
     */
    public RestoreGuiMain() {
    }

    @Override
    public void run() throws Exception {
        SwingUtilities.invokeLater(start);
    }

    private Runnable start = new Runnable() {
        @Override
        public void run() {
            restoreSummary = RestoreSummary.create(restore);

            JFrame window = new JFrame("Restore");

            JPanel l = new JPanel();
            l.setLayout(new BoxLayout(l, BoxLayout.PAGE_AXIS));
            l.add(new JLabel("Restore Manager"));
            JPanel b = new JPanel();
            b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS));
            b.add(new JButton("add"));
            b.add(new JButton("step"));
            b.add(Box.createHorizontalGlue());
            l.add(b);
            JTable t = new JTable(restoreSummaryTable);
            l.add(t.getTableHeader());
            l.add(t);
            l.add(new JLabel("(# files; # blocks)"));

            JPanel r = new JPanel();
            r.setLayout(new BoxLayout(r, BoxLayout.PAGE_AXIS));
            r.add(new JLabel("Volumes Needed"));
            b = new JPanel();
            b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS));
            b.add(new JButton("fetch"));
            b.add(new JButton("update"));
            b.add(Box.createHorizontalGlue());
            r.add(b);
            r.add(new JScrollPane(new JTable()));
            r.add(new JLabel("(# volumes; # blocks)"));

            Container c = window.getContentPane();
            JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, l, r);
            c.add(split);

            window.setSize(800, 600);
            window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            window.setVisible(true);
        }
    };

    private RestoreEngine restore = new RestoreEngine();

    private RestoreSummary restoreSummary;

    private TableModel restoreSummaryTable = new AbstractTableModel() {
        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public int getRowCount() {
            return restoreSummary.getLines().size();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            RestoreSummaryLine line = restoreSummary.getLines().get(rowIndex);
            switch (columnIndex) {
            case 0:
                return line.name;
            case 1:
                return line.size;
            }
            return "";
        }

        public String getColumnName(int column) {
            switch (column) {
            case 0:
                return "name";
            case 1:
                return "size";
            }
            return "";
        }
    };
}
