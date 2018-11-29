package businessLogic;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class Dispatcher {
	
	private static final int MAX_CAPACITY_QUEUE=20;
	
	private static final int SMALL_TABLE_PEOPLE_CAPACITY=2;
	private static final int MEDIUM_TABLE_PEOPLE_CAPACITY=4;
	private static final int LARGE_TABLE_PEOPLE_CAPACITY=6;
	private static final int EXTRA_TABLE_PEOPLE_CAPACITY=9;
	
	private static final String SMALL_TABLE_TYPE="SMALL";
	private static final String MEDIUM_TABLE_TYPE="MEDIUM";
	private static final String LARGE_TABLE_TYPE="LARGE";
	private static final String EXTRA_LARGE_TABLE_TYPE="EXTRA_LARGE";
	
	private LinkedList<Customer> smallQueue;
	private LinkedList<Customer> mediumQueue;
	private LinkedList<Customer> largeQueue;
	private LinkedList<Customer> extraLargeQueue;
	
	private ArrayList<TakenTable> smallTables;
	private ArrayList<TakenTable> mediumTables;
	private ArrayList<TakenTable> largeTables;
	private ArrayList<TakenTable> extraLargeTables;
	
	private int numSmallTables;
	private int numMediumTables;
	private int numLargeTables;
	private int numExtraLargeTables;
		
	private ArrayList<Bill> bills;
	
	public Dispatcher(int numSmallTables,int numMediumTables,int numLargeTables,int numExtraLargeTables) {
		// TODO Auto-generated constructor stub
		smallQueue = new LinkedList<Customer>();
		mediumQueue = new LinkedList<Customer>();
		largeQueue = new LinkedList<Customer>();
		extraLargeQueue = new LinkedList<Customer>();
		
		smallTables = new ArrayList<TakenTable>();
		mediumTables = new ArrayList<TakenTable>();
		largeTables = new ArrayList<TakenTable>();
		extraLargeTables = new ArrayList<TakenTable>();
		
		this.numSmallTables = numSmallTables;
		this.numMediumTables = numMediumTables;
		this.numLargeTables = numLargeTables;
		this.numExtraLargeTables = numExtraLargeTables;
	}
	
	public String assignCustomer(Customer c)
	{
		int companions = c.getCompanions();
		int busy;
		String id=null;
		if(companions<=SMALL_TABLE_PEOPLE_CAPACITY)
		{
			busy=this.smallTables.size();
			id = SMALL_TABLE_TYPE+"-"+this.smallTables.size();
			TakenTable t = new TakenTable(id, SMALL_TABLE_TYPE,SMALL_TABLE_PEOPLE_CAPACITY , c);
			if(busy<this.getNumSmallTables())				
				smallTables.add(t);
			else
			{
				id = this.checkAndAssignOtherTables(c);
				if(id==null)
					smallQueue.add(c);
			}
		}
		else if(companions<=MEDIUM_TABLE_PEOPLE_CAPACITY) 
		{
			busy=this.mediumTables.size();
			id = MEDIUM_TABLE_TYPE+"-"+this.mediumTables.size();
			TakenTable t = new TakenTable(id,MEDIUM_TABLE_TYPE ,MEDIUM_TABLE_PEOPLE_CAPACITY , c);
			if(busy<this.getNumMediumTables())
				mediumTables.add(t);
			else
			{
				id = this.checkAndAssignOtherTables(c);
				if(id==null)
					mediumQueue.add(c);
			}
		}
		else if(companions<=LARGE_TABLE_PEOPLE_CAPACITY) 
		{
			busy=this.largeTables.size();
			id = LARGE_TABLE_TYPE+"-"+this.largeTables.size();
			TakenTable t = new TakenTable(id, LARGE_TABLE_TYPE,LARGE_TABLE_PEOPLE_CAPACITY , c);
			if(busy<this.getNumLargeTables())				
				largeTables.add(t);
			else
			{
				id = this.checkAndAssignOtherTables(c);
				if(id==null)
					largeQueue.add(c);
			}
		}
		else
		{
			id = EXTRA_LARGE_TABLE_TYPE+"-"+this.extraLargeTables.size();
			busy=this.extraLargeTables.size();
			TakenTable t = new TakenTable(id, EXTRA_LARGE_TABLE_TYPE,EXTRA_TABLE_PEOPLE_CAPACITY, c);
			if(busy<this.getNumExtraLargeTables())
				extraLargeTables.add(t);
			else
			{
				id = this.checkAndAssignOtherTables(c);
				if(id==null)
					extraLargeQueue.add(c);
			}
		}
		return id;
	}
	
	public String checkAndAssignOtherTables(Customer c)
	{
		String id=null;
		int companions = c.getCompanions();
		int busy = this.mediumTables.size();
		if(busy>=this.getNumMediumTables() || companions>MEDIUM_TABLE_PEOPLE_CAPACITY || mediumQueue.size()>0)
		{
			busy = this.largeTables.size();
			if(busy>=this.getNumLargeTables() || companions>LARGE_TABLE_PEOPLE_CAPACITY || largeQueue.size()>0)
			{
				busy = this.extraLargeTables.size();
				if(busy<this.getNumExtraLargeTables() && extraLargeQueue.size()==0)
				{
					id = EXTRA_LARGE_TABLE_TYPE+"-"+this.extraLargeTables.size();
					TakenTable t = new TakenTable(id, EXTRA_LARGE_TABLE_TYPE, EXTRA_TABLE_PEOPLE_CAPACITY, c);
					this.extraLargeTables.add(t);
				}
			}
			else
			{
				id = LARGE_TABLE_TYPE+"-"+this.largeTables.size();
				TakenTable t = new TakenTable(id, LARGE_TABLE_TYPE, LARGE_TABLE_PEOPLE_CAPACITY, c);
				this.largeTables.add(t);
			}
		}
		else
		{
			id = MEDIUM_TABLE_TYPE+"-"+this.mediumTables.size();
			TakenTable t = new TakenTable(id, MEDIUM_TABLE_TYPE, MEDIUM_TABLE_PEOPLE_CAPACITY, c);
			this.mediumTables.add(t);
		}
		return id;
	}
	
	public boolean releaseTable(String tableId, int revenue)
	{
		String [] tableIdParts = tableId.split("-");
		TakenTable released = null;
		if(tableIdParts.length>1)
		{
			String type = tableIdParts[0];
			String indexS = tableIdParts[1];
			if(!type.isEmpty() && !indexS.isEmpty())
			{
				
				try {
					int index = Integer.parseInt(indexS);
					if(type.equals(SMALL_TABLE_TYPE) && this.smallTables.size()>index)
					{
						released=this.smallTables.get(index);
						smallTables.remove(index);
					}
					else if(type.equals(MEDIUM_TABLE_TYPE) && mediumTables.size()>index)
					{
						released=this.mediumTables.get(index);
						mediumTables.remove(index);
					}
					else if(type.equals(LARGE_TABLE_TYPE) && largeTables.size()>index)
					{
						released=this.largeTables.get(index);
						largeTables.remove(index);
					}
					else if(type.equals(EXTRA_LARGE_TABLE_TYPE) && extraLargeTables.size()>index)
					{
						released=this.extraLargeTables.get(index);
						extraLargeTables.remove(index);
					}
				}
				catch (Exception e) {
					
				}
			}
		}
		if(released!=null)
		{
			String id = this.shuffleTables(released.getType());
			System.out.println("ID="+id);
			Bill b = new Bill(released, new Date(), revenue);
			this.bills.add(b);
		}
		return released!=null;		
	}
	
	public String shuffleTables(String type)
	{
		String id=null;
		if(!this.smallQueue.isEmpty() && type.equals(SMALL_TABLE_TYPE))
		{
			Customer c = this.smallQueue.getFirst();
			if(this.smallTables.size()<this.getNumMediumTables())
			{
				id = SMALL_TABLE_TYPE+"-"+this.smallTables.size();
				TakenTable t = new TakenTable(id,SMALL_TABLE_TYPE ,SMALL_TABLE_PEOPLE_CAPACITY , c);
				this.smallTables.add(t);
				this.smallQueue.removeFirst();
			}
			else
			{
				id = this.checkAndAssignOtherTables(c);
				if(id!=null)
					this.smallQueue.removeFirst();
			}
		}
		else if(!this.mediumQueue.isEmpty() && type.equals(MEDIUM_TABLE_TYPE))
		{
			Customer c = this.smallQueue.getFirst();
			if(this.mediumTables.size()<this.getNumMediumTables())
			{
				id = MEDIUM_TABLE_TYPE+"-"+this.mediumTables.size();
				TakenTable t = new TakenTable(id,MEDIUM_TABLE_TYPE ,MEDIUM_TABLE_PEOPLE_CAPACITY , c);
				this.mediumTables.add(t);
				this.mediumQueue.removeFirst();
			}
			else
			{
				id = this.checkAndAssignOtherTables(c);
				if(id!=null)
					this.mediumQueue.removeFirst();
			}
		}
		else if(!this.largeQueue.isEmpty() && type.equals(LARGE_TABLE_TYPE))
		{
			Customer c = this.largeQueue.getFirst();
			if(this.largeTables.size()<this.getNumMediumTables())
			{
				id = LARGE_TABLE_TYPE+"-"+this.largeTables.size();
				TakenTable t = new TakenTable(id,LARGE_TABLE_TYPE ,LARGE_TABLE_PEOPLE_CAPACITY , c);
				this.largeTables.add(t);
				this.largeQueue.removeFirst();
			}
			else
			{
				id = this.checkAndAssignOtherTables(c);
				if(id!=null)
					this.largeQueue.removeFirst();
			}
		}
		else if(!this.extraLargeQueue.isEmpty() && type.equals(EXTRA_LARGE_TABLE_TYPE))
		{
			Customer c = this.extraLargeQueue.getFirst();
			if(this.extraLargeTables.size()<this.getNumMediumTables())
			{
				id = LARGE_TABLE_TYPE+"-"+this.extraLargeTables.size();
				TakenTable t = new TakenTable(id,EXTRA_LARGE_TABLE_TYPE ,EXTRA_TABLE_PEOPLE_CAPACITY , c);
				this.extraLargeTables.add(t);
				this.extraLargeQueue.removeFirst();
			}

		}
		return id;
	}

	public int getNumSmallTables() {
		return numSmallTables;
	}

	public void setNumSmallTables(int numSmallTables) {
		this.numSmallTables = numSmallTables;
	}

	public int getNumMediumTables() {
		return numMediumTables;
	}

	public void setNumMediumTables(int numMediumTables) {
		this.numMediumTables = numMediumTables;
	}

	public int getNumLargeTables() {
		return numLargeTables;
	}

	public void setNumLargeTables(int numLargeTables) {
		this.numLargeTables = numLargeTables;
	}

	public int getNumExtraLargeTables() {
		return numExtraLargeTables;
	}

	public void setNumExtraLargeTables(int numExtraLargeTables) {
		this.numExtraLargeTables = numExtraLargeTables;
	}
	
	
	
}
