package org.yi.happy.archive.join;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.key.FullKey;

public class JoinManager {
    private List<RestoreEngine> items;

    public JoinManager() {
        items = new ArrayList<RestoreEngine>();
    }

    public void addItem(FullKey key, FragmentHandler handler) {
        items.add(new RestoreEngine(key, handler));
    }

    public List<FullKey> getNeededNow() {
        Set<FullKey> needed = new LinkedHashSet<FullKey>();
        for (RestoreEngine item : items) {
            needed.addAll(item.getNeededNow());
        }
        return new ArrayList<FullKey>(needed);
    }

    public void addBlocks(Map<FullKey, Block> blocks) {
        for (RestoreEngine item : items) {
            item.addBlocks(blocks);
        }
    }

}
