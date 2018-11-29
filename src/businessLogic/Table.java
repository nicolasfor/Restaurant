package businessLogic;

public interface Table {
/*
	private String type;
	private int seats;
	private Customer occupied;
	private WaitingList queue;
	*/
	public void bookTable(Customer customer);
	public void releaseTable();
	public String getType();
	public void setType(String type) ;
	public int getSeats();
	public void setSeats(int seats);
	public Customer getOccupied();
	public void setOccupied(Customer occupied);
}
