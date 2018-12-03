package businessLogic;

import java.util.ArrayList;

public class Table {
	private String id;
	private String type;
	private int seats;
	private Customer busy;	
	private ArrayList<DayRevenue> dailyRevenue;
	
	public Table(String id,String type, int seats) {
		super();
		this.id = id;
		this.type = type;
		this.seats = seats;
		this.busy = null;
		this.dailyRevenue = new ArrayList<DayRevenue>();
		DayRevenue d = new DayRevenue(1,this);
		this.dailyRevenue.add(d);
	}

	public String getType() {
		return type;
	}
	public int getSeats() {
		return seats;
	}

	public void bookTable(Customer c)
	{
		this.busy = c;
	}
	public void freeTable(int revenue)
	{
		this.busy = null;
		this.addToDayRevenue(revenue);
	}
	
	public boolean isBusy()
	{
		return busy!=null;
	}
	public Customer getCustomer() {
		return busy;
	}

	public String getId() {
		return id;
	}
	
	public void addToDayRevenue(int revenue)
	{
		int lastDay = this.dailyRevenue.size()-1;
		this.dailyRevenue.get(lastDay).addRevenue(revenue);
	}
	public void addNewDay(int day)
	{
		DayRevenue dr = new DayRevenue(day,this);
		dailyRevenue.add(dr);
	}

	public ArrayList<DayRevenue> getDailyRevenue() {
		return dailyRevenue;
	}
	
	
}
