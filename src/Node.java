public class Node {

	State state;
	Node parent;
	byte operator;
	short depth;
	short pathCost;
	
	
	
	
	public Node(State state, Node parent, byte operator, short depth, short pathCost) {
		super();
		this.state = state;
		this.parent = parent;
		this.operator = operator;
		this.depth = depth;
		this.pathCost = pathCost;
	}




	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
