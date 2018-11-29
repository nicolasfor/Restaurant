package businessLogic;

import java.util.ArrayList;
import java.util.Date;

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
	
	private TableType smallTable;
	private TableType mediumTable;
	private TableType largeTable;
	private TableType extraLargeTable;
	
	private ArrayList<Bill> bills;
	
	public Dispatcher(int numSmallTables,int numMediumTables,int numLargeTables,int numExtraLargeTables) {
		// TODO Auto-generated constructor stub
		this.smallTable = new TableType(numSmallTables,SMALL_TABLE_TYPE,SMALL_TABLE_PEOPLE_CAPACITY);
		this.mediumTable = new TableType(numMediumTables,MEDIUM_TABLE_TYPE,MEDIUM_TABLE_PEOPLE_CAPACITY);
		this.largeTable = new TableType(numLargeTables,LARGE_TABLE_TYPE,LARGE_TABLE_PEOPLE_CAPACITY);
		this.extraLargeTable = new TableType(numExtraLargeTables,EXTRA_LARGE_TABLE_TYPE,EXTRA_TABLE_PEOPLE_CAPACITY);
		bills = new ArrayList<Bill>();
	}
	
	public TableType getTableTypeByCapacity(int companions)
	{
		if(companions<=SMALL_TABLE_PEOPLE_CAPACITY)
			return this.getSmallTable();
		else if(companions<=MEDIUM_TABLE_PEOPLE_CAPACITY)
			return this.getMediumTable();
		else if(companions<=LARGE_TABLE_PEOPLE_CAPACITY)
			return this.getLargeTable();
		else
			return this.getExtraLargeTable();
	}
	public TableType getTableTypeByType(String name)
	{
		if(name.equals(SMALL_TABLE_TYPE))
			return this.getSmallTable();
		else if(name.equals(MEDIUM_TABLE_TYPE))
			return this.getMediumTable();
		else if(name.equals(LARGE_TABLE_TYPE))
			return this.getLargeTable();
		else if(name.equals(EXTRA_LARGE_TABLE_TYPE))
			return this.getExtraLargeTable();
		return null;
	}
	
	public String assignCustomer(Customer c)
	{
		int companions = c.getCompanions();

		TableType type= this.getTableTypeByCapacity(companions);
		ArrayList<TakenTable> taken = type.getTakenTables();
		TakenTable newTaken = type.createNewTakenTable(c);
		String id=newTaken.getId();
		if(taken.size()<type.getNumTables())				
			type.addTakenTable(newTaken);
		else
		{
			id = this.checkAndAssignOtherTables(c);
			if(id==null)
				type.addToQueue(c);
		}
		
		return id;
	}
	
	public String checkAndAssignOtherTables(Customer c)
	{
		String id=null;
		int companions = c.getCompanions();
		
		TableType mediumTable = this.getMediumTable();
		TableType largeTable = this.getLargeTable();
		TableType extraLargeTable = this.getExtraLargeTable();
		
		int mediumTakenSize = mediumTable.getTakenTables().size();
		int largeTakenSize = largeTable.getTakenTables().size();
		int extraLargeTakenSize = extraLargeTable.getTakenTables().size();
		
		int numMediumTables = mediumTable.getNumTables();
		int numLargeTables = largeTable.getNumTables();
		int numExtraLargeTables = extraLargeTable.getNumTables();
		
		if(mediumTakenSize>=numMediumTables || companions>MEDIUM_TABLE_PEOPLE_CAPACITY || mediumTable.getQueue().size()>0)
		{
			if(largeTakenSize>=numLargeTables  || companions>LARGE_TABLE_PEOPLE_CAPACITY || largeTable.getQueue().size()>0)
			{
				if(extraLargeTakenSize<numExtraLargeTables && extraLargeTable.getQueue().size()==0)
				{
					id = EXTRA_LARGE_TABLE_TYPE+"-"+extraLargeTakenSize;
					TakenTable t = new TakenTable(id, EXTRA_LARGE_TABLE_TYPE, EXTRA_TABLE_PEOPLE_CAPACITY, c);
					extraLargeTable.addTakenTable(t);
				}
			}
			else
			{
				id = LARGE_TABLE_TYPE+"-"+largeTakenSize;
				TakenTable t = new TakenTable(id, LARGE_TABLE_TYPE, LARGE_TABLE_PEOPLE_CAPACITY, c);
				largeTable.addTakenTable(t);
			}
		}
		else
		{
			id = MEDIUM_TABLE_TYPE+"-"+mediumTakenSize;
			TakenTable t = new TakenTable(id, MEDIUM_TABLE_TYPE, MEDIUM_TABLE_PEOPLE_CAPACITY, c);
			this.mediumTable.addTakenTable(t);
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
					TableType tableType = this.getTableTypeByType(type);
					if(tableType!=null)
					{
						released = tableType.getTakenTables().get(index);
						tableType.getTakenTables().remove(index);
						String id = this.shuffleTables(released.getType());
						System.out.println("ID="+id);
						Bill b = new Bill(released, new Date(), revenue);
						this.bills.add(b);
					}
				}
				catch (Exception e) {
					
				}
			}
		}
		return released!=null;		
	}
	
	public String shuffleTables(String type)
	{
		String id=null;
		TableType tableType = this.getTableTypeByType(type);
		if(tableType!=null && !tableType.getQueue().isEmpty())
		{
			Customer c = tableType.getQueue().getFirst();
			if(tableType.getTakenTables().size()<tableType.getCapacity())
			{
				id = tableType.getType()+"-"+tableType.getCapacity();
				TakenTable t = new TakenTable(id,tableType.getType() ,tableType.getCapacity() , c);
				tableType.addTakenTable(t);
			}
			else
			{
				id = this.checkAndAssignOtherTables(c);
				if(id!=null)
					tableType.getQueue().removeFirst();
			}
		}
		return id;
	}

	public TableType getSmallTable() {
		return smallTable;
	}

	public void setSmallTable(TableType smallTable) {
		this.smallTable = smallTable;
	}

	public TableType getMediumTable() {
		return mediumTable;
	}

	public void setMediumTable(TableType mediumTable) {
		this.mediumTable = mediumTable;
	}

	public TableType getLargeTable() {
		return largeTable;
	}

	public void setLargeTable(TableType largeTable) {
		this.largeTable = largeTable;
	}

	public TableType getExtraLargeTable() {
		return extraLargeTable;
	}

	public void setExtraLargeTable(TableType extraLargeTable) {
		this.extraLargeTable = extraLargeTable;
	}
	
	

}
