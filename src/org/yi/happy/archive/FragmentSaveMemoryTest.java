package org.yi.happy.archive;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

/**
 * Tests for {@link FragmentSaveMemory}.
 */
public class FragmentSaveMemoryTest {
    private static final Bytes D1 = new Bytes("01234");
    private static final Bytes D2 = new Bytes("56789");

    private static final String N1 = "1";
    private static final String N2 = "2";

    /**
     * Typical usage.
     * 
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        FragmentSaveMemory data = new FragmentSaveMemory();
        FragmentSave target = data;

        target.save(N1, new Fragment(0, D1));
        target.save(N2, new Fragment(5, D2));
        target.close();

        assertArrayEquals(D1.toByteArray(), data.get(N1));
        assertArrayEquals(new BytesBuilder(0, 0, 0, 0, 0).add(D2)
                .createByteArray(), data.get(N2));

        target.save(N1, new Fragment(5, D2));
        target.save(N2, new Fragment(0, D1));
        target.close();

        assertArrayEquals(new BytesBuilder(D1).add(D2).createByteArray(),
                data.get(N1));
        assertArrayEquals(new BytesBuilder(D1).add(D2).createByteArray(),
                data.get(N2));

        target.close();
    }
}
