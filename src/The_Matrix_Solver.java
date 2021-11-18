import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.HashSet;

import javax.jws.soap.InitParam;

public class The_Matrix_Solver {
	private static  HashSet<String> encodedNodes = new HashSet<String>();
	private static ArrayList<Integer> availableCells;
	
	public static byte m,n,c;
	public static byte initNeoX, initNeoY, telephoneX, telephoneY;
	public static byte hostagesCount;
	public static byte[] hostagesHealth;
	
	// location of hostage i is saves at cells 2*i and 2*i+1
	public static byte[] hostagesLocation;
	
	public static byte[] pillsLocation;
	
	public static byte[] padsStartLocation;
	public static byte[] padsEndLocation;
	
	public static byte[] agentsLocation;
	
	
	public static void main(String[] args) {
		String grid = genGrid();
		System.out.println(grid);
		
//		m = (byte)5;
//		n = (byte)5;
//		c=1;
//		initNeoX = (byte)0;
//		initNeoY = (byte)4;
//		telephoneX =(byte)1;
//		telephoneY =(byte)4;
//		hostagesCount = (byte)3;
//		hostagesHealth = new byte[hostagesCount]; hostagesHealth[0] = (byte)30; ; hostagesHealth[1] = (byte)80;; hostagesHealth[2] = (byte)80;// hostagesHealth[3] = (byte)98;//; hostagesHealth[4] = (byte)90;//; hostagesHealth[5] = (byte)98;; hostagesHealth[6] = (byte)98; hostagesHealth[7] = (byte)98;
//		
//		hostagesLocation = new byte[8]; hostagesLocation[0]=(byte)0;hostagesLocation[1]=(byte)0;
//		 hostagesLocation[2]=(byte)3;hostagesLocation[3]=(byte)0;
//		 hostagesLocation[4]=(byte)4;hostagesLocation[5]=(byte)4;
////		 hostagesLocation[6]=(byte)0;hostagesLocation[7]=(byte)2;
////		 hostagesLocation[8]=(byte)2;hostagesLocation[9]=(byte)4;
////		 hostagesLocation[10]=(byte)3;hostagesLocation[11]=(byte)2;
////		 hostagesLocation[12]=(byte)4;hostagesLocation[13]=(byte)4;
////		 hostagesLocation[14]=(byte)1;hostagesLocation[15]=(byte)0;
//
//
//		 
//		pillsLocation = new byte[0];
//		padsStartLocation = new byte[2];padsStartLocation[0]=(byte)0;padsStartLocation[1]=(byte)3;
//		padsEndLocation = new byte[2];padsEndLocation[0]=(byte)4;padsEndLocation[1]=(byte)3;
//		agentsLocation = new byte[12];
//		agentsLocation[0]=(byte)0; agentsLocation[1]=(byte)1;
//		agentsLocation[2]=(byte)1; agentsLocation[3]=(byte)1;
//		agentsLocation[4]=(byte)2; agentsLocation[5]=(byte)1;
//		agentsLocation[6]=(byte)3; agentsLocation[7]=(byte)1;
//		agentsLocation[8]=(byte)3; agentsLocation[9]=(byte)3;
//		agentsLocation[10]=(byte)3; agentsLocation[11]=(byte)4;

		System.out.println(solve(grid, "bfs", false));
//		State initState = new State(initNeoX, initNeoY, (short)0, (short)0, (short)0, (short)0, 0l, 0l, 0l, 0, hostagesHealth, (byte)0);
//		Node initNode = new Node(initState, null, (byte)-1, (short)0, (short)0);
//		Node n2 = expand(initNode).get(0);
//		System.out.println(expand(n2).size());
	}
	
	
	private static String genGrid() {
		m = (byte)random(5,15);
		n = (byte)random(5,15);
		m = 15;
		n = 15;
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
		
		pillsLocation = new byte[pillsCount*2];
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
			pillsLocation[2*i]= pillX;
			pillsLocation[2*i+1]= pillY;
		}
		
		//Pads
		int maxPadsCount = availableCells.size()%2==0? (availableCells.size()-2)/2:(availableCells.size()-1)/2;
		int padsCount = random(1,maxPadsCount);
		
		
		padsStartLocation = new byte[padsCount*2];
		padsEndLocation = new byte[padsCount*2];
		
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
			
