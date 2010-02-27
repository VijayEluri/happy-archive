package org.yi.happy.archive.file_system;

/**
 * A value object that represents a file path. ./a/b/c or /a/b/c
 */
public abstract class Path {
    /**
     * get a path value representing the given child.
     * 
     * @param name
     *            the child name.
     * @return a path for that child.
     */
    public abstract Path child(String name);

    /**
     * get the parent of this path, the ".." child.
     * 
     * @return the parent path.
     */
    public abstract Path parent();

    /**
     * get the name of the deepest path component.
     * 
     * @return the name of the current path component.
     */
    public abstract String getName();

    @Override
    public abstract String toString();

    /**
     * get the length of the path.
     * 
     * @return the length of the path.
     */
    public abstract int size();

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object obj);

    private static class RelativeParent extends Path {

	private final Path path;

	public RelativeParent(Path path) {
	    this.path = path;
	}

	@Override
	public Path parent() {
	    return new RelativeParent(this);
	}

	@Override
	public String toString() {
	    return path + "/..";
	}

	@Override
	public Path child(String name) {
	    return new ChildPath(this, name);
	}

	@Override
	public String getName() {
	    return "..";
	}

	@Override
	public int size() {
	    return path.size() + 1;
	}

	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 0;
	    result = prime * result + ((path == null) ? 0 : path.hashCode());
	    return result;
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj)
		return true;
	    if (getClass() != obj.getClass())
		return false;
	    RelativeParent other = (RelativeParent) obj;
	    if (path == null) {
		if (other.path != null)
		    return false;
	    } else if (!path.equals(other.path))
		return false;
	    return true;
	}

	@Override
	public Path getBase() {
	    return path;
	}
    }

    private static class FirstRelativeParent extends Path {
	@Override
	public Path parent() {
	    return new RelativeParent(this);
	}

	@Override
	public String toString() {
	    return "..";
	}

	@Override
	public Path child(String name) {
	    return new ChildPath(this, name);
	}

	@Override
	public String getName() {
	    return "..";
	}

	@Override
	public int size() {
	    return 2;
	}

	@Override
	public int hashCode() {
	    return 13;
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj) {
		return true;
	    }
	    if (getClass() != obj.getClass()) {
		return false;
	    }
	    return true;
	}

	@Override
	public Path getBase() {
	    return RELATIVE;
	}
    }
    /**
     * The start of an absolute path.
     */
    public static final Path ABSOLUTE = new Path() {
	@Override
	public String toString() {
	    return "/";
	}

	@Override
	public Path parent() {
	    return this;
	}

	@Override
	public Path child(String name) {
	    return new AbsoluteChild(name);
	}

	@Override
	public String getName() {
	    return "/";
	}

	@Override
	public int size() {
	    return 1;
	}

	@Override
	public int hashCode() {
	    return 13;
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj) {
		return true;
	    }
	    if (getClass() != obj.getClass()) {
		return false;
	    }
	    return true;
	}

	@Override
	public Path getBase() {
	    return this;
	}
    };

    private static class AbsoluteChild extends Path {
	private final String name;

	public AbsoluteChild(String name) {
	    this.name = name;
	}

	@Override
	public Path child(String name) {
	    return new ChildPath(this, name);
	}

	@Override
	public String getName() {
	    return name;
	}

	@Override
	public Path parent() {
	    return ABSOLUTE;
	}

	@Override
	public int size() {
	    return 2;
	}

	@Override
	public String toString() {
	    return "/" + name;
	}

	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 0;
	    result = prime * result + ((name == null) ? 0 : name.hashCode());
	    return result;
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj)
		return true;
	    if (getClass() != obj.getClass())
		return false;
	    AbsoluteChild other = (AbsoluteChild) obj;
	    if (name == null) {
		if (other.name != null)
		    return false;
	    } else if (!name.equals(other.name))
		return false;
	    return true;
	}

	@Override
	public Path getBase() {
	    return ABSOLUTE;
	}
    }

    /**
     * The base value for a relative path.
     */
    public static final Path RELATIVE = new Path() {
	@Override
	public String toString() {
	    return ".";
	}

	@Override
	public Path parent() {
	    return new FirstRelativeParent();
	}

	@Override
	public Path child(String name) {
	    return new RelativeChild(name);
	}

	@Override
	public String getName() {
	    return ".";
	}

	@Override
	public int size() {
	    return 1;
	}

	@Override
	public int hashCode() {
	    return 13;
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj) {
		return true;
	    }
	    if (getClass() != obj.getClass()) {
		return false;
	    }
	    return true;
	}

	@Override
	public Path getBase() {
	    return this;
	}
    };

    private static class RelativeChild extends Path {

	private final String name;

	public RelativeChild(String name) {
	    this.name = name;
	}

	@Override
	public Path child(String name) {
	    return new ChildPath(this, name);
	}

	@Override
	public String getName() {
	    return name;
	}

	@Override
	public Path parent() {
	    return RELATIVE;
	}

	@Override
	public int size() {
	    return 2;
	}

	@Override
	public String toString() {
	    return name;
	}

	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 0;
	    result = prime * result + ((name == null) ? 0 : name.hashCode());
	    return result;
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj)
		return true;
	    if (getClass() != obj.getClass())
		return false;
	    RelativeChild other = (RelativeChild) obj;
	    if (name == null) {
		if (other.name != null)
		    return false;
	    } else if (!name.equals(other.name))
		return false;
	    return true;
	}

	@Override
	public Path getBase() {
	    return RELATIVE;
	}
    }

    private static class ChildPath extends Path {

	private final Path parent;
	private final String name;

	public ChildPath(Path parent, String name) {
	    this.parent = parent;
	    this.name = name;
	}

	@Override
	public String toString() {
	    return parent + "/" + name;
	}

	@Override
	public Path parent() {
	    return parent;
	}

	@Override
	public Path child(String name) {
	    return new ChildPath(this, name);
	}

	@Override
	public String getName() {
	    return name;
	}

	@Override
	public int size() {
	    return parent.size() + 1;
	}

	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 0;
	    result = prime * result + ((name == null) ? 0 : name.hashCode());
	    result = prime * result
		    + ((parent == null) ? 0 : parent.hashCode());
	    return result;
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj)
		return true;
	    if (getClass() != obj.getClass())
		return false;
	    ChildPath other = (ChildPath) obj;
	    if (name == null) {
		if (other.name != null)
		    return false;
	    } else if (!name.equals(other.name))
		return false;
	    if (parent == null) {
		if (other.parent != null)
		    return false;
	    } else if (!parent.equals(other.parent))
		return false;
	    return true;
	}

	@Override
	public Path getBase() {
	    return parent;
	}

    }

    /**
     * Get the base path.
     * 
     * @return the base path.
     */
    public abstract Path getBase();
}
