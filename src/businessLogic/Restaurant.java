package businessLogic;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Restaurant {

	private ArrayList<Table> smallTables;
	private ArrayList<Table> mediumTables;
	private ArrayList<Table> largeTables;
	private ArrayList<Table> extraLargeTables;

	public Restaurant() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException {
		
		System.out.println("hola "+ args[0]);
		
		for (String file : args) {
		File f = new File(file);
		
		System.out.println("file name: " + f.getName());
        System.out.println("Path name: " + f.getPath());
        

		if (f.exists()) {
			
			System.out.println("existe");

			System.out.print("file " + f.getName() + " was found in " + f.getPath());

			Restaurant resume = new Restaurant();

			String lista;
			
			lista = resume.readLineByLineJava8(f.getName());
			System.out.println("Estos son los elementos de lista:\n" + lista);
			
			Stream<String> stream = Stream.of(lista.toLowerCase().replaceAll(lista, lista).split("\\W+")).parallel();
			
			Map<String, Long> wordFreq = stream.filter(w -> w.matches("medium")).parallel()
					.filter(w -> !w.matches("-?\\d+(\\.\\d+)?")
					.collect(Collectors.groupingBy(String::toString, Collectors.counting()));		
			}
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
}