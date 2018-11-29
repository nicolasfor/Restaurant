package businessLogic;

import java.util.Date;

public class Bill {

	private TakenTable table;
	private Date date;
	private int revenue;
	
	
	public Bill(TakenTable table, Date date, int money) {
		super();
		this.table = table;
		this.date = date;
		this.revenue = money;
	}

	public TakenTable getTable() {
		return table;
	}

	public void setTable(TakenTable table) {
		this.table = table;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getRevenue() {
		return revenue;
	}

	public void setRevenue(int revenue) {
		this.revenue = revenue;
	}
	
	
}
