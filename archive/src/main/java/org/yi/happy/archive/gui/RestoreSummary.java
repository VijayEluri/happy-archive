package org.yi.happy.archive.gui;

import java.util.ArrayList;
import java.util.List;

import org.yi.happy.archive.restore.RestoreEngine;

public class RestoreSummary {
    public static class RestoreSummaryLine {
        public final String name;
        public final int size;

        public RestoreSummaryLine(String name, int size) {
            this.name = name;
            this.size = size;
        }
    }

    private List<RestoreSummaryLine> lines;

    public RestoreSummary(List<RestoreSummaryLine> lines) {
        this.lines = lines;
    }

    public static RestoreSummary create(RestoreEngine restore) {
        List<RestoreSummaryLine> lines = new ArrayList<RestoreSummaryLine>();
        restore.start();
        while (restore.findReady()) {
            lines.add(new RestoreSummaryLine(restore.getJobName(), restore
                    .getJobSize()));
            restore.skipJob();
        }
        return new RestoreSummary(lines);
    }

    public List<RestoreSummaryLine> getLines() {
        return lines;
    }
}
