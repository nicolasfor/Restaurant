package businessLogic;

import java.util.ArrayList;

public class Restaurant {

	private ArrayList<Table> smallTables;
	private ArrayList<Table> mediumTables;
	private ArrayList<Table> largeTables;
	private ArrayList<Table> extraLargeTables;
	
	
	public Restaurant() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

	public ArrayList<Table> getSmallTables() {
		return smallTables;
	}

	public void setSmallTables(ArrayList<Table> smallTables) {
		this.smallTables = smallTables;
	}

	public ArrayList<Table> getMediumTables() {
		return mediumTables;
	}

	public void setMediumTables(ArrayList<Table> mediumTables) {
		this.mediumTables = mediumTables;
	}

	public ArrayList<Table> getLargeTables() {
		return largeTables;
	}

	public void setLargeTables(ArrayList<Table> largeTables) {
		this.largeTables = largeTables;
	}

	public ArrayList<Table> getExtraLargeTables() {
		return extraLargeTables;
	}

	public void setExtraLargeTables(ArrayList<Table> extraLargeTables) {
		this.extraLargeTables = extraLargeTables;
	}
	

}
