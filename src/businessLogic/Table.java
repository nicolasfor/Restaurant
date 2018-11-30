package businessLogic;

public class Table {
	private String id;
	private String type;
	private int seats;
	private Customer busy;	
	
	public Table(String id,String type, int seats) {
		super();
		this.id = id;
		this.type = type;
		this.seats = seats;
		this.busy = null;
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

	public void bookTable(Customer c)
	{
		this.busy = c;
	}
	public void freeTable()
	{
		this.busy = null;
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

	public void setId(String id) {
		this.id = id;
	}
}
