package businessLogic;

import java.util.ArrayList;
import java.util.Date;

import exceptions.CustomerAlreadyExistsException;
import exceptions.CustomerNotFoundException;
import exceptions.OutOfBoundQueueException;
import exceptions.TableNotFoundException;

public class Dispatcher {
	
	private static final int MAX_CAPACITY_QUEUE=20;
	
	public static final int SMALL_TABLE_PEOPLE_CAPACITY=2;
	public static final int MEDIUM_TABLE_PEOPLE_CAPACITY=4;
	public static final int LARGE_TABLE_PEOPLE_CAPACITY=6;
	public static final int EXTRA_TABLE_PEOPLE_CAPACITY=9;
	
	public static final String SMALL_TABLE_TYPE="SMALL";
	public static final String MEDIUM_TABLE_TYPE="MEDIUM";
	public static final String LARGE_TABLE_TYPE="LARGE";
	public static final String EXTRA_LARGE_TABLE_TYPE="EXTRA_LARGE";
	
	private TableType smallTable;
	private TableType mediumTable;
	private TableType largeTable;
	private TableType extraLargeTable;
	
	private int currentDate;
	
	public Dispatcher(int numSmallTables,int numMediumTables,int numLargeTables,int numExtraLargeTables) {
		// TODO Auto-generated constructor stub
		this.smallTable = new TableType(numSmallTables,SMALL_TABLE_TYPE,SMALL_TABLE_PEOPLE_CAPACITY);
		this.mediumTable = new TableType(numMediumTables,MEDIUM_TABLE_TYPE,MEDIUM_TABLE_PEOPLE_CAPACITY);
		this.largeTable = new TableType(numLargeTables,LARGE_TABLE_TYPE,LARGE_TABLE_PEOPLE_CAPACITY);
		this.extraLargeTable = new TableType(numExtraLargeTables,EXTRA_LARGE_TABLE_TYPE,EXTRA_TABLE_PEOPLE_CAPACITY);
		this.currentDate = 1;
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
	public TableType getTableTypeByType(String name) throws TableNotFoundException
	{
		if(name.equals(SMALL_TABLE_TYPE))
			return this.getSmallTable();
		else if(name.equals(MEDIUM_TABLE_TYPE))
			return this.getMediumTable();
		else if(name.equals(LARGE_TABLE_TYPE))
			return this.getLargeTable();
		else if(name.equals(EXTRA_LARGE_TABLE_TYPE))
			return this.getExtraLargeTable();
		throw new TableNotFoundException();
	}
	
	public String assignCustomer(Customer c) throws OutOfBoundQueueException, CustomerAlreadyExistsException
	{
		int companions = c.getCompanions();

		TableType type= this.getTableTypeByCapacity(companions);
		String id= null;
		if(type.getNumAvailableTables()>0)
			id = type.bookTable(c);
		else
		{
			id = this.checkAndAssignOtherTables(c);
			if(id==null && type.getQueue().size()<MAX_CAPACITY_QUEUE)
			{
				boolean exists = type.alreadyExistsCustomerInQueue(c.getName());
				if(exists)
					throw new CustomerAlreadyExistsException();
				else
					type.addToQueue(c);
			}
				
			else if(type.getQueue().size()>=MAX_CAPACITY_QUEUE)
				throw new OutOfBoundQueueException();
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
		
		int mediumAvailable = mediumTable.getNumAvailableTables();
		int largeAvailable = largeTable.getNumAvailableTables();
		int extraLargeAvailable = extraLargeTable.getNumAvailableTables();
		
		if(mediumAvailable==0 || companions>MEDIUM_TABLE_PEOPLE_CAPACITY || mediumTable.getQueue().size()>0)
		{
			if(largeAvailable==0  || companions>LARGE_TABLE_PEOPLE_CAPACITY || largeTable.getQueue().size()>0)
			{
				if(extraLargeAvailable>0 && extraLargeTable.getQueue().size()==0)
					id = extraLargeTable.bookTable(c);
			}
			else
				id = largeTable.bookTable(c);
		}
		else
			id = mediumTable.bookTable(c);
		return id;
	}
	
	public void releaseTable(String tableId, int revenue) throws TableNotFoundException
	{
		String [] tableIdParts = tableId.split("-");
		if(tableIdParts.length>0)
		{
			String type = tableIdParts[0];
			if(!type.isEmpty())
			{
					TableType tableType = this.getTableTypeByType(type);
					if(tableType!=null)
					{
						tableType.freeTable(tableId,revenue);
						this.shuffleTables(tableType);
					}
			}
			else
				throw new TableNotFoundException();
		}
	}
	
	public void shuffleTables(TableType tableType)
	{
		if( !tableType.getQueue().isEmpty())
		{
			Customer c = tableType.getQueue().getFirst();
			if(tableType.getNumAvailableTables()>0)
			{
				tableType.bookTable(c);
				tableType.getQueue().removeFirst();
			}
		}
			
	}
	
	public void cancelReservation(String name)throws CustomerNotFoundException
	{
		boolean found = this.smallTable.findAndRemoveCustomerInQueued(name);
		if(!found)
			found = this.mediumTable.findAndRemoveCustomerInQueued(name);
		if(!found)
			found = this.largeTable.findAndRemoveCustomerInQueued(name);
		if(!found)
			found = this.extraLargeTable.findAndRemoveCustomerInQueued(name);
		
		if(!found)
			throw new CustomerNotFoundException();
	}
	
	public ArrayList<Table> getInformationByTableType(String type) throws TableNotFoundException
	{
		TableType tt = this.getTableTypeByType(type);
		return tt.getTables();
	}
	
	public void addNewDay()
	{
		int newCurrentDate = this.getCurrentDate()+1;
		this.setCurrentDate(newCurrentDate);
		smallTable.addNewDay(newCurrentDate);
		mediumTable.addNewDay(newCurrentDate);
		largeTable.addNewDay(newCurrentDate);
		extraLargeTable.addNewDay(newCurrentDate);
	}
		
	public int getSmallTablesAvailable()
	{
		return this.getSmallTable().getNumAvailableTables();
	}
	public int getMediumTablesAvailable()
	{
		return this.getMediumTable().getNumAvailableTables();
	}
	public int getLargeTablesAvailable()
	{
		return this.getLargeTable().getNumAvailableTables();
	}
	public int getExtraLargeTablesAvailable()
	{
		return this.getExtraLargeTable().getNumAvailableTables();
	}
	public TableType getSmallTable() {
		return smallTable;
	}

	public TableType getMediumTable() {
		return mediumTable;
	}


	public TableType getLargeTable() {
		return largeTable;
	}

	public TableType getExtraLargeTable() {
		return extraLargeTable;
	}

	public int getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(int currentDate) {
		this.currentDate = currentDate;
	}
	

}
