package org.yi.happy.archive.join;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.key.FullKey;

public class JoinTodo {

    private List<FullKey> todo;
    private FragmentHandler handler;

    public JoinTodo(FullKey key, FragmentHandler handler) {
        todo = new ArrayList<FullKey>();
        todo.add(key);
        this.handler = handler;
    }

    public Set<FullKey> getNeededNow() {
        LinkedHashSet<FullKey> needed = new LinkedHashSet<FullKey>();
        for (FullKey item : todo) {
            needed.add(item);
        }
        return needed;
    }

    public void addBlocks(Map<FullKey, Block> blocks) {
        Block block = blocks.get(todo.get(0));

        if (block == null) {
            return;
        }

        handler.data(0, block.getBody());

        todo.remove(0);
        handler.end();
    }
}
