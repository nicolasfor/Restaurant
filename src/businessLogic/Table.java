package businessLogic;

public class Table {

	private String type;
	private int seats;
	private Customer occupied;
	private WaitingList queue;
	
	public Table() {
		// TODO Auto-generated constructor stub
	}
	
	public void bookTable(Customer customer)
	{
		this.setOccupied(customer);
	}
	public void releaseTable()
	{
		this.setOccupied(null);
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getSeats() {
		return seats;
	}
	public void setSeats(int seats) {
		this.seats = seats;
	}

	public Customer getOccupied() {
		return occupied;
	}

	public void setOccupied(Customer occupied) {
		this.occupied = occupied;
	}

}
