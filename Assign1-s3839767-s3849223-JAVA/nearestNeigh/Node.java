package nearestNeigh;

public class Node {
	
	Node leftChild;
	Node rightChild;
	Point key;
	
	public Node(Point point) {
		leftChild = null;
		rightChild = null;
		key = point;
	}
	
	public Node getLeftChild() {
		return leftChild;
	}
	
	public Node getRightChild() {
		return rightChild;
	}
	
	public Point getKey() {
		return key;
	} 
}
