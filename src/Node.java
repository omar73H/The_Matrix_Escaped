public class Node {

	State state;
	Node parent;
	byte operator;
	String opString;
	short depth;
	short pathCost;
	
	
	
	
	public Node(State state, Node parent, byte operator, short depth, short pathCost) {
		super();
		this.state = state;
		this.parent = parent;
		this.operator = operator;
		this.depth = depth;
		this.pathCost = pathCost;
		
		switch(operator) {
		case(0): this.opString = "MoveUp";break;
		case(1): this.opString = "MoveDown";break;
		case(2): this.opString = "MoveLeft";break;
		case(3): this.opString = "MoveRight";break;
		case(4): this.opString = "Carry";break;
		case(5): this.opString = "Drop";break;
		case(6): this.opString = "TakePill";break;
		case(7): this.opString = "KillUp";break;
		case(8): this.opString = "KillDown";break;
		case(9): this.opString = "KillLeft";break;
		case(10): this.opString = "KillRight";break;
		case(11): this.opString = "Fly";break;
		}
		
	}
	




	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
