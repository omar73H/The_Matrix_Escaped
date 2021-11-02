import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class The_Matrix_Solver {
	
	private static ArrayList<Integer> availableCells;
	
	public static byte m,n,c;
	public static byte initNeoX, initNeoY, telephoneX, telephoneY;
	public static byte hostagesCount;
	public static String[] hostagesInformation;
	
	public static byte[] hostagesHealth;
	
	public static String[] pillsInformation;
	
	public static String[] padsInformation;
	
	public static String[] agentsInformation;
	// "0,0,1,1" "1,1,2,2"
	
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
		hostagesInformation = new String[hostagesCount];
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
			hostagesInformation[i] = hostageX+","+hostageY;
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
		agentsInformation = new String[agentsCount];
		
		StringBuilder agentsInfo = new StringBuilder();
		//Positions of pads;
		for(int i=0;i<agentsCount;i++)
		{
			int agentPos = randomCell();
			int agentX = agentPos/n;
			int agentY = agentPos%n;
			
			//########
			grid[agentX][agentY] = "A"+i;
			
			if(i>0)
				agentsInfo.append(",");
			
			agentsInfo.append(agentX+","+agentY);
			agentsInformation[i] = agentX+","+agentY;
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
		//up, down, left, right, carry, drop, takePill, killR , killL , killU , killD	, and fly
		//0    1      2     3     4      5        6      7         8
		
	    LinkedList<Node> expandedNodes = new LinkedList<Node>();
	    
	    // Try Up
	    if(neoX-1 >= 0 && !agentAt((byte) (neoX-1),
	    						  neoY,
	    						  currentNode.state.hostagesHealth,
	    						  currentNode.state.hostagesToAgents,
	    						  currentNode.state.killedTransHostages,
	    						  currentNode.state.killedNormalAgent0,
	    						  currentNode.state.killedNormalAgent1,
	    						  currentNode.state.killedNormalAgent2,
	    						  currentNode.state.killedNormalAgent3))
	    {
//	    	if()
	    }
	    
	    Node n = fly(currentNode);
		 
		return null;
	}
	
	public static Node fly(Node node) {
		for(int i=0; i<padsInformation.length;i++) {
			
		}
		return null;
	}
	
	private static boolean agentAt(byte x,
								   byte y,
								   byte[] hostagesHealth,
								   short hostagesToAgents,
								   short killedTransHostages,
								   long killedNormalAgent0,
								   long killedNormalAgent1,
								   long killedNormalAgent2,
								   int killedNormalAgent3) {
		return true;
	}

	private static String buildPath(Node currentNode) {
		// TODO Auto-generated method stub
		return null;
	}
}
