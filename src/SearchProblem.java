public class SearchProblem {
	byte numOperators = 8; // or array of enum
	State initialState;
	// State space!?
	
	short numOfHostages;

	public static short calculatePathCost(Node node) {
		// OPERATOR: Move(0,1,2,3) : 2, Carry(4) : 2, Drop(5): -500, Take_Pill(6): 2, Kill(7): 2 if mutated and 20 otherwise, Fly(8): 2
		// STATE: Neo Dies: 10000, Hostage Dies 1000
		byte op = node.operator;
		int baseCost = 0;
		switch(op) {////////////////////////       tag
		case(0): baseCost = 2;break;
		case(1): baseCost = 2;break;
		case(2): baseCost = 2;break;
		case(3): baseCost = 2;break;
		case(4): baseCost = 2;break; //carry
		case(5): baseCost = 2;break;//drop
		case(6): baseCost = 0;break;//pill
		case(7): baseCost = 20;break;//kill // how to check if mutated
		case(8): baseCost = 2;break;//fly
		}
		if(node.state.neoHealth>=100) {
			baseCost+=10000;
		}
		int prevHostages=0;
		int currHostages=0;
		for( int x : node.parent.state.hostagesHealth) if(x>=100) prevHostages++;
		for( int x : node.state.hostagesHealth) if(x>=100) currHostages++;
		
		baseCost+=(currHostages-prevHostages)*1000;
		return (node.parent != null)? (short)(baseCost + node.parent.pathCost) :(short)0 ;
	}
	
	public SearchProblem(byte numOfHostages,State initialState) {
		this.numOfHostages=numOfHostages;
		 this.initialState= initialState; 
		
	}
	
	public boolean goalTest(State state) {
		
		if(state.neoX!= The_Matrix_Solver.telephoneX || state.neoY!=The_Matrix_Solver.telephoneY)
			return false;
		// Check all hostages are either delivered or died, All converted hostages are killed		
		// No one in the currently carried
		if(state.currentlyCarriedHostages!=0)
			return false;
		
		// hostages in movedHostages + killed hostagesAgent == no. of all hostages
		          
		if( (state.movedHostages |state.killedTransHostages) != (1<<numOfHostages)-1 )
			return false;
		
		return true;
	}
	public static void main(String[] args) {
		System.out.println((short)(1<<8));
		System.out.println(~(short)(1<<8));
		
		
		// 1111111011111111 
		
		
		
		
		
//		System.out.println(18 | 13);
//		System.out.println((1<<5) - 1);
//		for(int i=0;i<32;i++)
//		{
//			if( (  ( (1<<5)-1 )  & (1<<i)  ) != 0)
//			{
//				System.out.print("1");
//			}
//			else
//			{
//				System.out.print("0");
//			}
//		}
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
