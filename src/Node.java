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
		case(0): this.opString = "up";break;
		case(1): this.opString = "down";break;
		case(2): this.opString = "left";break;
		case(3): this.opString = "right";break;
		case(4): this.opString = "carry";break;
		case(5): this.opString = "drop";break;
		case(6): this.opString = "takePill";break;
		case(7): this.opString = "kill";break;
//		case(8): this.opString = "KillDown";break;
//		case(9): this.opString = "KillLeft";break;
//		case(10): this.opString = "KillRight";break;
		case(8): this.opString = "fly";break;
		}
		
	}
	




	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
