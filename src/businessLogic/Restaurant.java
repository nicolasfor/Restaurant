package businessLogic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import exceptions.CustomerNotFoundException;
import exceptions.OutOfBoundQueueException;
import exceptions.TableNotFoundException;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Restaurant {

	private Dispatcher dispatcher;

	public Restaurant(Integer s, Integer m, Integer l, Integer xl) {
		// TODO Auto-generated constructor stub
		this.dispatcher = new Dispatcher(s, m, l, xl);
	}

	public static void main(String[] args) throws IOException {

		File file = new File("tables.txt");

		if (file.exists()) {

			//System.out.println("el archivo existe");

			System.out.print("file " + file.getName() + " was found in " + file.getPath());

			List<Integer> allValues;

			try (Stream<String> lines = Files.lines(file.toPath())) {
				allValues = lines.flatMap(Pattern.compile(" *")::splitAsStream).filter(s -> s.matches("[0-9]+"))
						.map(Integer::valueOf).collect(Collectors.toList());
				//imprime los valores de los tipos de tablas encontrados en el archivo por cada linea
				//allValues.forEach(v -> System.out.println(v));
			}
			Restaurant t = new Restaurant(allValues.get(0), allValues.get(1), allValues.get(2), allValues.get(3));

			Scanner sc = new Scanner(System.in);

			do {
				System.out.println(" Enter a command. If you need to kno the avaliables commands enter HELP ");

				try {

					String command = sc.nextLine();
					String[] line = command.split(" ");

					switch (line[0]) {
					case "ASSIGN":
						String s = t.assign(line[1], Integer.parseInt(line[2]));
						if(s!=null && !s.isEmpty())
						System.out.println("RESPONSE: " + s);
						break;
					case "FREE":
						t.free(line[1], Integer.parseInt(line[2]));
						break;
					case "CANCEL_RES":
						t.cancelRes(line[1]);
						break;
					case "END_DAY":
						t.endDay();
						break;
					case "SHOW_REV":
						t.showRev();
						break;
					case "SHOW_TAB":
						if(line[1].equals("STATUS"))
						{
							t.showTab_status();
						}else
						if(line[1].equals("USERS")){
							t.showTab_users(line[2]);
						}
						break;
					case "SHOW_RES":
						t.showRes();
						break;						
					case "CHANGE_TAB":
						t.changeTab(line[1], Integer.parseInt(line[2]));
						break;
					case "HELP":
						System.out.println("1. Enter ASSIGN customer_name _number_of_people: ");
						System.out.println("2. Enter FREE table_id revenue: ");	
						System.out.println("3. Enter CANCEL customer_name: ");
						System.out.println("4. Enter END_DAY");
						System.out.println("5. Enter SHOW_REV");
						System.out.println("6. Enter SHOW_TAB STATUS|{USERS table_type}");
						System.out.println("7. Enter SHOW_RES");
						System.out.println("8. Enter CHANGE_TAB table_type number_of_tables");
					}
				} catch (InputMismatchException e) {
					System.out.println("Not allowed format. please cheack and try again!");
					sc.next();
				}

			} while(true);
		}
	}

	public String assign(String name, int numPeople) {
		Customer c = new Customer(name, numPeople);
		try 
		{
			return dispatcher.assignCustomer(c);
		} 
		catch (OutOfBoundQueueException e) 
		{
			System.out.println(e.getMessage());
		}
		return null;
	}

	public void free(String tableId, int revenue) {
		try 
		{
			dispatcher.releaseTable(tableId, revenue);
			System.out.println("Table: " + tableId + " has been released!");
		}
		catch (TableNotFoundException e) 
		{
			System.out.println(e.getMessage());
		}
		catch (NumberFormatException e) 
		{
		   System.out.println("Table id format error");
		}
		
	}
	
	public void cancelRes(String name){
		try {
			dispatcher.cancelReservation(name);
			System.out.println("The reservation of:" + name + "has been cancelled" );
		} catch (CustomerNotFoundException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	public void showTab_status() {
		int smallAvailable = dispatcher.getSmallTablesAvailable();
		int mediumAvailable = dispatcher.getMediumTablesAvailable();
		int largeAvailable = dispatcher.getLargeTablesAvailable();
		int extraLargeAvailable = dispatcher.getExtraLargeTablesAvailable();
		
		System.out.println("the current status of tables for each table type is:");
		System.out.println("There are "+smallAvailable+" out of "+dispatcher.getSmallTable().getNumTables());
		System.out.println("There are "+mediumAvailable+" out of "+dispatcher.getMediumTable().getNumTables());
		System.out.println("There are "+largeAvailable+" out of "+dispatcher.getLargeTable().getNumTables());
		System.out.println("There are "+extraLargeAvailable+" out of "+dispatcher.getExtraLargeTable().getNumTables());
	}
	
	public void showTab_users(String tableType) {
		
		try 
		{
			ArrayList<Table> tables = dispatcher.getInformationByTableType(tableType);
			System.out.println("Tables taken for type: "+ tableType );
			for (int i = 0; i < tables.size(); i++) {
				if(tables.get(i).isBusy())
					System.out.println("Table "+ tables.get(i).getId()+" is taken by "+tables.get(i).getCustomer().getName());
			}

			
		} 
		catch (TableNotFoundException e) 
		{
			System.out.println(e.getMessage());
		}
	}
	
	public void endDay() {
		dispatcher.addNewDay();
		System.out.println("Day ends an bills are counted");
	}
	
	public void showRev() {
		ArrayList<Bill> bills = dispatcher.getBills();
			
		System.out.println("revenue is:");
	}
	
	public void showRes() {
		System.out.println("waiting list including the name and the number of people is:");
		LinkedList<Customer> smallQueue = dispatcher.getSmallTable().getQueue();
		LinkedList<Customer> mediumQueue = dispatcher.getMediumTable().getQueue();
		LinkedList<Customer> largeQueue = dispatcher.getLargeTable().getQueue();
		LinkedList<Customer> extraLargeQueue = dispatcher.getExtraLargeTable().getQueue();
		
		if(smallQueue.size()>0)
			System.out.println("For the small queue:");
		for (int i = 0; i < smallQueue.size(); i++) {
			System.out.println("Customer: "+smallQueue.get(i).getName()+ "with "+smallQueue.get(i).getCompanions()+" companions");
		}
		if(mediumQueue.size()>0)
			System.out.println("For the medium queue:");
		for (int i = 0; i < mediumQueue.size(); i++) {
			System.out.println("Customer: "+mediumQueue.get(i).getName()+ "with "+mediumQueue.get(i).getCompanions()+" companions");
		}
		if(largeQueue.size()>0)
			System.out.println("For the large queue:");
		for (int i = 0; i < largeQueue.size(); i++) {
			System.out.println("Customer: "+largeQueue.get(i).getName()+ "with "+largeQueue.get(i).getCompanions()+" companions");
		}
		if(extraLargeQueue.size()>0)
			System.out.println("For the extra large queue:");
		for (int i = 0; i < extraLargeQueue.size(); i++) {
			System.out.println("Customer: "+extraLargeQueue.get(i).getName()+ "with "+extraLargeQueue.get(i).getCompanions()+" companions");
		}
	}
	
	public void changeTab(String tableType, int numberOfTables) {
		System.out.println("Now there are" + numberOfTables + "of type" + tableType );
	}	
}