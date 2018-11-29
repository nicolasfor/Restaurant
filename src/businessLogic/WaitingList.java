package businessLogic;

import java.util.LinkedList;

public class WaitingList {

	LinkedList<Customer> customerQueueSmall = new LinkedList<Customer>();
	LinkedList<Customer> customerQueueMedium = new LinkedList<Customer>();
	LinkedList<Customer> customerQueueLarge = new LinkedList<Customer>();
	LinkedList<Customer> customerQueueExtraLarge = new LinkedList<Customer>();
	
	public WaitingList() {
		// TODO Auto-generated constructor stub
	}
}
