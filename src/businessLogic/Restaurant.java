package businessLogic;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Restaurant {

	private Dispatcher dispatcher;

	public Restaurant(Integer s, Integer m, Integer l, Integer xl) {
		// TODO Auto-generated constructor stub
		this.dispatcher = new Dispatcher(s, m, l, xl);
	}

	public static void main(String[] args) throws IOException {

		File file = new File("C:\\Users\\Felia\\git\\Restaurant\\tables.txt");

		if (file.exists()) {

			System.out.println("existe");

			System.out.print("file " + file.getName() + " was found in " + file.getPath());

			List<Integer> allValues;

			try (Stream<String> lines = Files.lines(file.toPath())) {
				allValues = lines.flatMap(Pattern.compile(" *")::splitAsStream).filter(s -> s.matches("[0-9]+"))
						.map(Integer::valueOf).collect(Collectors.toList());
				allValues.forEach(v -> System.out.println(v));
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
						if(line[1]=="STATUS")
						{
							t.showTab_status();
						}else
						if(line[1]=="USERS"){
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
		String enqueued = dispatcher.assignCustomer(c);
		return enqueued;
	}

	public void free(String tableId, int revenue) {
		boolean released = dispatcher.releaseTable(tableId, revenue);
		System.out.println("Table: " + tableId + "has been released!");
	}
	
	public void cancelRes(String name){
		//agregado un nuevo constructor en Customer
		Customer c = new Customer(name);
		//pendiente de crear metodo de cancelacion de reservación
		System.out.println("The reservation of:" + name + "has been cancelled" );
	}
	
	public void showTab_status() {
		System.out.println("the current status of tables of each table type is:");
	}
	
	public void showTab_users(String table_id) {
		System.out.println("Tables taken for type: "+ table_id );
	}
	
	public void endDay() {
		System.out.println("Day ends an bills are counted");
	}
	
	public void showRev() {
		System.out.println("revenue is:");
	}
	
	public void showRes() {
		System.out.println("waiting list including the name and the number of people is:");
	}
	
	public void changeTab(String tableType, int numberOfTables) {
		System.out.println("Now there are" + numberOfTables + "of type" + tableType );
	}	
}