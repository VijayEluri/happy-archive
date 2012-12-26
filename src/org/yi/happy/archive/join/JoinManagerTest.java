package org.yi.happy.archive.join;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.yi.happy.archive.Bytes;
import org.yi.happy.archive.Fragment;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.test_data.TestData;

/**
 * Tests for {@link JoinManager}.
 */
public class JoinManagerTest {
    /**
     * The first basic operations, load a single data block.
     * 
     * @throws Exception
     */
    @Test
    public void testFirstBasic() throws Exception {
        JoinManager join = new JoinManager();

        LogFragmentHandler log = new LogFragmentHandler();
        join.addItem(TestData.KEY_CONTENT.getFullKey(), log);

        assertEquals(Arrays.asList(TestData.KEY_CONTENT.getFullKey()),
                join.getNeededNow());

        join.addBlocks(new MapBuilder<FullKey, Block>().add(
                TestData.KEY_CONTENT.getFullKey(),
                TestData.CLEAR_CONTENT.getBlock()).create());

        assertEquals(Arrays.asList(new Fragment(0, new Bytes("hello\n"))),
                log.getFragments());
    }
}
