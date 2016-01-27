package org.yi.happy.archive.restore;

public class RestoreStateJobPart {
    private long offset;
    private String key;

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
