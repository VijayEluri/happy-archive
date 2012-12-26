package org.yi.happy.archive.join;

import java.util.ArrayList;
import java.util.List;

import org.yi.happy.archive.Bytes;
import org.yi.happy.archive.Fragment;

public class LogFragmentHandler implements FragmentHandler {
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private boolean done = false;

    @Override
    public void data(long offset, Bytes data) {
        fragments.add(new Fragment(offset, data));
    }

    @Override
    public void end() {
        done = true;
    }

    public List<Fragment> getFragments() {
        return fragments;
    }

    public boolean isDone() {
        return done;
    }
}
