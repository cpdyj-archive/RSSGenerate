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
		assert isAloneAndFree();
		assert size == 0;
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
			temp.insertBefore(node);
		} else if (parent != null) {
			Node temp=parent;
			remove();
			temp.insertChildFirst(node);
		}
		assert this.isAloneAndFree();
		assert !node.isAloneAndFree();
	}

	public void remove() {
		if (parent != null) {
			parent.size--;
			if (parent.childFirst == this) {
				parent.childFirst = next;
			}
			if (parent.childLast == this) {
				parent.childLast = prev;
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
			childFirst.insertBefore(node);
		}
		node.parent = this;
		assert !node.isFree();
		assert isAvailable();
		assert childFirst == node;
	}

	public void insertChildLast(Node node) {
		assert node.isAloneAndFree();
		if (size == 0) {
			childFirst = node;
			childLast = node;
			size = 1;
		} else {
			childLast.insertAfter(node);
		}
		node.parent = this;
		assert !node.isFree();
		assert isAvailable();
		assert childLast == node;
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
		assert available;
		available &= size == 1 ?childFirst == childLast: true;
		assert available;
		available &= size >= 0;
		assert available;
		available &= prev != null ?prev.next == this: true;
		assert available;
		available &= next != null ?next.prev == this: true;
		assert available:"Node state isn't available.";
		return available;
	}
}
