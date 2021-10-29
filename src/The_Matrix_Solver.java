import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

public class The_Matrix_Solver {
	
	private static ArrayList<Integer> availableCells;
	
	public static void main(String[] args) {
		System.out.println(genGrid());
	}
	
	
	private static String genGrid() {
		int m = random(5,15);
		int n = random(5,15);
		int c = random(1,4);
		
		//#########
		System.out.println("Maximum to carry: "+c);
		
		//#########
		String[][] grid = new String[m][n];
		
		availableCells = new ArrayList<Integer>(m*n);
		
		for(int i=0;i<m*n;i++)
			availableCells.add(i);
		
		//Position of Neo
		int neoPos = randomCell();
		int neoX = neoPos/n;
		int neoY = neoPos%n;
		
		
		//########
		grid[neoX][neoY] = "N";
		
		//Position of the Telephone booth
		int telephonePos = randomCell();
		int telephoneX = telephonePos/n;
		int telephoneY = telephonePos%n;
		
		//########
		grid[telephoneX][telephoneY] = "T";
		
		//The number of hostages
		int hostagesCount = random(3,10);
		StringBuilder hostagesInfo = new StringBuilder();
		//Positions of hostages;
		for(int i=0;i<hostagesCount;i++)
		{
			int hostagePos = randomCell();
			int hostageX = hostagePos/n;
			int hostageY = hostagePos%n;
			
			
			int hostageDamage = random(1,99);
			//########
			grid[hostageX][hostageY] = "H"+i+" "+hostageDamage;
			
			if(i>0)
				hostagesInfo.append(",");
			
			hostagesInfo.append(hostageX+","+hostageY+","+hostageDamage);	
		}
		
		//The number of pills
		int pillsCount = random(1,hostagesCount);
		StringBuilder pillsInfo = new StringBuilder();
		//Positions of pills;
		for(int i=0;i<pillsCount;i++)
		{
			int pillPos = randomCell();
			int pillX = pillPos/n;
			int pillY = pillPos%n;
			
			//########
			grid[pillX][pillY] = "P";
			
			if(i>0)
				pillsInfo.append(",");
			
			pillsInfo.append(pillX+","+pillY);	
		}
		
		//Pads
		int maxPadsCount = availableCells.size()%2==0? (availableCells.size()-2)/2:(availableCells.size()-1)/2;
		int padsCount = random(1,maxPadsCount);
		
		StringBuilder padsInfo = new StringBuilder();
		//Positions of pads;
		for(int i=0;i<padsCount;i++)
		{
			int startPadPos = randomCell();
			int startPadX = startPadPos/n;
			int startPadY = startPadPos%n;
			
			//########
			grid[startPadX][startPadY] = "F"+i;
			
			int endPadPos = randomCell();
			int endPadX = endPadPos/n;
			int endPadY = endPadPos%n;
			
			//########
			grid[endPadX][endPadY] = "D"+i;
			
			if(i>0)
				padsInfo.append(",");
			
			padsInfo.append(startPadX+","+startPadY+","+endPadX+","+endPadY);
			padsInfo.append(",");
			padsInfo.append(endPadX+","+endPadY+","+startPadX+","+startPadY);
		}
		
		
		//Agents
		int agentsCount = random(1,availableCells.size());
		
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
		}
		
		StringBuilder gridInfo = new StringBuilder();
		
		gridInfo.append(m+","+n+";"+c+";"+neoX+","+neoY+";"+telephoneX+","+telephoneY
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
		if (strategy=="btts")
		{}
		return "plan;deaths;kills;nodes";
	}
	
	public static String generalSearch(SearchProblem problem, String strategy) {
		Node node = new Node(problem.initialState, null, (byte)-1, (short)0, (short)0);
		PriorityQueue<Node> pq = new PriorityQueue<Node>();
	//	while(true) {
			if(pq.isEmpty())
				return "mnwb btts";
			Node currentNode = pq.poll();
			boolean isGoal = problem.goalTest(currentNode.state);
			if(isGoal) {
				return buildPath(currentNode);
			}
//			Node[] nodes = expand(currentNode);
	//	}
		return "btts";
	}


	private static Node[] expand(Node currentNode) {
	    byte m,n;
	    byte x =currentNode.state.neoX;
	    byte y =currentNode.state.neoY;
		//up, down, left, right, carry, drop, takePill, killR , killL , killU , killD	, and fly
		//0    1      2     3     4      5        6      7         8
//	 Node(State state, Node parent, byte operator, short depth, short pathCost) 
//		State(byte neoX, byte neoY, short movedHostages, short currentlyCarriedHostages, short hostagesToAgents,
//				short killedTransHostages, byte[] hostagesHealth, byte neoHealth) 
		
		
		return null;
	}


	private static String buildPath(Node currentNode) {
		// TODO Auto-generated method stub
		return null;
	}
}
