package org.yi.happy.archive.restore;

import java.util.ArrayList;
import java.util.List;

public class RestoreStateJob {
    private String name;
    private List<RestoreStateJobPart> parts = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RestoreStateJobPart> getParts() {
        return parts;
    }

    public void setParts(List<RestoreStateJobPart> parts) {
        this.parts = parts;
    }
}
