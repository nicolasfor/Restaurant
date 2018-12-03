package businessLogic;

public class Customer {

	private int companions;
	private String name;
	
	public Customer(String name, int companions) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.companions = companions;
	}

	public int getCompanions() {
		return companions;
	}

	public String getName() {
		return name;
	}

}
