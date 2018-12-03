package businessLogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.LinkedList;

import exceptions.CharactersOutOfBoundException;
import exceptions.CustomerAlreadyExistsException;
import exceptions.CustomerNotFoundException;
import exceptions.NotValidFileFormatException;
import exceptions.NumberNegativeException;
import exceptions.OutOfBoundQueueException;
import exceptions.TableNotFoundException;

import java.util.Scanner;

import javax.xml.stream.events.StartDocument;

public class Restaurant {

	public Dispatcher dispatcher;

	static private int smallTables;
	static private int mediumTables;
	static private int largeTables;
	static private int extraLargeTables;

	public Restaurant(Integer s, Integer m, Integer l, Integer xl) {
		// TODO Auto-generated constructor stub
		this.dispatcher = new Dispatcher(s, m, l, xl);
	}

	public static void main(String[] args) throws IOException, NotValidFileFormatException {
		String fileName = "tables.txt";

		readLineByLineAndSplit(fileName);

		new Restaurant(smallTables, mediumTables, largeTables, extraLargeTables).start();
	}
	
	public void start()
	{
		
		Scanner sc = new Scanner(System.in);

		do {
			System.out.println(" Enter a command. If you need to kno the avaliables commands enter HELP ");

			try {

				String command = sc.nextLine();
				String[] line = command.split(" ");
				switch (line[0]) {
				case "ASSIGN":
					try {
						String s = assign(line[1], Integer.parseInt(line[2]));
						if (s != null && !s.isEmpty())
							System.out.println("RESPONSE: " + s);
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
					break;
				case "FREE":
					free(line[1], Integer.parseInt(line[2]));
					break;
				case "CANCEL_RES":
					cancelRes(line[1]);
					break;
				case "END_DAY":
					endDay();
					break;
				case "SHOW_REV":
					showRev();
					break;
				case "SHOW_TAB":
					if (line[1].equals("STATUS")) {
						showTab_status();
					} else if (line[1].equals("USERS")) {
						showTab_users(line[2]);
					}
					break;
				case "SHOW_RES":
					showRes();
					break;
				case "CHANGE_TAB":
					try {
						changeTab(line[1], Integer.parseInt(line[2]));
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
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

		} while (sc.hasNextLine());

	}
	public static void readLineByLineAndSplit(String filePath){
		Path path = Paths.get(filePath);
		long count = 0;
		try {
			BufferedReader reader = Files.newBufferedReader(path, Charset.defaultCharset());
			String line;
			while ((line = reader.readLine()) != null) {
				String arraylines[] = line.split("-");
				if (count == 0 && arraylines[0].equals("SMALL")) {
					smallTables = Integer.parseInt(arraylines[1]);
					count++;
				} else if (count == 1 && arraylines[0].equals("MEDIUM")) {
					mediumTables = Integer.parseInt(arraylines[1]);
					count++;
				} else if (count == 2 && arraylines[0].equals("LARGE")) {
					largeTables = Integer.parseInt(arraylines[1]);
					count++;
				} else if (count == 3 && arraylines[0].equals("EXTRALARGE")) {
					extraLargeTables = Integer.parseInt(arraylines[1]);
					count++;
				} else
					throw new NotValidFileFormatException();
				// System.out.println("la linea " + arraylines[0] + " contiene: " +
				// arraylines[1]);
			}
			reader.close();
		} 
		catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public String assign(String name, int numPeople) throws NumberNegativeException, CharactersOutOfBoundException, CustomerAlreadyExistsException, OutOfBoundQueueException {
		Customer c = new Customer(name, numPeople);
		if (numPeople < 0)
			throw new NumberNegativeException();
		if (name.length() > 10)
			throw new CharactersOutOfBoundException();
		try {
			return dispatcher.assignCustomer(c);
		} catch (OutOfBoundQueueException e) {
			System.out.println(e.getMessage());throw new OutOfBoundQueueException();
		} catch (CustomerAlreadyExistsException e) {
			System.out.println(e.getMessage());throw new CustomerAlreadyExistsException();
		}
		//return null; Eclipse sugirio que era inalcanzable
	}

	public void free(String tableId, int revenue) {
		try {
			dispatcher.releaseTable(tableId, revenue);
			System.out.println("Table: " + tableId + " has been released!");
		} catch (TableNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (NumberFormatException e) {
			System.out.println("Table id format error");
		}

	}

	public void cancelRes(String name) {
		try {
			dispatcher.cancelReservation(name);
			System.out.println("The reservation of:" + name + "has been cancelled");
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
		System.out.println("There are " + smallAvailable + " out of " + dispatcher.getSmallTable().getNumTables()
				+ " small tables");
		System.out.println("There are " + mediumAvailable + " out of " + dispatcher.getMediumTable().getNumTables()
				+ " medium tables");
		System.out.println("There are " + largeAvailable + " out of " + dispatcher.getLargeTable().getNumTables()
				+ " large tables");
		System.out.println("There are " + extraLargeAvailable + " out of "
				+ dispatcher.getExtraLargeTable().getNumTables() + " extra large tables");
	}

	public void showTab_users(String tableType) {

		try {
			ArrayList<Table> tables = dispatcher.getInformationByTableType(tableType);
			boolean found = false;
			System.out.println("Tables taken for type: " + tableType);
			for (int i = 0; i < tables.size(); i++) {
				if (tables.get(i).isBusy()) {
					found = true;
					System.out.println(
							"Table " + tables.get(i).getId() + " is taken by " + tables.get(i).getCustomer().getName());
				}
			}
			if (!found)
				System.out.println("There are no " + tableType + " tables taken");

		} catch (TableNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	public void endDay() {
		dispatcher.addNewDay();
		System.out.println("Day ends an bills are counted");
	}

	public void showRev() {
		TableType small = dispatcher.getSmallTable();
		TableType medium = dispatcher.getMediumTable();
		TableType large = dispatcher.getLargeTable();
		TableType extraLarge = dispatcher.getExtraLargeTable();

		ArrayList<DayRevenue> smallRevenues = small.getDailyRevenues();
		ArrayList<DayRevenue> mediumRevenues = medium.getDailyRevenues();
		ArrayList<DayRevenue> largeRevenues = large.getDailyRevenues();
		ArrayList<DayRevenue> extraLargeRevenues = extraLarge.getDailyRevenues();
		int overAllSmall = this.printDailyReport(smallRevenues);
		System.out.println("Total Revenue for Small Tables is: " + overAllSmall);
		System.out.println("---------------------------------------");
		int overAllMedium = this.printDailyReport(mediumRevenues);
		System.out.println("Total Revenue for Medium Tables is: " + overAllMedium);
		System.out.println("---------------------------------------");
		int overAllLarge = this.printDailyReport(largeRevenues);
		System.out.println("Total Revenue for Large Tables is: " + overAllLarge);
		System.out.println("---------------------------------------");
		int overAllExtraLarge = this.printDailyReport(extraLargeRevenues);
		System.out.println("Total Revenue for Extralarges Tables is: " + overAllExtraLarge);
		System.out.println("---------------------------------------");

		System.out.println("---------------------------------------");
		int total = overAllSmall + overAllMedium + overAllLarge + overAllExtraLarge;
		System.out.println("Total revenue is: " + total);

	}

	public int printDailyReport(ArrayList<DayRevenue> revenues) {
		int currentDay = -1;
		int overAllRevenue = 0;
		int weekRevenue = 0;
		for (int i = 0; i < revenues.size(); i++) {
			currentDay = revenues.get(i).getDay();
			overAllRevenue += revenues.get(i).getRevenue();
			weekRevenue += revenues.get(i).getRevenue();
			System.out.println("Day " + currentDay + " Revenue for small table with id: "
					+ revenues.get(i).getTable().getId() + " is " + revenues.get(i).getRevenue());
			if (currentDay % 7 == 0) {
				System.out.println("---------------------------------------");
				System.out.println("Weekly revenue for small tables is " + weekRevenue);
				weekRevenue = 0;
			}
		}
		return overAllRevenue;
	}

	public void showRes() {
		System.out.println("waiting list including the name and the number of people is:");
		LinkedList<Customer> smallQueue = dispatcher.getSmallTable().getQueue();
		LinkedList<Customer> mediumQueue = dispatcher.getMediumTable().getQueue();
		LinkedList<Customer> largeQueue = dispatcher.getLargeTable().getQueue();
		LinkedList<Customer> extraLargeQueue = dispatcher.getExtraLargeTable().getQueue();

		if (smallQueue.size() > 0)
			System.out.println("For the small queue:");
		for (int i = 0; i < smallQueue.size(); i++) {
			System.out.println("Customer: " + smallQueue.get(i).getName() + " with " + smallQueue.get(i).getCompanions()
					+ " companions");
		}
		if (mediumQueue.size() > 0)
			System.out.println("For the medium queue:");
		for (int i = 0; i < mediumQueue.size(); i++) {
			System.out.println("Customer: " + mediumQueue.get(i).getName() + " with "
					+ mediumQueue.get(i).getCompanions() + " companions");
		}
		if (largeQueue.size() > 0)
			System.out.println("For the large queue:");
		for (int i = 0; i < largeQueue.size(); i++) {
			System.out.println("Customer: " + largeQueue.get(i).getName() + " with " + largeQueue.get(i).getCompanions()
					+ " companions");
		}
		if (extraLargeQueue.size() > 0)
			System.out.println("For the extra large queue:");
		for (int i = 0; i < extraLargeQueue.size(); i++) {
			System.out.println("Customer: " + extraLargeQueue.get(i).getName() + " with "
					+ extraLargeQueue.get(i).getCompanions() + " companions");
		}
	}

	public void changeTab(String tableType, int numberOfTables) throws TableNotFoundException, NumberNegativeException {
		int smallTable = this.dispatcher.getSmallTable().getNumTables();
		int mediumTable = this.dispatcher.getMediumTable().getNumTables();
		int largeTable = this.dispatcher.getMediumTable().getNumTables();
		int extraLargeTable = this.dispatcher.getMediumTable().getNumTables();

		if (numberOfTables < 0)
			throw new NumberNegativeException();
		if (tableType.equals(this.dispatcher.SMALL_TABLE_TYPE))
			smallTable = numberOfTables;
		else if (tableType.equals(this.dispatcher.MEDIUM_TABLE_TYPE))
			mediumTable = numberOfTables;
		else if (tableType.equals(this.dispatcher.LARGE_TABLE_TYPE))
			largeTable = numberOfTables;
		else if (tableType.equals(this.dispatcher.EXTRA_LARGE_TABLE_TYPE))
			extraLargeTable = numberOfTables;
		else
			throw new TableNotFoundException();

		new Restaurant(smallTable, mediumTable, largeTable, extraLargeTable);
		System.out.println("Now there are" + numberOfTables + "of type" + tableType);
	}

	public Dispatcher getDispatcher() {
		return dispatcher;
	}

	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}
	
	
}