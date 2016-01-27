package org.yi.happy.archive.restore;

import java.util.ArrayList;
import java.util.List;

public class RestoreState {
    private List<RestoreStateJob> jobs = new ArrayList<>();

    public List<RestoreStateJob> getJobs() {
        return jobs;
    }

    public void setJobs(List<RestoreStateJob> jobs) {
        this.jobs = jobs;
    }
}
