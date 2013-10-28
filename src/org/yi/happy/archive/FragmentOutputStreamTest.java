package org.yi.happy.archive;

import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayOutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link FragmentOutputStream}.
 */
public class FragmentOutputStreamTest {
    private ByteArrayOutputStream log;
    private FragmentOutputStream out;

    /**
     * setup.
     */
    @Before
    public void before() {
        log = new ByteArrayOutputStream();
        out = new FragmentOutputStream(log);
    }

    /**
     * tear down.
     */
    @After
    public void after() {
        log = null;
        out = null;
    }

    /**
     * write out one fragment.
     * 
     * @throws Exception
     */
    @Test
    public void testOne() throws Exception {
        Bytes D1 = new Bytes("01234");
        Bytes O = D1;

        out.write(0, D1);

        assertArrayEquals(O.toByteArray(), log.toByteArray());
    }

    /**
     * write out two consecutive fragments.
     * 
     * @throws Exception
     */
    @Test
    public void testTwo() throws Exception {
        Bytes D1 = new Bytes("01234");
        Bytes D2 = new Bytes("56789");
        Bytes O = new Bytes("0123456789");

        out.write(0, D1);
        out.write(5, D2);

        assertArrayEquals(O.toByteArray(), log.toByteArray());
    }

    /**
     * write out two fragments with a gap between.
     * 
     * @throws Exception
     */
    @Test
    public void testGap() throws Exception {
        Bytes D1 = new Bytes("01234");
        Bytes D2 = new Bytes("56789");
        Bytes O = new BytesBuilder(D1).add(0, 0, 0, 0, 0).add(D2).create();

        out.write(0, D1);
        out.write(10, D2);

        assertArrayEquals(O.toByteArray(), log.toByteArray());
    }

    /**
     * write out two fragments that overlap.
     * 
     * @throws Exception
     */
    @Test
    public void testOverlap() throws Exception {
        Bytes D1 = new Bytes("01234");
        Bytes D2 = new Bytes("56789");
        Bytes O = new Bytes("01234789");

        out.write(0, D1);
        out.write(3, D2);

        assertArrayEquals(O.toByteArray(), log.toByteArray());
    }
}
