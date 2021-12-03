package code;
public abstract class SearchProblem {
	byte numOperators; // or array of enum
	State initialState;
	
	public SearchProblem(State initialState, byte numOperators) {
		this.initialState= initialState;
		this.numOperators = numOperators;
		
	}
	
	abstract public int calculatePathCost(Node node);	
	abstract public boolean goalTest(State state);	

}
