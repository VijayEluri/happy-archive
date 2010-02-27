package org.yi.happy.archive.file_system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A value object that represents a file path. ./a/b/c or /a/b/c
 */
public final class Path {
    private final List<String> elements;
    private final boolean absolute;

    private Path(boolean absolute, List<String> elements) {
        this.absolute = absolute;
        this.elements = Collections.unmodifiableList(new ArrayList<String>(
        	elements));
    }

    private Path(boolean absolute) {
	this.absolute = absolute;
	this.elements = Collections.emptyList();
    }

    /**
     * get a path value representing the given child.
     * 
     * @param name
     *            the child name.
     * @return a path for that child.
     */
    public Path child(String name) {
	ArrayList<String> elements = new ArrayList<String>(this.elements);
	elements.add(name);
	return new Path(absolute, elements);
    }

    /**
     * get the name of the deepest path component.
     * 
     * @return the name of the current path component.
     */
    public String getName() {
	if (elements.isEmpty()) {
	    return absolute ? "/" : ".";
	}
	return elements.get(elements.size() - 1);
    }

    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder();
	String join = "";
	if (absolute) {
	    join = "/";
	}
	for (String i : elements) {
	    sb.append(join).append(i);
	    join = "/";
	}
	return sb.toString();
    }

    /**
     * get the length of the path.
     * 
     * @return the length of the path.
     */
    public int size() {
	return elements.size();
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (absolute ? 1231 : 1237);
	result = prime * result
		+ ((elements == null) ? 0 : elements.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Path other = (Path) obj;
	if (absolute != other.absolute)
	    return false;
	if (elements == null) {
	    if (other.elements != null)
		return false;
	} else if (!elements.equals(other.elements))
	    return false;
	return true;
    }

    /**
     * Get the base path.
     * 
     * @return the base path.
     */
    public Path getBase() {
	if (elements.isEmpty()) {
	    return this;
	}

	List<String> elements = new ArrayList<String>(this.elements);
	elements.remove(elements.size() - 1);
	return new Path(absolute, elements);
    }

    /**
     * Get the parent path for this path.
     * 
     * @return the parent path.
     */
    public Path parent() {
	if (elements.isEmpty() && absolute) {
	    return this;
	}

	if (elements.isEmpty() && !absolute) {
	    return child("..");
	}

	if (getName().equals("..")) {
	    return child("..");
	}

	return getBase();
    }

    /**
     * The start of an absolute path.
     */
    public static final Path ABSOLUTE = new Path(true);
    /**
     * The base value for a relative path.
     */
    public static final Path RELATIVE = new Path(false);
}
