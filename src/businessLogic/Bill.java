package businessLogic;

import java.util.Date;

public class Bill {

	private Table table;
	private int date;
	private int revenue;
	
	
	public Bill(Table table, int date, int money) {
		super();
		this.table = table;
		this.date = date;
		this.revenue = money;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public int getRevenue() {
		return revenue;
	}

	public void setRevenue(int revenue) {
		this.revenue = revenue;
	}
	
	
}
