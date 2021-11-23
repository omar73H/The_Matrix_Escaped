public class SearchProblem {
	byte numOperators = 8; // or array of enum
	State initialState;
	// State space!?
	
	short numOfHostages;

	public static short calculatePathCost(Node node) {
		// OPERATOR: Move(0,1,2,3) : 2, Carry(4) : 2, Drop(5): -500, Take_Pill(11): 2, Kill(7,8,9,10): 2 if mutated and 20 otherwise, Fly(11): 2
		// STATE: Neo Dies: 10000, Hostage Dies 1000
		byte op = node.operator;
		int baseCost = 0;
		switch(op) {
		case(0): baseCost = 2;break;
		case(1): baseCost = 20;break; // how to check if mutated
		case(2): baseCost = 2;break;
		case(3): baseCost = -500;break;
		case(4): baseCost = 2;break;
		case(5): baseCost = 2;break;
		}
		if(node.state.neoHealth<=0) {
			baseCost+=10000;
		}
		int prevHostages=0;
		int currHostages=0;
		for( int x : node.parent.state.hostagesHealth) if(x==0) prevHostages++;
		for( int x : node.state.hostagesHealth) if(x==0) currHostages++;
		
		baseCost+=(currHostages-prevHostages)*1000;
		return (node.parent != null)? (short)(baseCost + node.parent.pathCost) :(short)0 ;
	}
	
	public SearchProblem(byte numOfHostages,State initialState) {
		this.numOfHostages=numOfHostages;
		 this.initialState= initialState; 
		
	}
	
	public boolean goalTest(State state) {
		// Check all hostages are either delivered or died, All converted hostages are killed
		
		// No one in the currently carried
		if(state.currentlyCarriedHostages!=0)
			return false;
		
		// hostages in movedHostages + killed hostagesAgent == no. of all hostages
		if((state.movedHostages |state.killedTransHostages)!= (1<<numOfHostages)-1)
			return false;
		
		return true;
	}
	
//	public static void main(String[] args) {
//		byte neoX=0;
//		byte neoY=0;
//		short movedHostages=0;
//		short currentlyCarriedHostages=0;
//		short hostagesToAgents=0;
//		short killedTransHostages=0;
//		byte[]  prehostagesHealth= {10,5,6,7,0};
//		byte[]  currhostagesHealth= {10,5,6,11,0};
//
//		byte neoHealth=0;
//		State prevState=new State(neoX,neoY,movedHostages,currentlyCarriedHostages,hostagesToAgents,killedTransHostages,prehostagesHealth,(byte)50);
//		State currState=new State(neoX,neoY,movedHostages,currentlyCarriedHostages,hostagesToAgents,killedTransHostages,currhostagesHealth,neoHealth);
////		(State state, Node parent, byte operator, short depth, short pathCost)
//		Node parent=new Node(prevState,null,(byte) 1,(short) 10,(short) 100);
//		Node node=new Node(currState,parent,(byte) 1,(short) 10,(short) 100);
//		System.out.println(calculatePathCost(node));
//
//	}
}
