
public class SearchProblem {
	byte numOperators = 8; // or array of enum
	State initialState;
	// State space!?
	
	short numOfHostages;
	
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
}
