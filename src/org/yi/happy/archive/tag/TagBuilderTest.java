package org.yi.happy.archive.tag;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for {@link TagBuilder}.
 */
public class TagBuilderTest {
    /**
     * Make an empty map.
     */
    @Test
    public void testEmptyMap() {
        Map<String, String> want = new HashMap<String, String>();

        Map<String, String> have = new TagBuilder().createMap();

        assertEquals(want, have);
    }

    /**
     * Make a one element map.
     */
    @Test
    public void testOneMap() {
        Map<String, String> want = new HashMap<String, String>();
        want.put("a", "b");

        Map<String, String> have = new TagBuilder().add("a", "b").createMap();

        assertEquals(want, have);
    }

    /**
     * Make a two element map.
     */
    @Test
    public void testTwoMap() {
        Map<String, String> want = new HashMap<String, String>();
        want.put("a", "b");
        want.put("b", "c");

        Map<String, String> have = new TagBuilder().add("a", "b").add("b", "c")
                .createMap();

        assertEquals(want, have);
    }

    /**
     * When building a map.
     * 
     * If an element is repeated in the map.
     * 
     * Then the most recent version of the element is put in the map.
     */
    @Test
    public void testRepeatMap() {

        Map<String, String> have = new TagBuilder().add("a", "b").add("b", "c")
                .add("a", "d").createMap();

        Map<String, String> want = new HashMap<String, String>();
        want.put("a", "b");
        want.put("b", "c");

        assertEquals(want, have);
    }

    /**
     * When a tag is made, it has the contents wanted.
     */
    @Test
    public void testMakeTag() {
        Tag have = new TagBuilder().add("a", "d").add("b", "c").create();

        Map<String, String> wantMap = new HashMap<String, String>();
        wantMap.put("a", "d");
        wantMap.put("b", "c");
        Tag want = new Tag(wantMap);

        assertEquals(want, have);
    }

    /**
     * When building a map.
     * 
     * If an element is repeated in the map.
     * 
     * Then the most recent version of the element is put in the map.
     */
    @Test
    public void testRepeatChange() {

        Map<String, String> have = new TagBuilder().put("a", "b").put("b", "c")
                .put("a", "d").createMap();

        Map<String, String> want = new HashMap<String, String>();
        want.put("a", "d");
        want.put("b", "c");

        assertEquals(want, have);
    }

    /**
     * the field name can not have an equal sign in it (this may change in the
     * future with escaping).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNoEqualInFieldName() {
        new TagBuilder().add("a=b", "c");
    }

    /**
     * the field name can not have an equal sign in it (this may change in the
     * future with escaping).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNoEqualInFieldName2() {
        new TagBuilder().put("a=b", "c");
    }
}
