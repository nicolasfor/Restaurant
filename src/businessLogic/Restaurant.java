package businessLogic;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
		this.dispatcher = new Dispatcher(s,m,l,xl);
	}

	public static void main(String[] args) throws IOException {
		
		
		File file = new File("C:\\Users\\Felia\\git\\Restaurant\\tables.txt");

		if (file.exists()) {
			
			System.out.println("existe");

			System.out.print("file " + file.getName() + " was found in " + file.getPath());

			List<Integer> allValues;		    		   
		    
		    try(Stream<String> lines=Files.lines(file.toPath())) {
		    allValues=lines.flatMap(Pattern.compile(" *")::splitAsStream)
		         .filter(s -> s.matches("[0-9]+"))
		         .map(Integer::valueOf)
		         .collect(Collectors.toList());
			
		    allValues.forEach(v->System.out.println(v));
		    
			}
			
				Restaurant t = new Restaurant(allValues.get(0),allValues.get(1),
						allValues.get(2),allValues.get(3));
				t.test();
		
		}				       
	}

	String readLineByLineJava8(String filePath) throws IOException {
		System.out.println("filepath is: " + filePath);

		StringBuilder contentBuilder = new StringBuilder();

		Path file = Paths.get("C:\\Users\\Felia\\git\\Restaurant\\tables.txt");

		try (Stream<String> stream = Files.lines(file)) {
			stream.forEach(s -> contentBuilder.append(s).append("\n"));
		}
		
		return contentBuilder.toString();
		
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