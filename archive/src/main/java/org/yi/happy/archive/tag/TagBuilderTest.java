package org.yi.happy.archive.tag;

import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;
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
    public void testEmpty() {
        Tag have = new TagBuilder().create();

        Tag want = makeTag();
        assertEquals(want, have);
    }

    /**
     * Make a one element map.
     */
    @Test
    public void testOne() {
        Tag have = new TagBuilder().add("a", "b").create();

        Tag want = makeTag("a", "b");
        assertEquals(want, have);
    }

    /**
     * Make a two element map.
     */
    @Test
    public void testTwo() {
        Tag have = new TagBuilder().add("a", "b").add("b", "c").create();

        Tag want = makeTag("a", "b", "b", "c");
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
    public void testRepeat() {
        Tag have = new TagBuilder().add("a", "b").add("b", "c").add("a", "d")
                .create();

        Tag want = makeTag("a", "b", "b", "c");
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
        Tag have = new TagBuilder().put("a", "b").put("b", "c").put("a", "d")
                .create();

        Tag want = makeTag("a", "d", "b", "c");
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

    private static Tag makeTag(String... values) {
        if (values.length % 2 != 0) {
            throw new IllegalArgumentException();
        }

        Map<String, String> map = new LinkedHashMap<String, String>();
        for (int i = 0; i < values.length; i += 2) {
            map.put(values[i], values[i + 1]);
        }

        return new Tag(map);
    }
}