			padsStartLocation[2*i] = startPadX;
			padsStartLocation[2*i+1] = startPadY;
			padsEndLocation[2*i] = endPadX;
			padsEndLocation[2*i+1] = endPadY;
			
			
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
		return plan+";deaths;kills;nodes";
	}
	
	public static String generalSearch(SearchProblem problem, String strategy) {
		Node initNode = new Node(problem.initialState, null, (byte)-1, (short)0, (short)0);
		encodedNodes.add(encode(initNode));
		if(strategy.equals("bfs")) {
			return bfs(initNode, problem);
		}
		return "failure";
	}
	
	public static String bfs(Node initNode, SearchProblem problem){
		PriorityQueue<Node> pq = new PriorityQueue<Node>((x,y)->(x.depth-y.depth));// BFS
		pq.add(initNode);
		while(!pq.isEmpty()) 
		{
			Node currentNode = pq.poll();
			System.out.println(currentNode.opString + " " + currentNode.state.neoX + " " + currentNode.state.neoY +" "+ currentNode.depth);
			//System.out.println(currentNode.depth);
			//short btts=(short)(currentNode.state.movedHostages|currentNode.state.killedTransHostages);
			//System.out.println((1<<hostagesCount)-1+" "+btts);
			try {
				Thread.sleep(0);
			}
			catch(Exception e) {
				
			}
			boolean isGoal = problem.goalTest(currentNode.state);
			if(isGoal)
				return buildPath(currentNode);
			
			LinkedList<Node> nodes = expand(currentNode);

			for(Node node: nodes) {
				pq.add(node);
			}
			
			//System.out.println(pq.size());
			
		}
		return "Fail";
	}
	
	public static String dfs(Node initNode, SearchProblem problem) {
		PriorityQueue<Node> pq = new PriorityQueue<Node>((x,y)->(y.depth-x.depth));// BFS
		pq.add(initNode);
		while(!pq.isEmpty()) 
		{
			Node currentNode = pq.poll();
			System.out.println(currentNode.opString + " " + currentNode.state.neoX + " " + currentNode.state.neoY +" "+ currentNode.depth+" "+ encode(currentNode));
			try {
				Thread.sleep(0);
			}
			catch(Exception e) {
				
			}
			boolean isGoal = problem.goalTest(currentNode.state);
			if(isGoal)
				return buildPath(currentNode);
			
			LinkedList<Node> nodes = expand(currentNode);
			
			for(Node node: nodes) {
				pq.add(node);
			}
			
		}
		return "Fail";
	}

	private static LinkedList<Node> expand(Node currentNode) {
	    
		//up, down, left, right, carry, drop, takePill, killU ,killD, killL, killR 	, and fly
		//0    1      2     3     4      5        6      7         8      9      10       11
		
	    LinkedList<Node> expandedNodes = new LinkedList<Node>();
	    
	    if(currentNode.state.neoHealth>=100)
	    {
	    	return expandedNodes;
	    }
	    	
	    
	    Node triedNode=null;
	    
	    // Try carry
	    triedNode = pickUpAgent(currentNode);
	    String encodedNode="";
	    if(triedNode!=null)
	    {	encodedNode=encode(triedNode);
	    	if(!(encodedNodes.contains(encodedNode)))
	    	{
	    	encodedNodes.add(encodedNode);
			expandedNodes.add(triedNode);
	    	}
	    }
	    // Try drop
	    triedNode = dropAllHostages(currentNode);
	    
	    if(triedNode!=null)
	    {	encodedNode=encode(triedNode);
	    	if(!(encodedNodes.contains(encodedNode)))
	    	{
	    	encodedNodes.add(encodedNode);
			expandedNodes.add(triedNode);
	    	}
	    }
	    // Try takepill
	    triedNode = takePill(currentNode);
	    if(triedNode!=null)
	    {
			expandedNodes.add(triedNode);
	    }
	    //Try kill up
	    triedNode = kill(7, currentNode);
 		//if the node is not null we check if it's repeated state or not
	    if(triedNode!=null)
	    {	encodedNode=encode(triedNode);
	    	//if its repeated state we dont add it to the list 
			//if not we add it to hash set and add it to linked list
	    	if(!(encodedNodes.contains(encodedNode)))
	    	{
	    		encodedNodes.add(encodedNode);
				expandedNodes.add(triedNode);
	    	}
	    }
	    
	    //Try kill down
	    triedNode = kill(8, currentNode);
 		//if the node is not null we check if it's repeated state or not
	    if(triedNode!=null)
	    {	encodedNode=encode(triedNode);
	    	//if its repeated state we dont add it to the list 
			//if not we add it to hash set and add it to linked list
	    	if(!(encodedNodes.contains(encodedNode)))
	    	{
	    		encodedNodes.add(encodedNode);
	    		expandedNodes.add(triedNode);
	    	}
	    }
	    // Try kill left
	    triedNode = kill(9, currentNode);
 		//if the node is not null we check if it's repeated state or not
	    if(triedNode!=null)
	    {	encodedNode=encode(triedNode);
	    	//if its repeated state we dont add it to the list 
			//if not we add it to hash set and add it to linked list
	    	if(!(encodedNodes.contains(encodedNode)))
	    	{
	    		encodedNodes.add(encodedNode);
	    		expandedNodes.add(triedNode);
	    	}
	    }
	    // Try kill right
	    triedNode = kill(10, currentNode);
 		//if the node is not null we check if it's repeated state or not
	    if(triedNode!=null)
	    {	
	    	encodedNode=encode(triedNode);
	    	//if its repeated state we dont add it to the list 
			//if not we add it to hash set and add it to linked list
	    	if(!(encodedNodes.contains(encodedNode)))
	    	{
	    		encodedNodes.add(encodedNode);
	    		expandedNodes.add(triedNode);
	    	}
	    }
	    
	    
		//Try fly if parent was not fly
	    if(currentNode.operator!=11)
	    {
	    	triedNode = fly(currentNode);
	 		//if the node is not null we check if it's repeated state or not
	    	if(triedNode!=null)
	    	{	
	    		encodedNode=encode(triedNode);
	    		//if its repeated state we dont add it to the list 
	    		//if not we add it to hash set and add it to linked list
	    		if(!(encodedNodes.contains(encodedNode)))
	    		{
	    			encodedNodes.add(encodedNode);
	    			expandedNodes.add(triedNode);
	    		}
	    	}
	    }
	  //Try move up if parent was not move down
	    if(currentNode.operator!=1)
	    {
	     triedNode=move(0, currentNode);
 		 //if the node is not null we check if it's repeated state or not
	     if(triedNode!=null)
	     {	
	    	 encodedNode=encode(triedNode);
	    	//if its repeated state we dont add it to the list 
    		//if not we add it to hash set and add it to linked list
	    	 if(!(encodedNodes.contains(encodedNode)))
	    	 {
		    	encodedNodes.add(encodedNode);
				expandedNodes.add(triedNode);
	    	 }
	     }
	    }
	    //Try move down if parent was not move up 
	    if(currentNode.operator!=0)
	    {
	    	triedNode=move(1, currentNode);
	    	//if the node is not null we check if it's repeated state or not
	    	if(triedNode!=null)
	    	{	
	    		encodedNode=encode(triedNode);
	    		//if its repeated state we dont add it to the list 
	    		//if not we add it to hash set and add it to linked list
	    		if(!(encodedNodes.contains(encodedNode)))
	    		{
	    			encodedNodes.add(encodedNode);
	    			expandedNodes.add(triedNode);
	    		}
	    	}
	    }
	    // Try move left if parent was not move right
	    if(currentNode.operator!=3)
	    {
	    	triedNode=move(2, currentNode);
		    //if the node is not null we check if it's repeated state or not
	    	if(triedNode!=null)
	    	{		
	    		encodedNode=encode(triedNode);
	    		//if its repeated state we dont add it to the list 
	    		//if not we add it to hash set and add it to linked list
	    		if(!(encodedNodes.contains(encodedNode)))
	    		{
	    			encodedNodes.add(encodedNode);
	    			expandedNodes.add(triedNode);
	    		}
	    	}
	    }
	    // Try move right if parent was not move left
	    if(currentNode.operator!=2)
	    {
	    	triedNode=move(3, currentNode);
	    	//if the node is not null we check if it's repeated state or not
	    	if(triedNode!=null)
	    	{	
	    		encodedNode=encode(triedNode);
	    		//if its repeated state we dont add it to the list 
	    		//if not we add it to hash set and add it to linked list
	    		if(!(encodedNodes.contains(encodedNode)))
	    		{
	    			encodedNodes.add(encodedNode);
	    			expandedNodes.add(triedNode);
	    		}
	    	}
	    }
	    
		return expandedNodes;
	}
	
	public static Node move(int actionId, Node currentNode)
	{
		byte neoX =currentNode.state.neoX;
	    byte neoY =currentNode.state.neoY;
		if(actionId == 0)
		{
			// Try Up
		    if(currentNode.operator!=1 && neoX-1 >= 0 && agentAt((byte) (neoX-1),
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
		    	return newNode;
		    	
		    }
			
		}
		else if(actionId == 1)
		{
			//TryDown
			if(currentNode.operator!=0 &&neoX+1 < m && agentAt((byte) (neoX+1),
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
				return newNode;
			}
		}
		else if(actionId == 2)
		{
			//Try Left
			if(currentNode.operator!=3 && neoY-1 >=0 && agentAt(neoX,
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
				return newNode;
			}
		}
		else if(actionId == 3)
		{
			//Try Right
			if(currentNode.operator!=2 && neoY+1 <n && agentAt(neoX,
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
				return newNode;
			}
		}
		return null;
	}
	
	public static Node kill(int actionId, Node currentNode)
	{
		byte neoX =currentNode.state.neoX;
	    byte neoY =currentNode.state.neoY;
	    if(actionId == 7)
	    {
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
		    		agentAt = (short)(agentAt & ~(1<<8));
		    		newState.killedTransHostages |= 1<<agentAt;
		    	}
		    	
		    	Node newNode = new Node(newState, currentNode, (byte)7, (short)(currentNode.depth+1), (short)0);
		    	newNode.pathCost = SearchProblem.calculatePathCost(newNode);
		    	return newNode;
		    }
	    }
	    else if(actionId == 8)
	    {
	    	//TryKillDown
	    	short agentAt = agentAt((byte) (neoX+1),
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
		    		agentAt = (short)(agentAt & ~(1<<8));
		    		newState.killedTransHostages |= 1<<agentAt;
		    	}
		    	
		    	Node newNode = new Node(newState, currentNode, (byte)8, (short)(currentNode.depth+1), (short)0);
		    	newNode.pathCost = SearchProblem.calculatePathCost(newNode);
		    	return newNode;
		    }
	    }
	    else if(actionId == 9)
	    {
	    	//TryKillLeft
		    short agentAt = agentAt(neoX,
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
		    		agentAt = (short)(agentAt & ~(1<<8));
		    		newState.killedTransHostages |= 1<<agentAt;
		    	}
		    	
		    	Node newNode = new Node(newState, currentNode, (byte)9, (short)(currentNode.depth+1), (short)0);
		    	newNode.pathCost = SearchProblem.calculatePathCost(newNode);
		    	return newNode;
		    }
	    }
	    else if(actionId == 10)
	    {
	    	//TryKillRight
		    short agentAt = agentAt(neoX,
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
		    		agentAt = (short)(agentAt & ~(1<<8));
		    		newState.killedTransHostages |= 1<<agentAt;
		    	}
		    	
		    	Node newNode = new Node(newState, currentNode, (byte)10, (short)(currentNode.depth+1), (short)0);
		    	newNode.pathCost = SearchProblem.calculatePathCost(newNode);
		    	return newNode;
		    }
	    }
	    return null;
		    
	}
	
	//  
	
	// //////// 
		public static Node fly(Node node) {

			short[] t=isTherePad(node.state.neoX, node.state.neoY);
			if(t[0]!=0) {
				if(t[1]==0) {
				State tempNode=timeStep(node.state);
				tempNode.neoX=padsEndLocation[t[2]];
				tempNode.neoY=padsEndLocation[t[2]+1];
				Node sNode=new Node(tempNode,node,(byte) 11,(short)(node.depth+1),(short)0);
				sNode.pathCost=SearchProblem.calculatePathCost(sNode);
				return sNode;
				}
				else {
					State tempNode=timeStep(node.state);
					tempNode.neoX=padsStartLocation[t[2]];
					tempNode.neoY=padsStartLocation[t[2]+1];
					Node sNode=new Node(tempNode,node,(byte) 11,(short)(node.depth+1),(short)0);
					sNode.pathCost=SearchProblem.calculatePathCost(sNode);

					return sNode;
				}
//				State state;
//				Node parent;
//				byte operator;
//				short depth;
//				short pathCost;
				
			}
			else {
				return null ;
			}
			
		}
		public static Node takePill(Node node) {
			if(isTherePill(node.state.neoX, node.state.neoY)) {
				
				
				////////
				
				State newState = new State(node.state.neoX,
						node.state.neoY,
						node.state.movedHostages,
						node.state.currentlyCarriedHostages,
						node.state.hostagesToAgents,
						node.state.killedTransHostages,
						node.state.killedNormalAgent0,
						node.state.killedNormalAgent1,
						node.state.killedNormalAgent2,
						node.state.killedNormalAgent3,
						node.state.hostagesHealth,
						node.state.neoHealth);
			for(byte i=(byte)0;i<node.state.hostagesHealth.length;i++)
			{
				 // we check first if the hostage was delivered or not , if it was then we dont change it health 
				 //(in moved and not in currently carried)
				if(((newState.movedHostages & (1<<i)) !=0) && ((newState.currentlyCarriedHostages & (1<<i)) ==0))
					continue;
				
				newState.hostagesHealth[i] = (byte)Math.min(100, newState.hostagesHealth[i]-20);
				//if the hostage was not rescued and it health reached 100 
				// we turn the hostage into agent if and only if it was not carried at the moment
				if(newState.hostagesHealth[i]<0)
				{	
					newState.hostagesHealth[i]=0;
				}
					
			}
				////////
				Node sNode=new Node(newState,node,(byte) 6,(short)(node.depth+1),(short)0);
				
				return sNode;
				
				
			}
			else {
				return null ;
			}
			
			
		}
		public static boolean isTherePill(byte x,byte y) {
			for(int i=0;i<pillsLocation.length;i=i+2) {
				
				if(x==pillsLocation[i]&&y==pillsLocation[i+1])
				return true;
			}
			return false;
		}
		
		//check if there pad at location
		public static short[] isTherePad(byte x,byte y) {
			short s[]= {0,0,0};

			for(short i=0;i<padsStartLocation.length;i=(short)(i+2)) {
				if(x==padsStartLocation[i]&&y==padsStartLocation[i+1]) {
				s[0]=1;
				s[1]=0;
				s[2]=i;return s;}
				else if (x==padsEndLocation[i]&&y==padsEndLocation[i+1]) {
					s[0]=1;
					s[1]=1;
					s[2]=i; return s;
				}
			}
			return s;	}
		
		
		
		
		
		///////////////////////////////////////////////////////////////////////////////
	private static Node dropAllHostages(Node currentNode) {
		if(telephoneX != currentNode.state.neoX)
			return null;
		if(telephoneY != currentNode.state.neoY)
			return null;
		
//		for(int i = 0; i<currentNode.state.hostagesHealth.length;i++) {
//			// Neo is carrying this hostage
//			if((currentNode.state.currentlyCarriedHostages & (1<<i)) != 0)
//			{
//				currentNode.state.currentlyCarriedHostages = 0;
//			}
//		}
		
		
		
		
		
		
		if(currentNode.state.currentlyCarriedHostages==0)
		{
			return null;
		}
		
		State intermediateState = new State(currentNode.state.neoX,
				currentNode.state.neoY,
				currentNode.state.movedHostages,
				(short)0,
				currentNode.state.hostagesToAgents,
				currentNode.state.killedTransHostages,
				currentNode.state.killedNormalAgent0,
				currentNode.state.killedNormalAgent1,
				currentNode.state.killedNormalAgent2,
				currentNode.state.killedNormalAgent3,
				currentNode.state.hostagesHealth.clone(),
				currentNode.state.neoHealth);
		
		
		State newState = timeStep(intermediateState);
		Node newNode = new Node(newState, currentNode, (byte)5, (short)(currentNode.depth+1), (short)0);
		newNode.pathCost = SearchProblem.calculatePathCost(newNode);
		
		return newNode;
		
	}
	public static Node pickUpAgent(Node currentNode) {
		short hostageIdx = hostageAt(currentNode.state.neoX, currentNode.state.neoY, 
				currentNode.state.hostagesHealth, currentNode.state.movedHostages);
		if(hostageIdx <0)
			return null;
		
		State currState = currentNode.state;
		
		short intermediateCarried = currState.currentlyCarriedHostages;
		intermediateCarried |= (1 << (short)hostageIdx);
		short intermediateMoved = currState.movedHostages;
		intermediateMoved |= (1 << (short)hostageIdx);
		
		State intermediateState = new State(currState.neoX,
				currState.neoY,
				intermediateMoved,
				intermediateCarried,
				currState.hostagesToAgents,
				currState.killedTransHostages,
				currState.killedNormalAgent0,
				currState.killedNormalAgent1,
				currState.killedNormalAgent2,
				currState.killedNormalAgent3,
				currState.hostagesHealth.clone(),
				currState.neoHealth);
				
		State newState = timeStep(intermediateState);
		Node newNode = new Node(newState, currentNode, (byte)4, (short)(currentNode.depth+1), (short)0);
		newNode.pathCost = SearchProblem.calculatePathCost(newNode);
//		expandedNodes.add(newNode);
		
		return newNode;
	}
	
	// Return index of hostage at X,Y or -1 if no hostage here
		private static short hostageAt(byte x, byte y, 
				byte[] hosHealth, short movedHos) {
			
			for(byte i=(byte)0;i<hosHealth.length;i++){
				// If init location isn't here no need to check
				if(x != hostagesLocation[2*i] 
						|| y != hostagesLocation[2*i+1])
					continue;
				
				// If hostage that started at x,y was moved there is hostage one here
				if((movedHos & (1<<i)) != 0)
					return Short.MIN_VALUE;
				
				// If hostage is alive
				if(hosHealth[i] < 100){
					return i;
				}
			}
			return Short.MIN_VALUE;
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
		
		for(short i=0;i<agentsLocation.length;i+=2)
		{
			
			if(x != agentsLocation[i] || y != agentsLocation[i+1])
			{
				continue;
			}
			
			short idx = (short)(i/2);
			
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
				idx -= (short)64;
				if( (killedNormalAgent1 & (1<<idx)) == 0 )
				{
					return (short)(idx+64);
				}
			}
			else if(idx < 192)
			{
				idx -= (short)128;
				if( (killedNormalAgent2 & (1<<idx)) == 0 )
				{
					return (short)(idx+128);
				}
			}
			else
			{
				idx -= (short)192;
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
					currState.hostagesHealth.clone(),
					currState.neoHealth);
		
		
		
		
		// consider take pill
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
		if(currentNode.parent ==null)
			return "";
		return buildPath(currentNode.parent)+currentNode.opString;
	}
	// this function encode the node state for the hashSet
	public static  String encode(Node node)
	{
		
		/*
		byte neoX,neoY;
	short movedHostages;
	short currentlyCarriedHostages;
	short hostagesToAgents;
	short killedTransHostages;
	
	
	
	// interface/object that have a methods that makes the required operations easy
	long killedNormalAgent0, killedNormalAgent1, killedNormalAgent2;
	int killedNormalAgent3;
		*/
		
		StringBuilder sb = new StringBuilder();
		sb.append(node.state.neoX);
		sb.append(node.state.neoY);
		sb.append(node.state.movedHostages);
		sb.append(node.state.currentlyCarriedHostages);
		sb.append(node.state.hostagesToAgents);
		sb.append(node.state.killedTransHostages);
		sb.append(node.state.killedNormalAgent0);
		sb.append(node.state.killedNormalAgent1);
		sb.append(node.state.killedNormalAgent2);
		sb.append(node.state.killedNormalAgent3);
		
	
		
		String encode=sb.toString();
		return  encode;
		
		
	}	
	
	
	
	
	
	
	
}
