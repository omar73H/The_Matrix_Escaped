package code;
import java.util.*;

class PQ {

	PQNode head;

	static int counter=0;
// Function to Create A New Node
	

public PQ() {
		head=null;
	}

	// Return the value at head
	Object peek() {
		return this.head.data;
	}

// Removes the element with the
// highest priority form the list
	public  Object poll() {
		this.counter-=1;
		PQNode temp = this.head;
		this.head = this.head.next;
	
		return temp.data;
	}

// Function to push according to priority
	public void push( Object d, int p) {
		this.counter+=1;
		PQNode temp=new PQNode(d,p);
		if(this.head==null) {
		this.head=temp;
		return;
		}
		
		// Create new Node
		

		// Special Case: The head of list has lesser
		// priority than new node. So insert new
		// node before head node and change head node.
		if (this.head.priority > p) {

			// Insert New Node before head
			temp.next = this.head;
			this.head = temp;
		} else {

			// Traverse the list and find a
			// position to insert new node
			PQNode start=this.head;
			while (start.next != null && start.next.priority < p) {
				start = start.next;
			}

			// Either at the ends of the list
			// or at required position
			temp.next = start.next;
			start.next = temp;
		}
		
	}

// Function to check is list is empty
	public boolean isEmpty() {
		return (this.counter == 0) ? true : false;
	}

// Driver code
	
}

// This code is contributed
// by Eiad Essam