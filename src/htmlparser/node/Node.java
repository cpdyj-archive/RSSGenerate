package htmlparser.node;

public class Node {
	private Node prev,next,parent,childFirst,childLast;
	private int size;
	private NodeRef ref;

	public NodeRef getRef() {
		ref = new NodeRef(this);
		return ref;
	}

	public void moveChildTo(Node node) {
		Node current=this.childFirst;
		while (current != null) {
			current.parent = node;
			current = current.next;
		}
		node.size = this.size;
		node.childFirst = this.childFirst;
		node.childLast = this.childLast;
		this.size = 0;
		this.childFirst = null;
		this.childLast = null;
	}

	public void replaceOne(Node node) {
		replaceTree(node);
		moveChildTo(node);
	}

	public void replaceTree(Node node) {
		assert node.isAloneAndFree();
		if (ref != null) {
			ref.setNode(node);
			ref = null;
		}
		if (prev != null) {
			Node temp=prev;
			remove();
			temp.insertAfter(node);
		} else if (next != null) {
			Node temp=next;
			remove();
			next.insertBefore(node);
		} else if (parent != null) {
			Node temp=parent;
			remove();
			parent.insertChildFirst(node);
		}
		assert this.isAloneAndFree();
		assert !node.isAloneAndFree();
	}

	public void remove() {
		if (parent != null) {
			parent.size--;
			if (parent.childFirst == this) {
				parent.childFirst = null;
			}
			if (parent.childLast == null) {
				parent.childLast = null;
			}
			assert parent.isAvailable();
		}
		if (prev != null) {
			prev.next = this.next;
		}
		if (next != null) {
			next.prev = this.prev;
		}
		assert prev != null ?prev.isAvailable(): true;
		assert next != null ?next.isAvailable(): true;
		next = null;
		prev = null;
		parent = null;
		assert isAlone();
		assert isFree();
	}

	public void insertChildFirst(Node node) {
		assert node.isAloneAndFree();
		node.parent = this;
		if (size == 0) {
			childFirst = node;
			childLast = node;
			size = 1;
		} else {
			size++;
			childFirst.insertBefore(node);
		}
		assert !node.isFree();
		assert isAvailable();
	}

	public void insertChildLast(Node node) {
		assert node.isAloneAndFree();
		node.parent = this;
		if (size == 0) {
			childFirst = node;
			childLast = node;
			size = 1;
		} else {
			size++;
			childLast.insertAfter(node);
		}
		assert !node.isFree();
		assert isAvailable();
	}
	public void insertBefore(Node node) {
		assert node.isAloneAndFree();
		if (parent != null) {
			parent.size++;
			if (parent.childFirst == this) {
				parent.childFirst = node;
			}
			assert parent.isAvailable();
		}
		if (prev != null) {
			node.prev = prev;
			prev.next = node;
			assert !prev.isAlone();
		}
		prev = node;
		node.next = this;
		assert !node.isAlone();
		assert isAvailable();
	}

	public void insertAfter(Node node) {
		assert node.isAloneAndFree();
		if (parent != null) {
			parent.size++;
			if (parent.childLast == this) {
				parent.childLast = node;
			}
			assert parent.isAvailable();
		}
		if (next != null) {
			node.next = next;
			next.prev = node;
			assert !next.isAlone();
		}
		next = node;
		node.prev = this;
		assert !node.isAlone();
		assert isAvailable();
	}

	public Node getPrev() {
		return prev;
	}

	public Node getNext() {
		return next;
	}

	public Node getParent() {
		return parent;
	}

	public Node getChildFirst() {
		return childFirst;
	}

	public Node getChildLast() {
		return childLast;
	}

	public int size() {
		return size;
	}

	// Only for assert.
	public boolean isAloneAndFree() {
		assert isAvailable();
		return parent == null && prev == null && next == null;
	}
	public boolean isFree() {
		assert isAvailable();
		return parent == null;
	}
	public boolean isAlone() {
		assert isAvailable();
		return next == null && prev == null;
	}
	public boolean isAvailable() {
		boolean available;
		available = size == 0 ?childFirst == null && childLast == null: childFirst != null && childLast != null;
		available &= size == 1 ?childFirst == childLast: true;
		available &= size >= 0;
		available &= prev != null ?prev.next == this: true;
		available &= next != null ?next.prev == this: true;
		assert available:"Node state isn't available.";
		return available;
	}
}