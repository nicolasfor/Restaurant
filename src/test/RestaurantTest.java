package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;

import businessLogic.Customer;
import businessLogic.Dispatcher;
import businessLogic.Restaurant;
import exceptions.CharactersOutOfBoundException;
import exceptions.CustomerAlreadyExistsException;
import exceptions.NotValidFileFormatException;
import exceptions.NumberNegativeException;
import exceptions.OutOfBoundQueueException;

class RestaurantTest {

	Path path = Paths.get("tables.txt");

	private Restaurant rest1 = new Restaurant(1, 2, 3, 1);
	private Dispatcher dispatcher = new Dispatcher(1, 2, 3, 1);
	private Customer customerSmall = new Customer("Small1", 1);
	private Customer customerSmall2 = new Customer("Small2", 1);
	private Customer customerMedium = new Customer("Medium1", 3);
	private Customer customerMedium2 = new Customer("Medium2", 4);
	private Customer customerMedium3 = new Customer("Medium3", 4);
	private Customer customerLarge = new Customer("Large1", 5);
	private Customer customerLarge2 = new Customer("Large2", 5);
	private Customer customerLarge3 = new Customer("Large3", 5);
	private Customer customerLarge4 = new Customer("Large4", 5);
	private Customer customerExtraLarge = new Customer("ExtraL1", 9);
	private Customer customerExtraLarge2 = new Customer("ExtraL2", 9);
	private Customer customerExtraLarge3 = new Customer("ExtraL3", 9);

	@Test
	void testRestaurant() {
		fail("Not yet implemented");
	}

	@Test
	void testStart() {
		fail("Not yet implemented");
	}

	@Test
	void testReadLineByLineAndSplit() throws IOException {
		// 123
		System.out.println(path.getName(0));
		BufferedReader reader = Files.newBufferedReader(path, Charset.defaultCharset());
		assertNotNull(reader);

		// 125
		String[] expectedTableType = { "SMALL", "MEDIUM", "LARGE", "EXTRALARGE" };
		String[] lineRead = reader.readLine().split("-");
		assertTrue(lineRead[0].equals(expectedTableType[0]));
		lineRead = reader.readLine().split("-");
		assertTrue(lineRead[0].equals(expectedTableType[1]));
		lineRead = reader.readLine().split("-");
		assertTrue(lineRead[0].equals(expectedTableType[2]));
		lineRead = reader.readLine().split("-");
		assertTrue(lineRead[0].equals(expectedTableType[3]));

	}

	@Test
	void testAssign() throws OutOfBoundQueueException, CustomerAlreadyExistsException, NumberNegativeException,
			CharactersOutOfBoundException {
		//151 Restaurant
		assertThrows(NumberNegativeException.class, ()->{rest1.assign("Small1", -2);});
		//153 Restaurant
		assertThrows(CharactersOutOfBoundException.class, 
				()->{rest1.assign("A very large name to be processed by this program ", 2);});
		// 71 dispatcher
		assertEquals(rest1.dispatcher.getLargeTable().getTables().get(0).getId(), rest1.assign("Large1", 5));// CL1
		rest1.assign("Small1", 2);
		// 75 dispatcher
		assertEquals(rest1.dispatcher.getMediumTable().getTables().get(0).getId(), rest1.assign("Medium1", 3));// CM1
		// 76,81 dispatcher
		rest1.assign("ExtraL1", 10);// CXL1"
		assertNull(rest1.assign("ExtraL2", 8));// CXL2
		rest1.assign("ExtraL3", 11);// CXL3
		LinkedList<Customer> queue = rest1.dispatcher.getExtraLargeTable().getQueue();
		boolean found = false;
		for (int i = 0; i < queue.size(); i++) {
			if (queue.get(i).getName() == "ExtraL3")// CXL3
				found = true;
		}
		assertTrue(found);
		// System.out.println("el primero en la cola es:" + queue.getFirst().getName());
		// 76,79
		assertThrows(CustomerAlreadyExistsException.class, () -> {
			rest1.assign("ExtraL2", 9);
		});

		// 85
		//System.out.println("el ultimo en la cola es:" + queue.getLast().getName());
		for (int i = 4; i < 22; i++) {
			rest1.assign("ExtraL" + i, 14);
			//System.out.println("el ultimo en la cola es:" + queue.getLast().getName());
		}		
		//System.out.println("el tamaño de la cola es:" + rest1.dispatcher.getExtraLargeTable().getQueue().size());
		assertThrows(OutOfBoundQueueException.class, () -> {
			rest1.assign("ExtraL"+ 22, 14);
		});
	}

	@Test
	void testFree() {
		fail("Not yet implemented");
	}

	@Test
	void testCancelRes() {
		fail("Not yet implemented");
	}

	@Test
	void testShowTab_status() {
		fail("Not yet implemented");
	}

	@Test
	void testShowTab_users() {
		fail("Not yet implemented");
	}

	@Test
	void testEndDay() {
		fail("Not yet implemented");
	}

	@Test
	void testShowRev() {
		fail("Not yet implemented");
	}

	@Test
	void testPrintDailyReport() {
		fail("Not yet implemented");
	}

	@Test
	void testShowRes() {
		fail("Not yet implemented");
	}

	@Test
	void testChangeTab() {
		fail("Not yet implemented");
	}

}
