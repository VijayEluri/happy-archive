package org.yi.happy.archive;

import java.io.IOException;
import java.util.List;

import org.yi.happy.archive.test_data.TestData;

/**
 * a not ready handler that loads a block at a time according to a script. if
 * the script contains nulls then no block is loaded in that round.
 */
public class ScriptNotReadyHandler implements NotReadyHandler {

    /**
     * where to load the blocks into
     */
    private final BlockStore store;

    /**
     * the script to follow
     */
    private final List<TestData> script;

    /**
     * configure
     * 
     * @param store
     *            where to load blocs into
     * @param script
     *            the block to load in each round
     */
    public ScriptNotReadyHandler(BlockStore store, List<TestData> script) {
        this.store = store;
        this.script = script;
    }

    /**
     * load the next item from the script. if the item is null do not load a
     * block, if there are no blocks left then throw {@link IONotReadyException}
     */
    @Override
    public void notReady(SplitReader reader) throws IOException {
        if (script.size() == 0) {
            throw new IONotReadyException();
        }

        TestData item = script.remove(0);

        if (item != null) {
            store.put(item.getEncodedBlock());
        }
    }

}
