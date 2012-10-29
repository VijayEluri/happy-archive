package org.yi.happy.archive.tag;

/**
 * Accept tags.
 */
public interface TagVisitor {
    /**
     * Accept a tag.
     * 
     * @param tag
     *            the tag.
     */
    public void accept(Tag tag);
}
