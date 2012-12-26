package org.yi.happy.archive.restore;

import java.util.ArrayDeque;
import java.util.Deque;

import org.yi.happy.archive.Bytes;
import org.yi.happy.archive.Fragment;

public class CaptureFragmentHandler implements FragmentHandler {
    private Deque<Fragment> fragments = new ArrayDeque<Fragment>();
    private boolean done = false;

    @Override
    public void data(long offset, Bytes data) {
        fragments.add(new Fragment(offset, data));
    }

    @Override
    public void end() {
        done = true;
    }

    public boolean isReady() {
        return fragments.isEmpty() == false;
    }

    public boolean isDone() {
        return done && fragments.isEmpty();
    }

    public Fragment get() {
        return fragments.removeFirst();
    }
}
