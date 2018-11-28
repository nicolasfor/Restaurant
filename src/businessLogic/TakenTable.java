package businessLogic;

public class TakenTable {

	private String id;
	private String type;
	private int seats;
	private Customer occupied;	
	
	public TakenTable(String id,String type, int seats, Customer occupied) {
		super();
		this.id = id;
		this.type = type;
		this.seats = seats;
		this.occupied = occupied;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	

}
