package code;
public class SearchProblem {
	byte numOperators = 8; // or array of enum
	State initialState;
	
	short numOfHostages;

	public static int calculatePathCost(Node node) {
		// OPERATOR: Move(0,1,2,3) : 1, Carry(4) : 1, Drop(5): 1, Take_Pill(6): 1,
		// Kill1(7): 1000, Kill2(8): 2000, Kill3(9): 3000, Kill4(10): 4000, Fly(11): 1
		byte op = node.operator;
		int baseCost = 0;
		switch(op) {////////////////////////       tag
		case(0): case(1): case(2): case(3): baseCost = 1;break;
		case(4): baseCost = 1;break; //carry
		case(5): baseCost = 1;break;//drop
		case(6): baseCost = 1;break;//pill
		case(7): baseCost = 1000;break;//kill1
		case(8): baseCost = 2000;break;//kill2
		case(9): baseCost = 3000;break;//kill3
		case(10): baseCost = 4000;break;//kill4
		case(11): baseCost = 1;break;//fly
		}
		if(node.state.neoHealth>=100) {
			baseCost+=1000000;
		}
		int prevHostages=0;
		int currHostages=0;
		for( int x : node.parent.state.hostagesHealth) if(x>=100) prevHostages++;
		for( int x : node.state.hostagesHealth) if(x>=100) currHostages++;
		
		baseCost+=(currHostages-prevHostages)*250000;
		return (node.parent == null)? baseCost:baseCost + node.parent.pathCost;
	}
	
	public SearchProblem(byte numOfHostages,State initialState) {
		this.numOfHostages=numOfHostages;
		 this.initialState= initialState; 
		
	}
	
	public boolean goalTest(State state) {
		// if Neo is dead, then return false
		if(state.neoHealth >= 100)
			return false;
		
		if(state.neoX!= Matrix.telephoneX || state.neoY!=Matrix.telephoneY)
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
