package businessLogic;

import java.util.ArrayList;
import java.util.LinkedList;

public class TableType {
	
	private LinkedList<Customer> queue;
	private ArrayList<TakenTable> takenTables;
	private int numTables;
	private String type;
	private int capacity;
	
	public TableType(int numTables, String type, int capacity) {
		this.numTables = numTables;
		this.type = type;
		this.capacity = capacity;
		this.queue = new LinkedList<Customer>();
		this.takenTables = new ArrayList<TakenTable>();
	}

	public TakenTable createNewTakenTable(Customer c)
	{
		String type = this.getType();
		String id = type+"-"+this.takenTables.size();
		int capacity = this.getCapacity();
		return new TakenTable(id, type, capacity, c);
	}
	public void addTakenTable(TakenTable table)
	{
		this.takenTables.add(table);
	}
	public void addToQueue(Customer c)
	{
		this.queue.add(c);
	}
	public LinkedList<Customer> getQueue() {
		return queue;
	}

	public void setQueue(LinkedList<Customer> queue) {
		this.queue = queue;
	}

	public ArrayList<TakenTable> getTakenTables() {
		return takenTables;
	}

	public void setTakenTables(ArrayList<TakenTable> takenTables) {
		this.takenTables = takenTables;
	}

	public int getNumTables() {
		return numTables;
	}

	public void setNumTables(int numTables) {
		this.numTables = numTables;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	

}
