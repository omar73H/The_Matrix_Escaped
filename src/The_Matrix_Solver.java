import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class The_Matrix_Solver {
	
	private static ArrayList<Integer> availableCells;
	
	public static byte m,n,c;
	public static byte initNeoX, initNeoY, telephoneX, telephoneY;
	public static byte hostagesCount;
	public static byte[] hostagesLocation;
	// location of hostage i is saves at cells 2*i and 2*i+1
	
	
	// use 2D array for grid???
	public static byte[] hostagesHealth;
	
	public static String[] pillsInformation;
	
	public static String[] padsInformation;
	
<<<<<<< HEAD
	public static byte[] agentsLocation;
	// location of hostage i is saves at cells 2*i and 2*i+1
=======
	public static String[] agentsInformation;
	// "0,0,1,1" "1,1,2,2"
>>>>>>> 3b4e9d17cd47514dd738259f64b228bec5bfe3da
	
	public static void main(String[] args) {
		System.out.println(genGrid());
	}
	
	
	private static String genGrid() {
		m = (byte)random(5,15);
		n = (byte)random(5,15);
		c = (byte)random(1,4);
		
		//#########
		System.out.println("Maximum to carry: "+c);
		
		//#########
		String[][] grid = new String[m][n];
		
		availableCells = new ArrayList<Integer>(m*n);
		
		for(int i=0;i<m*n;i++)
			availableCells.add(i);
		
		//Position of Neo
		int neoPos = randomCell();
		initNeoX = (byte)(neoPos/n);
		initNeoY = (byte)(neoPos%n);
		
		
		//########
		grid[initNeoX][initNeoY] = "N";
		
		//Position of the Telephone booth
		int telephonePos = randomCell();
		telephoneX = (byte)(telephonePos/n);
		telephoneY = (byte)(telephonePos%n);
		
		//########
		grid[telephoneX][telephoneY] = "T";
		
		//The number of hostages
		hostagesCount = (byte)random(3,10);
		hostagesLocation = new byte[hostagesCount*2];
		hostagesHealth = new byte[hostagesCount];
		
		StringBuilder hostagesInfo = new StringBuilder();
		//Positions of hostages;
		for(int i=0;i<hostagesCount;i++)
		{
			int hostagePos = randomCell();
			byte hostageX = (byte)(hostagePos/n);
			byte hostageY = (byte)(hostagePos%n);
			
			
			byte hostageDamage = (byte)random(1,99);
			//########
			grid[hostageX][hostageY] = "H"+i+" "+hostageDamage;
			
			if(i>0)
				hostagesInfo.append(",");
			
			hostagesInfo.append(hostageX+","+hostageY+","+hostageDamage);
			hostagesLocation[2*i] = hostageX;
			hostagesLocation[2*i+1] = hostageY;
			hostagesHealth[i] = hostageDamage;
		}
		//The number of pills
		byte pillsCount = (byte)random(1,hostagesCount);
		
		pillsInformation = new String[pillsCount];
		StringBuilder pillsInfo = new StringBuilder();
		//Positions of pills;
		for(int i=0;i<pillsCount;i++)
		{
			int pillPos = randomCell();
			byte pillX = (byte)(pillPos/n);
			byte pillY = (byte)(pillPos%n);
			
			//########
			grid[pillX][pillY] = "P";
			
			if(i>0)
				pillsInfo.append(",");
			
			pillsInfo.append(pillX+","+pillY);
			pillsInformation[i]= pillX+","+pillY;
		}
		
		//Pads
		int maxPadsCount = availableCells.size()%2==0? (availableCells.size()-2)/2:(availableCells.size()-1)/2;
		int padsCount = random(1,maxPadsCount);
		
		padsInformation = new String[padsCount];
		
		StringBuilder padsInfo = new StringBuilder();
		//Positions of pads;
		for(int i=0;i<padsCount;i++)
		{
			int startPadPos = randomCell();
			byte startPadX = (byte)(startPadPos/n);
			byte startPadY = (byte)(startPadPos%n);
			
			//########
			grid[startPadX][startPadY] = "F"+i;
			
			int endPadPos = randomCell();
			byte endPadX = (byte)(endPadPos/n);
			byte endPadY = (byte)(endPadPos%n);
			
			//########
			grid[endPadX][endPadY] = "D"+i;
			
			if(i>0)
				padsInfo.append(",");
			
			padsInfo.append(startPadX+","+startPadY+","+endPadX+","+endPadY);
			padsInfo.append(",");
			padsInfo.append(endPadX+","+endPadY+","+startPadX+","+startPadY);
			
			padsInformation[i] = startPadX+","+startPadY+","+endPadX+","+endPadY;
			
			
		}
		
		
		//Agents
		int agentsCount = random(1,availableCells.size());
		agentsLocation = new byte[agentsCount*2];
		
		StringBuilder agentsInfo = new StringBuilder();
		//Positions of pads;
		for(int i=0;i<agentsCount;i++)
		{
			int agentPos = randomCell();
			byte agentX = (byte)(agentPos/n);
			byte agentY = (byte)(agentPos%n);
			
			//########
			grid[agentX][agentY] = "A"+i;
			
			if(i>0)
				agentsInfo.append(",");
			
			agentsInfo.append(agentX+","+agentY);
			agentsLocation[2*i] = agentX;
			agentsLocation[2*i+1] = agentY;
		}
		
		StringBuilder gridInfo = new StringBuilder();
		
		gridInfo.append(m+","+n+";"+c+";"+initNeoX+","+initNeoY+";"+telephoneX+","+telephoneY
				+";");
		gridInfo.append(agentsInfo);
		gridInfo.append(";");
		
		gridInfo.append(pillsInfo);
		gridInfo.append(";");
		
		gridInfo.append(padsInfo);
		gridInfo.append(";");
		
		gridInfo.append(hostagesInfo);
		
		for(int i=0;i<m;i++)
			System.out.println(Arrays.toString(grid[i]));
		
		return gridInfo.toString();
	
		
	}
	
	private static int random(int start,int end) {
		return start   +  (int)( Math.random() * (end-start+1) );
	}
	
	private static int randomCell() {
		int cellIndex = random(0,availableCells.size()-1);
		int cell = availableCells.get(cellIndex);
		
		availableCells.remove(cellIndex);
		
		return cell;
	}
	
	public static String solve(String grid, String strategy, boolean visualize) {
	
		State initState = new State(initNeoX, initNeoY, (short)0, (short)0, (short)0, (short)0, 0l, 0l, 0l, 0, hostagesHealth, (byte)0);
		
		SearchProblem X = new SearchProblem(hostagesCount, initState);
		
		String plan = generalSearch(X, strategy);
		return "plan;deaths;kills;nodes";
	}
	
	public static String generalSearch(SearchProblem problem, String strategy) {
		Node node = new Node(problem.initialState, null, (byte)-1, (short)0, (short)0);
		PriorityQueue<Node> pq = new PriorityQueue<Node>();
		while(!pq.isEmpty()) 
		{
			Node currentNode = pq.poll();
			boolean isGoal = problem.goalTest(currentNode.state);
			if(isGoal)
				return buildPath(currentNode);
			
			LinkedList<Node> nodes = expand(currentNode);
		}
		return "failure"; 
	}


	private static LinkedList<Node> expand(Node currentNode) {

	    byte neoX =currentNode.state.neoX;
	    byte neoY =currentNode.state.neoY;
		//up, down, left, right, carry, drop, takePill, killU ,killD, killL, killR 	, and fly
		//0    1      2     3     4      5        6      7         8      9      10       11
		
	    LinkedList<Node> expandedNodes = new LinkedList<Node>();
	    
	    // Try Up
	    if(neoX-1 >= 0 && agentAt((byte) (neoX-1),
	    						  neoY,
	    						  currentNode.state.hostagesHealth,
	    						  currentNode.state.movedHostages,
	    						  currentNode.state.hostagesToAgents,
	    						  currentNode.state.killedTransHostages,
	    						  currentNode.state.killedNormalAgent0,
	    						  currentNode.state.killedNormalAgent1,
	    						  currentNode.state.killedNormalAgent2,
	    						  currentNode.state.killedNormalAgent3, false) < 0)
	    {
	    	State newState = timeStep(currentNode.state);
	    	newState.neoX=(byte) (neoX-1);
	    	Node newNode = new Node(newState, currentNode, (byte)0, (short)(currentNode.depth+1), (short)0);
	    	newNode.pathCost = SearchProblem.calculatePathCost(newNode);
	    	expandedNodes.add(newNode);
	    	
	    }
	    //Try Down
	    if(neoX+1 < m && agentAt((byte) (neoX+1),
								  neoY,
								  currentNode.state.hostagesHealth,
								  currentNode.state.movedHostages,
								  currentNode.state.hostagesToAgents,
								  currentNode.state.killedTransHostages,
								  currentNode.state.killedNormalAgent0,
								  currentNode.state.killedNormalAgent1,
								  currentNode.state.killedNormalAgent2,
								  currentNode.state.killedNormalAgent3, false) < 0)
	    {
			State newState = timeStep(currentNode.state);
			newState.neoX=(byte) (neoX+1);
			Node newNode = new Node(newState, currentNode, (byte)1, (short)(currentNode.depth+1), (short)0);
			newNode.pathCost = SearchProblem.calculatePathCost(newNode);
			expandedNodes.add(newNode);

	    }
	    
<<<<<<< HEAD
	  //Try Left
	    if(neoY-1 >=0 && agentAt(neoX,
	    						  (byte)(neoY-1),
								  currentNode.state.hostagesHealth,
								  currentNode.state.movedHostages,
								  currentNode.state.hostagesToAgents,
								  currentNode.state.killedTransHostages,
								  currentNode.state.killedNormalAgent0,
								  currentNode.state.killedNormalAgent1,
								  currentNode.state.killedNormalAgent2,
								  currentNode.state.killedNormalAgent3, false) < 0)
	    {
			State newState = timeStep(currentNode.state);
			newState.neoY=(byte) (neoY-1);
			Node newNode = new Node(newState, currentNode, (byte)2, (short)(currentNode.depth+1), (short)0);
			newNode.pathCost = SearchProblem.calculatePathCost(newNode);
			expandedNodes.add(newNode);

	    }
	    //Try Right
	    if(neoY+1 <n && agentAt(neoX,
	    						  (byte)(neoY+1),
								  currentNode.state.hostagesHealth,
								  currentNode.state.movedHostages,
								  currentNode.state.hostagesToAgents,
								  currentNode.state.killedTransHostages,
								  currentNode.state.killedNormalAgent0,
								  currentNode.state.killedNormalAgent1,
								  currentNode.state.killedNormalAgent2,
								  currentNode.state.killedNormalAgent3, false) < 0)
	    {
			State newState = timeStep(currentNode.state);
			newState.neoY=(byte) (neoY+1);
			Node newNode = new Node(newState, currentNode, (byte)3, (short)(currentNode.depth+1), (short)0);
			newNode.pathCost = SearchProblem.calculatePathCost(newNode);
			expandedNodes.add(newNode);

	    }
	    
	    //TryKillUp
	    short agentAt = agentAt((byte) (neoX-1),
				  neoY,
				  currentNode.state.hostagesHealth,
				  currentNode.state.movedHostages,
				  currentNode.state.hostagesToAgents,
				  currentNode.state.killedTransHostages,
				  currentNode.state.killedNormalAgent0,
				  currentNode.state.killedNormalAgent1,
				  currentNode.state.killedNormalAgent2,
				  currentNode.state.killedNormalAgent3, true);
	    if(neoX-1 >= 0 && agentAt >= 0)
	    {
	    	State newState = timeStep(currentNode.state);
	    	
	    	newState.neoHealth += 20;
	    	
	    	// if normal agent
	    	if((agentAt & (1<<8)) == 0)
	    	{
	    		if(agentAt < 64) // in the first bitmask (killedNormalAgent0)
				{
					newState.killedNormalAgent0 |= (1<<agentAt);
				}
				else if(agentAt < 128)
				{
					agentAt -= (byte)64;
					newState.killedNormalAgent1 |= (1<<agentAt);
				}
				else if(agentAt < 192)
				{
					agentAt -= (byte)128;
					newState.killedNormalAgent2 |= (1<<agentAt);
				}
				else
				{
					agentAt -= (byte)192;
					newState.killedNormalAgent3 |= (1<<agentAt);
				}
	    	}
	    	else
	    	{
	    		// negate 1<<8 then &
	    		newState.killedTransHostages |= 1<<agentAt;
	    	}
	    	
	    	Node newNode = new Node(newState, currentNode, (byte)7, (short)(currentNode.depth+1), (short)0);
	    	newNode.pathCost = SearchProblem.calculatePathCost(newNode);
	    	expandedNodes.add(newNode);
	    }
	    	
	    	
		    //TryKillDown
		    agentAt = agentAt((byte) (neoX+1),
					  neoY,
					  currentNode.state.hostagesHealth,
					  currentNode.state.movedHostages,
					  currentNode.state.hostagesToAgents,
					  currentNode.state.killedTransHostages,
					  currentNode.state.killedNormalAgent0,
					  currentNode.state.killedNormalAgent1,
					  currentNode.state.killedNormalAgent2,
					  currentNode.state.killedNormalAgent3, true);
		    // to do check first not outside the grid
		    if(neoX+1 < m && agentAt >= 0)
		    {
		    	State newState = timeStep(currentNode.state);
		    	
		    	newState.neoHealth += 20;
		    	
		    	// if normal agent
		    	if((agentAt & (1<<8)) == 0)
		    	{
		    		if(agentAt < 64) // in the first bitmask (killedNormalAgent0)
					{
						newState.killedNormalAgent0 |= (1<<agentAt);
					}
					else if(agentAt < 128)
					{
						agentAt -= (byte)64;
						newState.killedNormalAgent1 |= (1<<agentAt);
					}
					else if(agentAt < 192)
					{
						agentAt -= (byte)128;
						newState.killedNormalAgent2 |= (1<<agentAt);
					}
					else
					{
						agentAt -= (byte)192;
						newState.killedNormalAgent3 |= (1<<agentAt);
					}
		    	}
		    	else
		    	{
		    		// negate 1<<8 then &
		    		newState.killedTransHostages |= 1<<agentAt;
		    	}
		    	
		    	Node newNode = new Node(newState, currentNode, (byte)8, (short)(currentNode.depth+1), (short)0);
		    	newNode.pathCost = SearchProblem.calculatePathCost(newNode);
		    	expandedNodes.add(newNode);
		    }
	    
		    
		    //TryKillLeft
		    agentAt = agentAt(neoX,
		    			(byte) (neoY-1),
					  currentNode.state.hostagesHealth,
					  currentNode.state.movedHostages,
					  currentNode.state.hostagesToAgents,
					  currentNode.state.killedTransHostages,
					  currentNode.state.killedNormalAgent0,
					  currentNode.state.killedNormalAgent1,
					  currentNode.state.killedNormalAgent2,
					  currentNode.state.killedNormalAgent3, true);
		    // to do check first not outside the grid
		    if(neoY-1 >= 0 && agentAt >= 0)
		    {
		    	State newState = timeStep(currentNode.state);
		    	
		    	newState.neoHealth += 20;
		    	
		    	// if normal agent
		    	if((agentAt & (1<<8)) == 0)
		    	{
		    		if(agentAt < 64) // in the first bitmask (killedNormalAgent0)
					{
						newState.killedNormalAgent0 |= (1<<agentAt);
					}
					else if(agentAt < 128)
					{
						agentAt -= (byte)64;
						newState.killedNormalAgent1 |= (1<<agentAt);
					}
					else if(agentAt < 192)
					{
						agentAt -= (byte)128;
						newState.killedNormalAgent2 |= (1<<agentAt);
					}
					else
					{
						agentAt -= (byte)192;
						newState.killedNormalAgent3 |= (1<<agentAt);
					}
		    	}
		    	else
		    	{
		    		// negate 1<<8 then &
		    		newState.killedTransHostages |= 1<<agentAt;
		    	}
		    	
		    	Node newNode = new Node(newState, currentNode, (byte)9, (short)(currentNode.depth+1), (short)0);
		    	newNode.pathCost = SearchProblem.calculatePathCost(newNode);
		    	expandedNodes.add(newNode);
		    }
		    //TryKillRight
		    agentAt = agentAt(neoX,
		    		(byte)(neoY+1),
					  currentNode.state.hostagesHealth,
					  currentNode.state.movedHostages,
					  currentNode.state.hostagesToAgents,
					  currentNode.state.killedTransHostages,
					  currentNode.state.killedNormalAgent0,
					  currentNode.state.killedNormalAgent1,
					  currentNode.state.killedNormalAgent2,
					  currentNode.state.killedNormalAgent3, true);
		    // to do check first not outside the grid
		    if(neoY+1 < n && agentAt >= 0)
		    {
		    	State newState = timeStep(currentNode.state);
		    	
		    	newState.neoHealth += 20;
		    	
		    	// if normal agent
		    	if((agentAt & (1<<8)) == 0)
		    	{
		    		if(agentAt < 64) // in the first bitmask (killedNormalAgent0)
					{
						newState.killedNormalAgent0 |= (1<<agentAt);
					}
					else if(agentAt < 128)
					{
						agentAt -= (byte)64;
						newState.killedNormalAgent1 |= (1<<agentAt);
					}
					else if(agentAt < 192)
					{
						agentAt -= (byte)128;
						newState.killedNormalAgent2 |= (1<<agentAt);
					}
					else
					{
						agentAt -= (byte)192;
						newState.killedNormalAgent3 |= (1<<agentAt);
					}
		    	}
		    	else
		    	{
		    		// negate 1<<8 then &
		    		newState.killedTransHostages |= 1<<agentAt;
		    	}
		    	
		    	Node newNode = new Node(newState, currentNode, (byte)10, (short)(currentNode.depth+1), (short)0);
		    	newNode.pathCost = SearchProblem.calculatePathCost(newNode);
		    	expandedNodes.add(newNode);
		    }
		return null;
	}
	/**
	 * 
	 * @param x
	 * @param y
	 * @param hostagesHealth
	 * @param movedHostages
	 * @param hostagesToAgents
	 * @param killedTransHostages
	 * @param killedNormalAgent0
	 * @param killedNormalAgent1
	 * @param killedNormalAgent2
	 * @param killedNormalAgent3
	 * @return
	 * if we check for movement then we return a short with negative number, if no agent in the directed cell
	 * if we check for trying kill then
	 * if there is an agent we return the index saved in in least siginifcant 8 bits 
	 * and the 9th bit is set to 0 to indicate a normal agent  Or 1 to indicate a Hostage turned to agent
	 * else will return negative value
	 */
	private static short agentAt(byte x,
=======
	    Node n = fly(currentNode);
		 
		return null;
	}
	
	public static Node fly(Node node) {
		for(int i=0; i<padsInformation.length;i++) {
			
		}
		return null;
	}
	
	public static Node pickUpAgent(Node node) {
		int index = -1;
		String lookingfor = node.state.neoX + "," + node.state.neoY;
		for(int i = 0; i<hostagesInformation.length;i++) 
			if(hostagesInformation[i].equals(lookingfor)) {
				index = i;
				break;
			}

		if(index == -1)
			return null;
		Node nextNode = node.
		return null;
	}
	
	private static boolean agentAt(byte x,
>>>>>>> 3b4e9d17cd47514dd738259f64b228bec5bfe3da
								   byte y,
								   byte[] hostagesHealth,
								   short movedHostages,
								   short hostagesToAgents,
								   short killedTransHostages,
								   long killedNormalAgent0,
								   long killedNormalAgent1,
								   long killedNormalAgent2,
								   int killedNormalAgent3,
								   boolean tryKill) 
	{
		for(byte i=(byte)0;i<hostagesHealth.length;i++)
		{
			
			// if not at that location
			if(x != hostagesLocation[2*i] || y != hostagesLocation[2*i+1])
			{
				continue;
			}
			// if it was moved before (in the moved hostages bitmask)
			if((movedHostages & (1<<i)) != 0)
			{
				return Short.MIN_VALUE;
			}
			
			
			if(hostagesHealth[i] >= 98)
			{
			
				// it is dead
				if(hostagesHealth[i] == 100)
				{
					// since it was not moved and it is dead, then we know for sure it was converted to agent
					// if not killed yet
					if((killedTransHostages&(1<<i)) == 0 )
					{
						short ans = i;
						ans |= (1<<8);
						
						return ans;
					}
					return Short.MIN_VALUE;
				}
				// will die and become an agent at the next time step
				else
				{
					if(!tryKill)
					{
						short ans = i;
						ans |= (1<<8);	
						return ans;
					}
					return Short.MIN_VALUE;
				}
				
			}
		}
		
		for(byte i=0;i<agentsLocation.length;i+=2)
		{
			if(x != agentsLocation[i] || y != agentsLocation[i+1])
			{
				continue;
			}
			
			byte idx = (byte)(i/2);
			
			if(idx < 64) // in the first bitmask (killedNormalAgent0)
			{
				// if not killed
				if( (killedNormalAgent0 & (1<<idx)) == 0 )
				{
					return (short)idx;
				}
			}
			else if(idx < 128)
			{
				idx -= (byte)64;
				if( (killedNormalAgent1 & (1<<idx)) == 0 )
				{
					return (short)(idx+64);
				}
			}
			else if(idx < 192)
			{
				idx -= (byte)128;
				if( (killedNormalAgent2 & (1<<idx)) == 0 )
				{
					return (short)(idx+128);
				}
			}
			else
			{
				idx -= (byte)192;
				if( (killedNormalAgent3 & (1<<idx)) == 0 )
				{
					return (short)(idx+192);
				}
			}
			return Short.MIN_VALUE;
		}
		
		return Short.MIN_VALUE;
	}
	
	
	private static State timeStep(State currState) {
		// This function is for doing the basic update in the state related to 1 time step passed
		//if another custom update required, then to be done after calling this function
		State newState = new State(currState.neoX,
					currState.neoY,
					currState.movedHostages,
					currState.currentlyCarriedHostages,
					currState.hostagesToAgents,
					currState.killedTransHostages,
					currState.killedNormalAgent0,
					currState.killedNormalAgent1,
					currState.killedNormalAgent2,
					currState.killedNormalAgent3,
					currState.hostagesHealth,
					currState.neoHealth);
		for(byte i=(byte)0;i<currState.hostagesHealth.length;i++)
		{
			 // we check first if the hostage was delivered or not , if it was then we dont change it health 
			 //(in moved and not in currently carried)
			if(((newState.movedHostages & (1<<i)) !=0) && ((newState.currentlyCarriedHostages & (1<<i)) ==0))
				continue;
			
			newState.hostagesHealth[i] = (byte)Math.min(100, newState.hostagesHealth[i]+2);
			//if the hostage was not rescued and it health reached 100 
			// we turn the hostage into agent if and only if it was not carried at the moment
			if(newState.hostagesHealth[i]>=100)
			{	
				if((newState.currentlyCarriedHostages & 1<<i) !=0)
					newState.hostagesToAgents |=1<<i;

			}
				
		}
		return newState;
	}
	
	private static String buildPath(Node currentNode) {
		// TODO Auto-generated method stub
		return null;
	}
}
