package businessLogic;

import java.util.ArrayList;
import java.util.LinkedList;

import exceptions.TableNotFoundException;

public class TableType {
	
	private LinkedList<Customer> queue;
	private ArrayList<Table> tables;
	private int numAvailableTables;
	private String type;
	private int capacity;
	private int numTables;
	
	public TableType(int numTables, String type, int capacity) {
		
		this.type = type;
		this.capacity = capacity;
		this.queue = new LinkedList<Customer>();
		this.tables = new ArrayList<Table>();
		this.numAvailableTables = numTables;
		this.numTables = numTables;
		for (int i = 0; i < numTables; i++) {
			tables.add(new Table(type+"-"+i, type, capacity));
		}
	}

	public String bookTable(Customer c)
	{
		for (int i = 0; i < tables.size(); i++) {
			if(!tables.get(i).isBusy())
			{
				tables.get(i).bookTable(c);
				this.setNumAvailableTables(getNumAvailableTables()-1);
				return tables.get(i).getId();
			}
		}
		return null;		
	}
	
	public Table freeTable(String id) throws TableNotFoundException
	{
		boolean found = false; 
		for (int i = 0; i < tables.size(); i++) {
			if(tables.get(i).getId().equals(id))
			{
				found = true;
				tables.get(i).freeTable();
				this.setNumAvailableTables(getNumAvailableTables()+1);
				return tables.get(i);
			}
		}
		if(!found)
			throw new TableNotFoundException();
		return null;
	}
	public void addToQueue(Customer c)
	{
		this.queue.add(c);
	}
	public boolean findAndRemoveCustomerInQueued(String name)
	{
		boolean exists= false;
		for (int i = 0; i < queue.size(); i++) {
			if(queue.get(i).getName().equals(name))
			{
				exists=true;
				queue.remove(i);
			}
		}
		return exists;
	}

	public LinkedList<Customer> getQueue() {
		return queue;
	}

	public void setQueue(LinkedList<Customer> queue) {
		this.queue = queue;
	}

	public int getNumAvailableTables() {
		return numAvailableTables;
	}

	public void setNumAvailableTables(int numTables) {
		this.numAvailableTables = numTables;
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

	public ArrayList<Table> getTables() {
		return tables;
	}

	public void setTables(ArrayList<Table> tables) {
		this.tables = tables;
	}

	public int getNumTables() {
		return numTables;
	}

	public void setNumTables(int numTables) {
		this.numTables = numTables;
	}
	
	

}
