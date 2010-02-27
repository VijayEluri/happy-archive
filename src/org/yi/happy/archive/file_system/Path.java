package org.yi.happy.archive.file_system;


/**
 * A value object that represents a file path. ./a/b/c or /a/b/c
 */
public class Path {
    private final Path base;

    private final String name;

    private final boolean absolute;

    private Path(Path base, String name) {
	this.absolute = base.absolute;
	this.base = base;
	this.name = name;
    }

    private Path(boolean absolute) {
	this.absolute = absolute;
	this.base = null;
	this.name = null;
    }

    /**
     * get a path value representing the given child.
     * 
     * @param name
     *            the child name.
     * @return a path for that child.
     */
    public Path child(String name) {
	return new Path(this, name);
    }

    /**
     * get the name of the deepest path component.
     * 
     * @return the name of the current path component.
     */
    public String getName() {
	if (base == null) {
	    return absolute ? "/" : ".";
	}
	return name;
    }

    @Override
    public String toString() {
	if (base == null) {
	    return absolute ? "/" : ".";
	}
	StringBuilder sb = new StringBuilder();
	toString(sb);
	return sb.toString();
    }

    private void toString(StringBuilder sb) {
	/*
	 * recursive step
	 */
	if (base != null) {
	    base.toString(sb);
	    sb.append("/").append(name);
	    return;
	}

	/*
	 * base step
	 */
	if (absolute) {
	    sb.append("/");
	}
	return;
    }

    /**
     * get the length of the path.
     * 
     * @return the length of the path.
     */
    public int size() {
	if (base == null) {
	    return 0;
	}
	return base.size() + 1;
    }

    /**
     * Get the base path.
     * 
     * @return the base path.
     */
    public Path getBase() {
	if (base == null) {
	    return this;
	}

	return base;
    }

    /**
     * Get the parent path for this path.
     * 
     * @return the parent path.
     */
    public Path parent() {
	if (base == null && absolute) {
	    return this;
	}

	if (base == null && !absolute) {
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
