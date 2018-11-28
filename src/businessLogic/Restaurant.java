package businessLogic;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Restaurant {

	private Dispatcher dispatcher;
	
	public Restaurant() {
		// TODO Auto-generated constructor stub
		this.dispatcher = new Dispatcher(1,1,1,1);
	}

	public static void main(String[] args) {
		
		//File f = new File("tables.txt");
		//System.out.print("file " + f.getName() + " was found in " + f.getPath());
		Restaurant t = new Restaurant();
		t.test();
	}
	
	public void test()
	{
		System.out.println("Enter a customer name and people: ");
		Scanner sc = new Scanner(System.in);
		String str = sc.nextLine();
		while(!str.isEmpty())
		{
			String [] line = str.split("-");
			String s = this.assignCustomer(line[0], Integer.parseInt(line[1]));
			System.out.println("RESPONSE: "+s);
			str = sc.nextLine();
		}
		
	}
	
	public String assignCustomer(String name, int numPeople)
	{
		Customer c = new Customer(name,numPeople);
		String enqueued = dispatcher.assignCustomer(c);
		return enqueued;
	}
	
	public void releaseTable(String tableId, int revenue)
	{
		boolean released = dispatcher.releaseTable(tableId, revenue);
	}
	
}
