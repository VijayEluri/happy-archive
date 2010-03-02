package org.yi.happy.archive.file_system;

/**
 * A value object that represents a file path. ./a/b/c or /a/b/c
 */
public final class Path {
    private final Path base;
    private final String name;
    private final boolean absolute;

    private Path(Path base, String name) {
	this.absolute = base.absolute;
	this.base = base;
	this.name = name;
    }

    private Path(boolean absolute, String name) {
	this.absolute = absolute;
	this.base = null;
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
	if (this.name == null) {
	    return new Path(absolute, name);
	}
	return new Path(this, name);
    }

    /**
     * get the name of the deepest path component.
     * 
     * @return the name of the current path component.
     */
    public String getName() {
	if (name == null) {
	    return absolute ? "/" : ".";
	}
	return name;
    }

    @Override
    public String toString() {
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

	if (name != null) {
	    sb.append(name);
	}

	return;
    }

    /**
     * get the length of the path.
     * 
     * @return the length of the path.
     */
    public int size() {
	if (base == null && name == null) {
	    return 0;
	}
	if (base == null) {
	    return 1;
	}
	return base.size() + 1;
    }

    /**
     * Get the base path.
     * 
     * @return the base path.
     */
    public Path getBase() {
	if (base == null && name == null) {
	    return this;
	}

	if (base == null && name != null) {
	    return new Path(absolute);
	}

	return base;
    }

    /**
     * Get the parent path for this path.
     * 
     * @return the parent path.
     */
    public Path parent() {
	if (name == null && absolute) {
	    return this;
	}

	if (name == null && !absolute) {
	    return child("..");
	}

	if (getName().equals("..")) {
	    return child("..");
	}

	return getBase();
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (absolute ? 1231 : 1237);
	result = prime * result + ((base == null) ? 0 : base.hashCode());
	result = prime * result + ((name == null) ? 0 : name.hashCode());
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
	if (base == null) {
	    if (other.base != null)
		return false;
	} else if (!base.equals(other.base))
	    return false;
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	return true;
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
