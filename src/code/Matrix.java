package code;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Stack;

//import javax.jws.soap.InitParam;

public class Matrix extends SearchProblem{
	byte numOfHostages;
	
	public Matrix(byte numOfHostages, State initialState, byte numOperators) {
		super(initialState, numOperators);
		this.numOfHostages=numOfHostages; 
	}
	
	public int calculatePathCost(Node node) {
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

	// For eliminating repeated states
	private static HashMap<String,Short> encodedNodes = new HashMap<String,Short>();
	
	//For genGrid
	private static ArrayList<Integer> availableCells;
	
	// For printing the solution
	private static short DeathCounter=0;
	private static short KillCounter=0;
	private static int nodesExpanded = 0;
	
	// Used in buildPath to know that we are in the start node for building the path
	public static  boolean leafnode;
	
	// Filled by genGrid
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
		
		// Create a Priority Queue
				// 7.4.5.6
		
	
		
		String grid = genGrid();
		System.out.println(grid);
	}
	
	
	private static String genGrid() {
		m = (byte)random(5,15);
		n = (byte)random(5,15);
		c = (byte)random(1,4);
		

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
	

//	public static byte m,n,c;
//	public static byte initNeoX, initNeoY, telephoneX, telephoneY;
//	public static byte hostagesCount;
//	public static byte[] hostagesHealth;
//	
//	// location of hostage i is saves at cells 2*i and 2*i+1
//	public static byte[] hostagesLocation;
//	
//	public static byte[] pillsLocation;
//	
//	public static byte[] padsStartLocation;
//	public static byte[] padsEndLocation;
//	
//	public static byte[] agentsLocation;
	//"5,5;2;3,4;1,2;0,3,1,4;2,3;4,4,0,2,0,2,4,4;2,2,91,2,4,62";
	 //  m,n;c;neox,neoy;telx,tely;AgentX,AgentY;PillX,PillY;PadStartX,PadStartY,FinishX,FinishY,HostageX,HostageY,Damage
	
	
	
	//";0,3,1,4;2,3;4,4,0,2,0,2,4,4";
	public static void gridSplicer(String grid)
	{
		String[] gridArray= grid.split(";");
		m=Byte.parseByte(gridArray[0].split(",")[0]);
		n=Byte.parseByte(gridArray[0].split(",")[1]);
		c=Byte.parseByte(gridArray[1]);
		
		initNeoX=Byte.parseByte(gridArray[2].split(",")[0]);
		initNeoY=Byte.parseByte(gridArray[2].split(",")[1]);
		
		telephoneX=Byte.parseByte(gridArray[3].split(",")[0]);
		telephoneY=Byte.parseByte(gridArray[3].split(",")[1]);
		
		String[] Info=(gridArray[7].split(","));
		hostagesCount=(byte)(Info.length/3);
		hostagesHealth=new byte[hostagesCount];
		hostagesLocation= new byte [hostagesCount*2];		
		
		for(byte i =0; i<hostagesCount;i++)
		{
			hostagesLocation[2*i]=Byte.parseByte(Info[(i*3)]);
			hostagesLocation[(2*i)+1]=Byte.parseByte(Info[(i*3)+1]);
			hostagesHealth[i]=Byte.parseByte(Info[(i*3)+2]);
		}
		
		Info=(gridArray[5].split(","));
		pillsLocation= new byte [Info.length];		
		for(byte i =0; i<Info.length;i+=2)
		{
			pillsLocation[i]=Byte.parseByte(Info[(i)]);
			pillsLocation[(i)+1]=Byte.parseByte(Info[i+1]);
		}
		
		Info=(gridArray[6].split(","));
		padsStartLocation= new byte [Info.length/4];		
		padsEndLocation=new byte[Info.length/4];
		for(byte i =0; i<Info.length;i+=8)
		{
			padsStartLocation[i/4]=Byte.parseByte(Info[(i)]);
			padsStartLocation[(i/4)+1]=Byte.parseByte(Info[i+1]);
			
			padsEndLocation[i/4]=Byte.parseByte(Info[(i)+2]);
			padsEndLocation[(i/4)+1]=Byte.parseByte(Info[i+3]);
		}
		
		Info=(gridArray[4].split(","));
		agentsLocation= new byte [Info.length];		
		for(byte i =0; i<Info.length;i+=2)
		{
			agentsLocation[i]=(byte)Integer.parseInt(Info[(i)]);
			agentsLocation[(i)+1]=(byte)Integer.parseInt(Info[i+1]);
		}
		
		
	}
	
	
	public static String solve(String grid, String strategy, boolean visualize) {

		encodedNodes = new HashMap<String,Short>();
		DeathCounter=0;
		leafnode=true;
		KillCounter=0;
		nodesExpanded = 0;
		
		gridSplicer(grid);
		State initState = new State(initNeoX, initNeoY, (short)0, (short)0, (short)0, (short)0, 0l, 0l, 0l, 0, hostagesHealth, (byte)0,(short)0);
		Matrix matrix = new Matrix(hostagesCount, initState, (byte)8);
		String plan = generalSearch(matrix, strategy,visualize);
		if(plan.equals("Fail"))	
			return "No Solution";
		else
			return plan+";"+DeathCounter+";"+KillCounter+";"+(nodesExpanded+1);
	}
	

	
	public static String generalSearch(Matrix problem, String strategy,boolean visualize) {
		Node initNode = new Node(problem.initialState, null, (byte)-1, (short)0, 0);
		encodedNodes.put(encode(initNode),sumHealthes(initNode));
		if(strategy.equals("BF")) {
			return bfs(initNode, problem,visualize);
		}
		if(strategy.equals("DF")) {
			return dfs(initNode, problem,visualize);
		}
		
		if(strategy.equals("UC")) {
			return UniformCostSearch(initNode, problem,visualize);
		}
		
		if(strategy.equals("ID")) {
			return IterativeSearch(initNode, problem,visualize);
		}
		if(strategy.equals("GR1")) {
			return HeuristicSearch(0,initNode, problem,visualize);
		}
		if(strategy.equals("GR2")) {
			return HeuristicSearch(1,initNode, problem,visualize);
		}
		if(strategy.equals("AS1")) {
			return AStarSearch(0,initNode, problem,visualize);
		}
		
		if(strategy.equals("AS2")) {
			return AStarSearch(1,initNode, problem,visualize);
		}
		return "failure";
	}
	
	public static String bfs(Node initNode, Matrix problem,boolean visualize){
		PriorityQueue<Node> pq = new PriorityQueue<Node>((x,y)->(x.depth==y.depth? (y.operator-x.operator):(x.depth-y.depth)));// BFS
		pq.add(initNode);
		while(!pq.isEmpty()) 
		{
			Node currentNode = pq.poll();
			
			boolean isGoal = problem.goalTest(currentNode.state);
			if(isGoal)
				return buildPath(currentNode,visualize);
			
			LinkedList<Node> nodes = expand(problem, currentNode);

			for(Node node: nodes) {
				pq.add(node);
			}
			
			
		}
		return "Fail";
	}
	
	public static String dfs(Node initNode, Matrix problem,boolean visualize) {
		String sol =  dfs_up_to_level(initNode, problem, -1, visualize);
		return sol.split(" ")[0];
	}
	
	public static String dfs_up_to_level(Node initNode, Matrix problem, int maxDepth,boolean visualize) {
		int maxSofar = 0;
		PriorityQueue<Node> pq = new PriorityQueue<Node>((x,y)->(x.depth==y.depth? (y.operator-x.operator):(y.depth-x.depth)));// DFS
		pq.add(initNode);
		while(!pq.isEmpty()) 
		{
			Node currentNode = pq.poll();
			maxSofar = Math.max(maxSofar, currentNode.depth);
			boolean isGoal = problem.goalTest(currentNode.state);
			if(isGoal)
				return buildPath(currentNode,visualize) +" "+maxSofar;
			
			if(maxDepth == -1 || currentNode.depth < maxDepth) {
				
				LinkedList<Node> nodes = expand(problem, currentNode);
				
				for(Node node: nodes) {
					pq.add(node);
				}
			}
		}
		return "Fail"+" "+maxSofar;
		
	}

	private static LinkedList<Node> expand(Matrix problem, Node currentNode) {
	    
		//up, down, left, right, carry, drop, takePill, kill , and fly
		//0    1      2     3     4      5        6      7          8
		
	    LinkedList<Node> expandedNodes = new LinkedList<Node>();
	    
	    if(currentNode.state.neoHealth>=100)
	    {
	    	return expandedNodes;
	    }
	    	
	    
	    Node triedNode=null;
	    String encodedNode="";
	    
	    // Try carry
	    triedNode = pickUpAgent(problem, currentNode);
	    if(triedNode!=null)
	    {	
	    	encodedNode=encode(triedNode);
	    	short sumHealthes = sumHealthes(triedNode);
	    	if(! encodedNodes.containsKey(encodedNode))
	    	{
	    		encodedNodes.put(encodedNode,sumHealthes);
	    		expandedNodes.add(triedNode);
	    	}
	    	else
	    	{
	    		short oldSumHealthes = encodedNodes.get(encodedNode);
	    		if(sumHealthes < oldSumHealthes)
	    		{
	    			encodedNodes.put(encodedNode, sumHealthes);
	    			expandedNodes.add(triedNode);
	    		}
	    	}
	    }
	    // Try drop
	    triedNode = dropAllHostages(problem, currentNode);
	    
	    if(triedNode!=null)
	    {	
	    	encodedNode=encode(triedNode);
	    	short sumHealthes = sumHealthes(triedNode);
	    	if(! encodedNodes.containsKey(encodedNode) )
	    	{
	    		encodedNodes.put(encodedNode,sumHealthes);
	    		expandedNodes.add(triedNode);
	    	}
	    	else
	    	{
	    		short oldSumHealthes = encodedNodes.get(encodedNode);
	    		if(sumHealthes < oldSumHealthes)
	    		{
	    			encodedNodes.put(encodedNode, sumHealthes);
	    			expandedNodes.add(triedNode);
	    		}
	    	}
	    }
	    // Try takepill
	    triedNode = takePill(problem, currentNode);
	    if(triedNode!=null)
	    {	
	    	encodedNode=encode(triedNode);
	    	short sumHealthes = sumHealthes(triedNode);
	    	if(! encodedNodes.containsKey(encodedNode) )
	    	{
	    		encodedNodes.put(encodedNode,sumHealthes);
	    		expandedNodes.add(triedNode);
	    	}
	    	else
	    	{
	    		short oldSumHealthes = encodedNodes.get(encodedNode);
	    		if(sumHealthes < oldSumHealthes)
	    		{
	    			encodedNodes.put(encodedNode, sumHealthes);
	    			expandedNodes.add(triedNode);
	    		}
	    	}
	    }

	    // Try kill Around
	    triedNode = kill(problem, currentNode);
 		//if the node is not null we check if it's repeated state or not
	    if(triedNode!=null)
	    {	
	    	encodedNode=encode(triedNode);
	    	short sumHealthes = sumHealthes(triedNode);
	    	if(! encodedNodes.containsKey(encodedNode) )
	    	{
	    		encodedNodes.put(encodedNode,sumHealthes);
	    		expandedNodes.add(triedNode);
	    	}
	    	else
	    	{
	    		short oldSumHealthes = encodedNodes.get(encodedNode);
	    		if(sumHealthes < oldSumHealthes)
	    		{
	    			encodedNodes.put(encodedNode, sumHealthes);
	    			expandedNodes.add(triedNode);
	    		}
	    	}
	    }
	    
	    
		//Try fly if parent was not fly
	    if(currentNode.operator!=8)
	    {
	    	triedNode = fly(problem, currentNode);
	 		//if the node is not null we check if it's repeated state or not
	    	if(triedNode!=null)
		    {	
		    	encodedNode=encode(triedNode);
		    	short sumHealthes = sumHealthes(triedNode);
		    	if(! encodedNodes.containsKey(encodedNode) )
		    	{
		    		encodedNodes.put(encodedNode,sumHealthes);
		    		expandedNodes.add(triedNode);
		    	}
		    	else
		    	{
		    		short oldSumHealthes = encodedNodes.get(encodedNode);
		    		if(sumHealthes < oldSumHealthes)
		    		{
		    			encodedNodes.put(encodedNode, sumHealthes);
		    			expandedNodes.add(triedNode);
		    		}
		    	}
		    }
	    }
	    
	  
	    
	  //Try move up if parent was not move down
	    if(currentNode.operator!=1)
	    {
	    	triedNode=move(problem, 0, currentNode);
	    	//if the node is not null we check if it's repeated state or not
	    	if(triedNode!=null)
		    {	
		    	encodedNode=encode(triedNode);
		    	short sumHealthes = sumHealthes(triedNode);
		    	if(! encodedNodes.containsKey(encodedNode) )
		    	{
		    		encodedNodes.put(encodedNode,sumHealthes);
		    		expandedNodes.add(triedNode);
		    	}
		    	else
		    	{
		    		short oldSumHealthes = encodedNodes.get(encodedNode);
		    		if(sumHealthes < oldSumHealthes)
		    		{
		    			encodedNodes.put(encodedNode, sumHealthes);
		    			expandedNodes.add(triedNode);
		    		}
		    	}
		    }
	    }
	    //Try move down if parent was not move up 
	    if(currentNode.operator!=0)
	    {
	    	triedNode=move(problem, 1, currentNode);
	    	//if the node is not null we check if it's repeated state or not
	    	if(triedNode!=null)
		    {	
		    	encodedNode=encode(triedNode);
		    	short sumHealthes = sumHealthes(triedNode);
		    	if(! encodedNodes.containsKey(encodedNode) )
		    	{
		    		encodedNodes.put(encodedNode,sumHealthes);
		    		expandedNodes.add(triedNode);
		    	}
		    	else
		    	{
		    		short oldSumHealthes = encodedNodes.get(encodedNode);
		    		if(sumHealthes < oldSumHealthes)
		    		{
		    			encodedNodes.put(encodedNode, sumHealthes);
		    			expandedNodes.add(triedNode);
		    		}
		    	}
		    }
	    }
	    // Try move left if parent was not move right
	    if(currentNode.operator!=3)
	    {
	    	triedNode=move(problem, 2, currentNode);
		    //if the node is not null we check if it's repeated state or not
	    	if(triedNode!=null)
		    {	
		    	encodedNode=encode(triedNode);
		    	short sumHealthes = sumHealthes(triedNode);
		    	if(! encodedNodes.containsKey(encodedNode) )
		    	{
		    		encodedNodes.put(encodedNode,sumHealthes);
		    		expandedNodes.add(triedNode);
		    	}
		    	else
		    	{
		    		short oldSumHealthes = encodedNodes.get(encodedNode);
		    		if(sumHealthes < oldSumHealthes)
		    		{
		    			encodedNodes.put(encodedNode, sumHealthes);
		    			expandedNodes.add(triedNode);
		    		}
		    	}
		    }
	    }
	    // Try move right if parent was not move left
	    if(currentNode.operator!=2)
	    {
	    	triedNode=move(problem, 3, currentNode);
	    	//if the node is not null we check if it's repeated state or not
	    	if(triedNode!=null)
		    {	
		    	encodedNode=encode(triedNode);
		    	short sumHealthes = sumHealthes(triedNode);
		    	if(! encodedNodes.containsKey(encodedNode) )
		    	{
		    		encodedNodes.put(encodedNode,sumHealthes);
		    		expandedNodes.add(triedNode);
		    	}
		    	else
		    	{
		    		short oldSumHealthes = encodedNodes.get(encodedNode);
		    		if(sumHealthes < oldSumHealthes)
		    		{
		    			encodedNodes.put(encodedNode, sumHealthes);
		    			expandedNodes.add(triedNode);
		    		}
		    	}
		    }
	    }
	    
	    nodesExpanded += expandedNodes.size();
		return expandedNodes;
	}
	
	public static Node move(Matrix problem, int actionId, Node currentNode)
	{
		byte neoX =currentNode.state.neoX;
	    byte neoY =currentNode.state.neoY;
		if(actionId == 0)
		{
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
		    	Node newNode = new Node(newState, currentNode, (byte)0, (short)(currentNode.depth+1), 0);
		    	newNode.pathCost = problem.calculatePathCost(newNode);
		    	return newNode;
		    	
		    }
			
		}
		else if(actionId == 1)
		{
			//TryDown
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
				Node newNode = new Node(newState, currentNode, (byte)1, (short)(currentNode.depth+1), 0);
				newNode.pathCost = problem.calculatePathCost(newNode);
				return newNode;
			}
		}
		else if(actionId == 2)
		{
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
				Node newNode = new Node(newState, currentNode, (byte)2, (short)(currentNode.depth+1), 0);
				newNode.pathCost = problem.calculatePathCost(newNode);
				return newNode;
			}
		}
		else if(actionId == 3)
		{
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
				Node newNode = new Node(newState, currentNode, (byte)3, (short)(currentNode.depth+1), 0);
				newNode.pathCost = problem.calculatePathCost(newNode);
				return newNode;
			}
		}
		return null;
	}
	
	public static Node kill(Matrix problem, Node currentNode)
	{
		
		byte neoX =currentNode.state.neoX;
	    byte neoY =currentNode.state.neoY;
	    
	    
	    // check if I am at a cell where there is an agent of damage 98 or 99
	    for(byte i=(byte)0;i<hostagesCount;i++)
		{
			// if not at that location
			if(neoX != hostagesLocation[2*i] || neoY != hostagesLocation[2*i+1])
				continue;
			// if it was moved before (in the moved hostages bitmask)
			if((currentNode.state.movedHostages & (1<<i)) != 0)
				break;
			if(currentNode.state.hostagesHealth[i] == 98 || currentNode.state.hostagesHealth[i] == 99)
				return null;
			else
				break;
		}
	    
	    
	    
	    boolean didkill=false;
	    byte count = (byte)0; // To count the killed agents by this kill action
	    
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
		State newState = timeStep(currentNode.state);
		
		if(neoX-1 >= 0 && agentAt >= 0)
		{
		    if(!didkill)
		    {
		    	newState.neoHealth = (byte)Math.min(100, newState.neoHealth + 20);
		    	didkill=true;
		    }
		    
		    count += (byte)1;
		    // if normal agent
		    if((agentAt & (1<<8)) == 0)
		    {
		    	if(agentAt < 64) // in the first bitmask (killedNormalAgent0)
				{
					newState.killedNormalAgent0 |= (1L<<agentAt);
				}
				else if(agentAt < 128)
				{
					agentAt -= (short)64;
					newState.killedNormalAgent1 |= (1L<<agentAt);
				}
				else if(agentAt < 192)
				{
					agentAt -= (short)128;
					newState.killedNormalAgent2 |= (1L<<agentAt);
				}	
				else
				{
					agentAt -= (short)192;
					newState.killedNormalAgent3 |= (1<<agentAt);
				}
		    }
		    else
		    {
		    	// negate 1<<8 then &
		    	agentAt = (short)(agentAt & ~(short)(1<<8));
		    	newState.killedTransHostages |= (1<<agentAt);
		    }
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
		    	

			if(!didkill)
			{
				newState.neoHealth = (byte)Math.min(100, newState.neoHealth + 20);
		    	didkill=true;
		    }
			count += (byte)1;	
		    // if normal agent
		    if((agentAt & (1<<8)) == 0)
		    {
		    	if(agentAt < 64) // in the first bitmask (killedNormalAgent0)
				{
		    		newState.killedNormalAgent0 |= (1L<<agentAt);
				}
				else if(agentAt < 128)
				{
					agentAt -= (short)64;
					newState.killedNormalAgent1 |= (1L<<agentAt);
				}
				else if(agentAt < 192)
				{
					agentAt -= (short)128;
					newState.killedNormalAgent2 |= (1L<<agentAt);
				}
				else
				{
					agentAt -= (short)192;
					newState.killedNormalAgent3 |= (1<<agentAt);
				}
		    }
		    else
		    {
		    	// negate 1<<8 then &
		    	agentAt = (short)(agentAt & ~(short)(1<<8));
		    	newState.killedTransHostages |= (1<<agentAt);
		    }

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
		    	

			 if(!didkill)
		    {
				newState.neoHealth = (byte)Math.min(100, newState.neoHealth + 20);
		    	didkill=true;
		    }
			 count += (byte)1;
		    // if normal agent
		    if((agentAt & (1<<8)) == 0)
		    {
		    	if(agentAt < 64) // in the first bitmask (killedNormalAgent0)
				{
					newState.killedNormalAgent0 |= (1L<<agentAt);
				}
				else if(agentAt < 128)
				{
					agentAt -= (short)64;
					newState.killedNormalAgent1 |= (1L<<agentAt);
				}
				else if(agentAt < 192)
				{
					agentAt -= (short)128;
					newState.killedNormalAgent2 |= (1L<<agentAt);
				}
				else
				{
					agentAt -= (short)192;
					newState.killedNormalAgent3 |= (1<<agentAt);
				}
		    }
		    else
		    {
		    	// negate 1<<8 then &
		    	agentAt = (short)(agentAt & ~(short)(1<<8));
		    	newState.killedTransHostages |= (1<<agentAt);
		    }
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
		   	
			if(!didkill)
		    {
				newState.neoHealth = (byte)Math.min(100, newState.neoHealth + 20);
		    	didkill=true;
		    }
			count += (byte)1;
		    // if normal agent
		    if((agentAt & (1<<8)) == 0)
		    {
		    	if(agentAt < 64) // in the first bitmask (killedNormalAgent0)
				{
					newState.killedNormalAgent0 |= (1L<<agentAt);
				}
				else if(agentAt < 128)
				{
					agentAt -= (short)64;
					newState.killedNormalAgent1 |= (1L<<agentAt);
				}
				else if(agentAt < 192)
				{
					agentAt -= (short)128;
					newState.killedNormalAgent2 |= (1L<<agentAt);
				}
				else
				{
					agentAt -= (short)192;
					newState.killedNormalAgent3 |= (1<<agentAt);
				}
	    	}
	    	else
	    	{
	    		// negate 1<<8 then &
	    		agentAt = (short)(agentAt & ~(short)(1<<8));
	    		newState.killedTransHostages |= (1<<agentAt);
	    	}
		}
		if(didkill)
		{
		    Node newNode = new Node(newState, currentNode, (byte)(6+count), (short)(currentNode.depth+1), 0);
			newNode.pathCost = problem.calculatePathCost(newNode);

			return newNode;
		}
	    return null;    
	}
	
	//  
	
	public static Node fly(Matrix problem, Node node) {
		
		short[] t=isTherePad(node.state.neoX, node.state.neoY);
		
			if(t[0]!=0) 
			{
				if(t[1]==0) 
				{
					State tempNode=timeStep(node.state);
					tempNode.neoX=padsEndLocation[t[2]];
					tempNode.neoY=padsEndLocation[t[2]+1];
					Node sNode=new Node(tempNode,node,(byte) 11,(short)(node.depth+1),0);
					sNode.pathCost=problem.calculatePathCost(sNode);
					return sNode;
				}
				else
				{
					State tempNode=timeStep(node.state);
					tempNode.neoX=padsStartLocation[t[2]];
					tempNode.neoY=padsStartLocation[t[2]+1];
					Node sNode=new Node(tempNode,node,(byte) 11,(short)(node.depth+1),0);
					sNode.pathCost=problem.calculatePathCost(sNode);

					return sNode;
				}
			}
			else 
			{
				return null;
			}
			
		}
		public static Node takePill(Matrix problem, Node node) {
			
			byte idx=isTherePill(node.state.neoX, node.state.neoY,node.state.pills);
			if(idx!=-1)
			{
				
				short pills=node.state.pills;
				pills|=(1<<idx);
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
						node.state.hostagesHealth.clone(),
						node.state.neoHealth,
						pills);
				for(byte i=(byte)0;i<node.state.hostagesHealth.length;i++)
				{
					if(newState.hostagesHealth[i] >= 100)
						continue;
					// we check first if the hostage was delivered or not , if it was then we dont change it health 
					//(in moved and not in currently carried)
					if(((newState.movedHostages & (1<<i)) !=0) && ((newState.currentlyCarriedHostages & (1<<i)) ==0))
						continue;
				
					//if the hostage turned to agent we don't change its damage
					if((newState.hostagesToAgents & (1<<i))==0)
						newState.hostagesHealth[i] = (byte)Math.max(0, newState.hostagesHealth[i]-20);
				
				}
				
				newState.neoHealth = (byte)Math.max(0, newState.neoHealth-20);
				
				Node sNode=new Node(newState,node,(byte)6,(short)(node.depth+1),0);
				sNode.pathCost=problem.calculatePathCost(sNode);
				return sNode;	
			}
			else
			{
				return null ;
			}
			
			
		}
		public static byte isTherePill(byte x,byte y,short mask) {
			for(int i=0;i<pillsLocation.length/2;i++)
			{				
				if(x==pillsLocation[2*i]&&y==pillsLocation[(2*i)+1])
				{
					if((mask & (1<<i))==0)
					{
						return (byte) i;
					}
				}
			}
			return (byte)(-1);
		}
		
		//check if there pad at location
		public static short[] isTherePad(byte x,byte y) {
			short s[]= {0,0,0};

			for(short i=0;i<padsStartLocation.length;i=(short)(i+2))
			{
				if(x==padsStartLocation[i]&&y==padsStartLocation[i+1])
				{
					s[0]=1;
					s[1]=0;
					s[2]=i;
					return s;
				}
				else if (x==padsEndLocation[i]&&y==padsEndLocation[i+1])
				{
					s[0]=1;
					s[1]=1;
					s[2]=i; return s;
				}
			}
			return s;
		}
		
		
		
		
		
		///////////////////////////////////////////////////////////////////////////////
	private static Node dropAllHostages(Matrix problem, Node currentNode) {
		if(telephoneX != currentNode.state.neoX)
			return null;
		if(telephoneY != currentNode.state.neoY)
			return null;
		
		
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
				currentNode.state.neoHealth,
				currentNode.state.pills);
		
		
		State newState = timeStep(intermediateState);
		Node newNode = new Node(newState, currentNode, (byte)5, (short)(currentNode.depth+1), 0);
		newNode.pathCost = problem.calculatePathCost(newNode);
		
		return newNode;
		
	}
	public static Node pickUpAgent(Matrix problem, Node currentNode) {
		
		// check if carrying max, if so return null
		int carriedNow = 0;
		for(byte i=(byte)0;i<currentNode.state.hostagesHealth.length;i++)
			if((currentNode.state.currentlyCarriedHostages & (1<<i)) != 0)
				carriedNow++;
		if(carriedNow >= c)
			return null;
		
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
				currState.neoHealth,
				currState.pills);
				
		State newState = timeStep(intermediateState);
		Node newNode = new Node(newState, currentNode, (byte)4, (short)(currentNode.depth+1), 0);
		newNode.pathCost = problem.calculatePathCost(newNode);
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
				if(hostagesHealth[i] >= 100)
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
		
		for(short i=(short)0;i<agentsLocation.length;i+=(short)2)
		{
			
			if(x != agentsLocation[i] || y != agentsLocation[i+1])
			{
				continue;
			}
			
			short idx = (short)(i/2);
			
			if(idx < 64) // in the first bitmask (killedNormalAgent0)
			{
				// if not killed
				if( (killedNormalAgent0 & (1L<<idx)) == 0 )
				{
					return (short)idx;
				}
			}
			else if(idx < 128)
			{
				idx -= (short)64;
				if( (killedNormalAgent1 & (1L<<idx)) == 0 )
				{
					return (short)(idx+64);
				}
			}
			else if(idx < 192)
			{
				idx -= (short)128;
				if( (killedNormalAgent2 & (1L<<idx)) == 0 )
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
					currState.neoHealth,
					currState.pills);
		
		for(byte i=(byte)0;i<currState.hostagesHealth.length;i++)
		{
			 // we check first if the hostage was delivered or not , if it was then we dont change it health 
			 //(in moved and not in currently carried)
			if(((newState.movedHostages & ( ((short)1)<<i) ) !=0) && ((newState.currentlyCarriedHostages & ( ((short)1)<<i) ) ==0))
				continue;
			
			newState.hostagesHealth[i] = (byte)Math.min(100, newState.hostagesHealth[i]+2);
			//if the hostage was not rescued and it health reached 100 
			// we turn the hostage into agent if and only if it was not carried at the moment
			if(newState.hostagesHealth[i]>=100)
			{	
				if((newState.currentlyCarriedHostages & (1<<i)) ==0)				
					newState.hostagesToAgents |= (1<<i);
			}
				
		}
		return newState;
	}
	

	public static Stack<Node> Plan =new Stack<>();


	private static void visualize() {
		String [] [] Grid=new String[n][m];
		System.out.println("");

		while (!Plan.isEmpty()) {
			
			for(int i=0;i<Grid.length;i++) {
				for(int j=0;j<Grid[0].length;j++) {
					Grid[i][j]=" E   ";
				}
			}
			int k=0;
			
			
			Node grid =Plan.pop();
			
			// put pills on place

			for(int i=0;i<pillsLocation.length-1;i+=2) {
				if((grid.state.pills&(1<<k))==0) { // The pill is not taken
					Grid[pillsLocation[i]][pillsLocation[i+1]]=" P   ";	
				}
				k++;
			}
			k=0;
			// put pads on place
			for(int i=0;i<padsStartLocation.length-1;i+=2) {
				
				Grid[padsStartLocation[i]][padsStartLocation[i+1]]=" F"+k;
				k++;
			}
			k=0;
			for(int i=0;i<padsEndLocation.length-1;i+=2) {
				Grid[padsEndLocation[i]][padsEndLocation[i+1]]=" F"+k;
				k++;
			}
			// put all agents on place
			k=0;
			for(int i=0;i<agentsLocation.length-1;i+=2) {
				boolean putAgent=true;
				int a=(int)k/64;
				int b=k%64;
				if(a==0&&!((grid.state.killedNormalAgent0&(1L<<b))!=0)) // if it was killed
					putAgent=false;
				if(a==1&&!((grid.state.killedNormalAgent1&(1L<<b))!=0))
					putAgent=false;
				if(a==2&&!((grid.state.killedNormalAgent2&(1L<<b))!=0))
					putAgent=false;
				if(a==3&&!((grid.state.killedNormalAgent3&(1<<b))!=0))
					putAgent=false;
					
				if(putAgent) { // means this agent not killed
					Grid[agentsLocation[i]][agentsLocation[i+1]]=" A ";
				}
				k++;
			}
			
			k=0;
			// put hostages on place
			for(int i=0;i<hostagesLocation.length-1;i+=2) {
				if(grid.state.hostagesHealth[i/2]<100&&(grid.state.movedHostages&1<<(i/2))==0) {
					Grid[hostagesLocation[i]][hostagesLocation[i+1]]=" H"+(i/2);
				}
			}
			// put hostages changed to agents  on place
			for(int i=0;i<hostagesLocation.length-1;i+=2) {
				if(grid.state.hostagesHealth[i/2]==100  // if it is dead
						&&
				(grid.state.movedHostages&1<<(i/2))==0 // and not moved
						&&
				(grid.state.killedTransHostages&1<<(i/2))==0 // and not killed
				
				)
				{
					Grid[hostagesLocation[i]][hostagesLocation[i+1]]=" M"+(i/2);
				}
			}
			// put telephone on place
			Grid[telephoneX][telephoneY]=" T ";
			
			
			// put neo on place
			Grid[grid.state.neoX][grid.state.neoY]=" N ";
			
			
			
			for(int i=0;i<Grid.length;i++) {
				for(int j=0;j<Grid[0].length;j++) {
					System.out.print(Grid[i][j]);
				}
				System.out.println("");
			}
			System.out.println("---------------------------");

		}

	}
	private static String buildPath(Node currentNode ,boolean visualize) {
		if(currentNode.parent ==null) {
			Plan.add(currentNode);
			if(visualize) {
				visualize();

			}
			else {
				Plan.clear();
			}
			return "";
		}
		if(leafnode)
		{
			killCount(currentNode);
			deathCount(currentNode);
			leafnode=false;
		}

			
			if(currentNode.parent.parent==null) {
				Plan.add(currentNode);
				return buildPath(currentNode.parent,visualize)+currentNode.opString;

			}
			else 
			{
				Plan.add(currentNode);
				return buildPath(currentNode.parent,visualize)+","+currentNode.opString;
			}
	}
	
	
	private static void deathCount(Node currentNode){
		
		for(int i=0;i<hostagesCount;i++)
		{
			if(currentNode.state.hostagesHealth[i] >= 100)
			{
				DeathCounter++;
			}
		}
	}
	
	private static void killCount(Node currentNode)
	{
		for (byte i =0;i<64;i++)
		{
			if((currentNode.state.killedNormalAgent0 & (1L<<i))!=0)
			{
				KillCounter++;
			}
			if((currentNode.state.killedNormalAgent1 & (1L<<i))!=0)
			{
				KillCounter++;
			}
			if((currentNode.state.killedNormalAgent2 & (1L<<i))!=0)
			{
				KillCounter++;
			}
			if(i<32)
			{
				if((currentNode.state.killedNormalAgent3 & (1<<i))!=0)
				{
					KillCounter++;
				}
				if(i<16)
				{
					if((currentNode.state.killedTransHostages & (1<<i))!=0)
					{
						KillCounter++;
					}
				}
			}	
			
		}	

		
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
		sb.append(",");
		sb.append(node.state.neoY);
		sb.append(",");
		sb.append(node.state.movedHostages);
		sb.append(",");
		sb.append(node.state.currentlyCarriedHostages);
		sb.append(",");
		sb.append(node.state.hostagesToAgents);
		sb.append(",");
		sb.append(node.state.killedTransHostages);
		sb.append(",");
		sb.append(node.state.pills);
		sb.append(",");
		sb.append(node.state.killedNormalAgent0);
		sb.append(",");
		sb.append(node.state.killedNormalAgent1);
		sb.append(",");
		sb.append(node.state.killedNormalAgent2);
		sb.append(",");
		sb.append(node.state.killedNormalAgent3);

	
		
		String encode=sb.toString();
		return  encode;
		
		
	}	
	
	
	
	public static String AStarSearch(int idx,Node initNode, Matrix problem,boolean visualize){
		PriorityQueue<Node> pq = new PriorityQueue<Node>((x,y)->( (HeuristicFunction(idx,x, problem)+x.pathCost)-(HeuristicFunction(idx,y, problem)+y.pathCost) ));// A*
		pq.add(initNode);
		while(!pq.isEmpty()) 
		{
			Node currentNode = (Node) pq.poll();
			
			try {
				Thread.sleep(0);
			}
			catch(Exception e) {
				
			}
			boolean isGoal = problem.goalTest(currentNode.state);
			if(isGoal)
				return buildPath(currentNode,visualize);
			
			LinkedList<Node> nodes = expand(problem, currentNode);

			for(Node node: nodes) {
				pq.add(node);
			}
			
			
		}
		return "Fail";
	}	
	
	
	public static String UniformCostSearch(Node initNode, Matrix problem,boolean visualize){
		
		
		PriorityQueue<Node> pq = new PriorityQueue<Node>((x,y)->(x.pathCost-y.pathCost));// UCS
		pq.add(initNode);
		while(!pq.isEmpty()) 
		{
			Node currentNode = (Node) pq.poll();

			
			boolean isGoal = problem.goalTest(currentNode.state);
			if(isGoal)
				return buildPath(currentNode,visualize);
			
			LinkedList<Node> nodes = expand(problem, currentNode);

			for(Node node: nodes) {
				pq.add(node);
			}
			
			
		}
		return "Fail";
	}	
	
	
	
	public static String IterativeSearch(Node initNode, Matrix problem, boolean visualize){
		int currDepth = 0;
		int totNodesExpanded = 0;
		do {
			String[] solPair =  dfs_up_to_level(initNode, problem, currDepth,visualize).split(" ");
			String answer = solPair[0];
			if(!answer.equals("Fail"))
			{
				nodesExpanded += totNodesExpanded;
				return answer;
			}
			
			if(currDepth > Integer.parseInt(solPair[1]))
			{
				nodesExpanded = totNodesExpanded;
				return "Fail";
			}
			
			currDepth++;
			totNodesExpanded += nodesExpanded;
			nodesExpanded = 0;
			encodedNodes = new HashMap<String, Short>();
		}while(true);
	}	
	public static String HeuristicSearch(int idx,Node initNode, Matrix problem,boolean visualize){
		PriorityQueue<Node> pq = new PriorityQueue<Node>((x,y)->( HeuristicFunction(idx,x, problem)-HeuristicFunction(idx,y, problem) ));// A*
		pq.add(initNode);
		while(!pq.isEmpty()) 
		{
			Node currentNode = (Node) pq.poll();
			
			try {
				Thread.sleep(0);
			}
			catch(Exception e) {
				
			}
			boolean isGoal = problem.goalTest(currentNode.state);
			if(isGoal)
				return buildPath(currentNode,visualize);
			
			LinkedList<Node> nodes = expand(problem, currentNode);

			for(Node node: nodes) {
				pq.add(node);
			}
			
			
		}
		return "Fail";
	}	
	
	public static int HeuristicFunction(int idx,Node node, SearchProblem problem){
		return idx==0?Heuristic0(node, problem):Heuristic00(node, problem);
	}
	/**
	 * 
	 * @param node the node to predicts its heuristic value (estimate for the cost from it to the nearest goal)
	 * @param problem
	 * @return
	 * Our assumptions for calculating this heuristic:
	 * willDieForSure => the hostages that will die in the next time step and you cannot save them (you are not at the TB nor you are at a pill)
	 * minRequiredActionsCount => the min required actions in the best case is that:
	 * 	we have all the not yet moved hostages on one row and you take them one by one till the TB, you at least need two actions for each one of them
	 */
	public static int Heuristic0(Node node, SearchProblem problem){
		
		// To ensure the centering property for the heuristic function (h(goal) = 0)
		if(problem.goalTest(node.state))
		{
			return 0;
		}
		int willDieForSure = 0;
		if((node.state.neoX != telephoneX || node.state.neoY != telephoneY) 
			&& isTherePill(node.state.neoX, node.state.neoX, node.state.pills) == -1)
		{
			// Means that Neo is neither at the telephone booth nor at a cell with a pill
			// This means that if a hostage will die in one move, he will not be able to save them
			for(byte i=(byte)0;i<hostagesCount;i++)
			{
				// if the hostage was delivered
				// i.e. in movedHostages but not in currentlyCarriedHostages
				// then we should not consider it
				if( ((node.state.movedHostages & (1<<i)) != 0) && ((node.state.currentlyCarriedHostages & (1<<i)) == 0) )
					continue;
				// else if the hostage will die the next time step
				if(node.state.hostagesHealth[i] == 98 || node.state.hostagesHealth[i] == 99)
					willDieForSure++;
			}
		}
		
		// In the best grid set up, what will be the minimum actions count required to reach a goal state
		// Therefore, for every not moved hostage
		// if not mutated agent, we will need at least two actions
		// else we can at best kill 3 of them by one kill action
		int minRequiredActionsCount = 0;
		int mutatedCount =0;
		for(byte i=(byte)0;i<hostagesCount;i++)
		{
			if((node.state.movedHostages & (1<<i)) == 0)
			{
				// Not moved before
				// if not mutated agent
				if(node.state.hostagesHealth[i]<100)
				{
					minRequiredActionsCount+=2;
				}
				else
				{
					mutatedCount++;
				}
			}
		}
		minRequiredActionsCount += (mutatedCount/3);
		return minRequiredActionsCount + willDieForSure*250000;
	}
	/**
	 * @param node
	 * @param problem
	 * @return
	 */
	public static int Heuristic00(Node node, SearchProblem problem) {		
		// To ensure the centering property for the heuristic function (h(goal) = 0)
		if(problem.goalTest(node.state))
		{
			return 0;
		}
		
		int minDistanceToPill = Integer.MAX_VALUE;
		for(byte i=(byte)0;i<pillsLocation.length;i+=2)
		{
			// if this pill already taken then do not consider it
			if((node.state.pills & (1<<i))!=0)
			{
				continue;
			}
			minDistanceToPill =
				Math.min(minDistanceToPill,
				  bestPath(node.state.neoX, node.state.neoY, pillsLocation[i], pillsLocation[i+1]));
		}
		int willDieForSure = 0;
		for(byte i=(byte)0;i<hostagesCount;i++)
		{	
			// if not moved yet and still alive
			if((node.state.movedHostages & (1<<i)) == 0  && node.state.hostagesHealth[i] < 100)
			{	
				// The following is the shortest path to pass by the i-th hostage
				// (We use the pads freely, which is any pad is connected to all other pads)
				int currHostageBestDistance =
						bestPath(node.state.neoX, node.state.neoY, hostagesLocation[2*i], hostagesLocation[2*i+1])
					   +bestPath(hostagesLocation[2*i], hostagesLocation[2*i+1], telephoneX, telephoneY);
				
				int actionsCountToDie = (100-node.state.hostagesHealth[i]+1)/2;
				
				int requiredToSave = Math.min(minDistanceToPill, currHostageBestDistance);
				if(actionsCountToDie < requiredToSave)
				{
					willDieForSure++;
				}
			}
		}
		
		int maxBestDistance = 0;
		// best distance for the farthest hostage that I will deliver to the T booth
		
		for(byte i=(byte)0;i<hostagesCount;i++)
			if((node.state.movedHostages & (1<<i)) == 0)
			{

				int currHostageBestDistance =
						bestPath(node.state.neoX, node.state.neoY, hostagesLocation[2*i], hostagesLocation[2*i+1])
					   +bestPath(hostagesLocation[2*i], hostagesLocation[2*i+1], telephoneX, telephoneY);
				maxBestDistance = Math.max(maxBestDistance, currHostageBestDistance);
			}
		return maxBestDistance + willDieForSure*250000; 
		// + remainingCount because each other hostage will need at least to carry it
		
	}
	public static int bestPath(int x1, int y1, int x2, int y2) {
		// Assuming we can talk only one magical pad to try to shorten the distance from P1 to P2 
		int bestDistance = manhattan(x1, y1, x2, y2);
		
		bestDistance = Math.min(bestDistance, distanceUsingPad(x1, y1, x2, y2));
		
		return bestDistance;
	}
	
	public static int distanceUsingPad(int x1, int y1, int x2, int y2) {
		int d1 = Integer.MAX_VALUE;
		int d2 = Integer.MAX_VALUE;

		for(int padIdx = 0; padIdx< padsStartLocation.length;padIdx+=2) {
			int padStartX = padsStartLocation[padIdx];
			int padStartY = padsStartLocation[padIdx +1];
			d1 = Math.min(d1, manhattan(x1, y1, padStartX, padStartY));
			d2 = Math.min(d2, manhattan(x2, y2, padStartX, padStartY));
		}
		for(int padIdx = 0; padIdx< padsEndLocation.length;padIdx+=2) {
			int padEndX = padsEndLocation[padIdx];
			int padEndY = padsEndLocation[padIdx +1];
			d1 = Math.min(d1, manhattan(x1, y1, padEndX, padEndY));
			d2 = Math.min(d2, manhattan(x2, y2, padEndX, padEndY));
		}
		
		return d1 + d2;
	}

	public static int manhattan(int x1, int y1, int x2, int y2) {
		return Math.abs(x1-x2) + Math.abs(y1-y2); 
	}
	
	private static short sumHealthes(Node node) {
		short sum=(byte)0;
		for(int i=0;i<hostagesCount;i++)
		{
			sum += node.state.hostagesHealth[i];
		}
		return sum;
	}

}
