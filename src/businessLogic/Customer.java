package businessLogic;

public class Customer {

	private int companions;
	private String name;
	
	public Customer(String name, int companions) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.companions = companions;
	}

	public Customer(String name) {
		this.name = name;
	}

	public int getCompanions() {
		return companions;
	}

	public void setCompanions(int companions) {
		this.companions = companions;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
