package code;

public class PQNode {
	public Object data;

	// Lower values indicate higher priority
	int priority;

	PQNode next;
	
		public PQNode(Object data, int priority) {
		super();
		this.data = data;
		this.priority = priority;
		next=null;
	}

		

	

}
