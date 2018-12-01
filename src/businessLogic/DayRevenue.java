package businessLogic;

public class DayRevenue {

	private int revenue;
	private int day;
	private Table table;
	public DayRevenue(int day,Table table) {
		this.day = day;
		this.table = table;
		this.revenue = 0;
	}
	public int getRevenue() {
		return revenue;
	}
	public void setRevenue(int revenue) {
		this.revenue = revenue;
	}
	public void addRevenue(int revenue)
	{
		int newRevenue = getRevenue()+revenue;
		setRevenue(newRevenue);
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public Table getTable() {
		return table;
	}
	public void setTable(Table table) {
		this.table = table;
	}
	
	
}
