package htmlparser.node;

public class NodeRef extends Node {
	private Node node;

	public NodeRef(Node node) {
		this.node = node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public Node getNode() {
		return node;
	}
}
