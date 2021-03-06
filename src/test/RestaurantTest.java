package test;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import org.junit.After;
import org.junit.Before;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import org.junit.jupiter.api.Test;

import businessLogic.Dispatcher;
import businessLogic.Restaurant;
import exceptions.CharactersOutOfBoundException;
import exceptions.NumberNegativeException;
import exceptions.TableNotFoundException;
import businessLogic.Customer;
import exceptions.CustomerAlreadyExistsException;
import exceptions.OutOfBoundQueueException;

class RestaurantTest {
	
	private Restaurant restaurant = new Restaurant(1, 2, 2, 2);
	//private Restaurant restaurant = new Restaurant(1, 2, 3, 1);
	private Dispatcher dispatcher = restaurant.getDispatcher();
	private String assignSmall = "ASSIGN Small 2";
	private String assignSmallNegative = "ASSIGN Small -2";
	private String assignExtra = "ASSIGN Extra1 12";
	private String assignExtra2 = "ASSIGN Extra2 12";
	private String assignExtra3 = "ASSIGN Extra3 12";
	private String freeSmall = "FREE SMALL-0 10";
	private String cancelExtra = "CANCEL_RES Extra3";
	private String endDay = "END_DAY";
	private String showRev = "SHOW_REV";
	private String showTabStatus = "SHOW_TAB STATUS";
	private String showTabUserExtra = "SHOW_TAB USERS EXTRA_LARGE";
	private String showTabBad = "SHOW_TAB BAD EXTRA_LARGE";
	private String showRes = "SHOW_RES";
	private String changeTab = "CHANGE_TAB LARGE 2";
	private String changeTabNegative = "CHANGE_TAB LARGE -2";
	private String help = "HELP";
	private String bad = "BAD";
	
	private final InputStream systemIn = System.in;

    private ByteArrayInputStream testIn;
    Path path = Paths.get("tables.txt");

	

    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }


    @After
    public void restoreSystemInputOutput() {
        System.setIn(systemIn);
    }

    @Test
	void fileReader() throws IOException {
		
		assertThrows(NullPointerException.class, () -> {Restaurant.fileReader(null);});
		Restaurant.fileReader(Paths.get("emptyFile.txt"));
		Restaurant.fileReader(Paths.get("fakeFile.txt"));
		Restaurant.fileReader(Paths.get("tables.txt"));
    }


	@Test
	void main() {
		Restaurant.main(null);
	}

	@Test
	void testStart() throws IOException {
		
        provideInput(assignSmall);
        restaurant.start();
        //60
        assertEquals("Small",dispatcher.getSmallTable().getTables().get(0).getCustomer().getName());
		assertEquals("SMALL-0", dispatcher.getSmallTable().getTables().get(0).getId());
		//65
		provideInput(assignSmallNegative);
		restaurant.start();
		assertFalse(dispatcher.getMediumTable().getTables().get(0).isBusy());
		//69
		 provideInput(freeSmall);
	        restaurant.start();
	     assertFalse(dispatcher.getSmallTable().getTables().get(0).isBusy()); 
	     //72
		provideInput(assignExtra);
		restaurant.start();
		provideInput(assignExtra2);
		restaurant.start();
		provideInput(assignExtra3);
		restaurant.start();
		assertEquals("Extra3", dispatcher.getExtraLargeTable().getQueue().get(0).getName());
		provideInput(cancelExtra);
		restaurant.start();
		assertTrue(dispatcher.getExtraLargeTable().getQueue().size()==0);
		
		//75
		provideInput(endDay);
		restaurant.start();
		assertTrue( dispatcher.getExtraLargeTable().getTables().get(0).getDailyRevenue().size()==2);
		assertEquals( 2,dispatcher.getExtraLargeTable().getTables().get(0).getDailyRevenue().get(1).getDay());
		//78
		provideInput(showRev);
		restaurant.start();
		assertEquals(10,dispatcher.getSmallTable().getTables().get(0).getDailyRevenue().get(0).getRevenue());
		
		//81,82
		provideInput(showTabStatus);
		restaurant.start();
		assertFalse(dispatcher.getSmallTable().getTables().get(0).isBusy());
		assertFalse(dispatcher.getMediumTable().getTables().get(0).isBusy());
		assertFalse(dispatcher.getMediumTable().getTables().get(1).isBusy());
		assertFalse(dispatcher.getLargeTable().getTables().get(0).isBusy());
		assertTrue(dispatcher.getExtraLargeTable().getTables().get(0).isBusy());
		assertTrue(dispatcher.getExtraLargeTable().getTables().get(1).isBusy());
		
		//81,84
		provideInput(showTabUserExtra);
		restaurant.start();
		assertTrue(dispatcher.getExtraLargeTable().getTables().get(0).isBusy());
		assertTrue(dispatcher.getExtraLargeTable().getTables().get(1).isBusy());
	
		provideInput(showTabBad);
		restaurant.start();
		
		//88
		provideInput(assignExtra3);
		restaurant.start();
		provideInput(showRes);
		restaurant.start();
		assertEquals(1,dispatcher.getExtraLargeTable().getQueue().size());
		
		//91
		provideInput(changeTab);
		restaurant.start();
		provideInput(changeTabNegative);
		restaurant.start();
		
		//98
		provideInput(help);
		restaurant.start();
		
		provideInput(bad);
		//98
		provideInput(" ");
		restaurant.start();
	}

	@Test
	void testReadLineByLineAndSplit() throws IOException {
		// 123		
		
		String smallLine = "SMALL-2";
		String mediumLine = "MEDIUM-2";
		String largeLine = "LARGE-2";
		String extraLargeLine = "EXTRALARGE-2";
		
		String [] smallExpected = smallLine.split("-");						
		restaurant.readLineByLineAndSplit(smallLine);
		assertEquals(Integer.parseInt(smallExpected[1]),restaurant.smallTables);
		
		String [] mediumExpected = mediumLine.split("-");
		restaurant.readLineByLineAndSplit(mediumLine);
		assertEquals(Integer.parseInt(mediumExpected[1]),restaurant.mediumTables);
		
		String [] largeExpected = largeLine.split("-");
		restaurant.readLineByLineAndSplit(largeLine);
		assertEquals(Integer.parseInt(largeExpected[1]),restaurant.largeTables);
		
		String [] extraLargeExpected = extraLargeLine.split("-");
		restaurant.readLineByLineAndSplit(extraLargeLine);
		assertEquals(Integer.parseInt(extraLargeExpected[1]),restaurant.extraLargeTables);
	}

	@Test
	void testAssign() throws OutOfBoundQueueException, CustomerAlreadyExistsException, NumberNegativeException,
			CharactersOutOfBoundException {
		//151 Restaurant
		assertThrows(NumberNegativeException.class, ()->{restaurant.assign("Small1", -2);});
		//153 Restaurant
		assertThrows(CharactersOutOfBoundException.class, 
				()->{restaurant.assign("A very large name to be processed by this program ", 2);});
		// 71 dispatcher
		assertEquals(restaurant.dispatcher.getLargeTable().getTables().get(0).getId(), restaurant.assign("Large1", 5));// CL1
		restaurant.assign("Small1", 2);
		restaurant.assign("Small2", 2);
		// 75 dispatcher
		assertEquals(restaurant.dispatcher.getMediumTable().getTables().get(1).getId(), restaurant.assign("Medium1", 3));// CM1
		
		restaurant.assign("Medium", 4);
		restaurant.assign("Medium2", 4);
		restaurant.assign("Large1", 6);
		
		// 76,81 dispatcher
		restaurant.assign("ExtraL1", 10);// CXL1"
		assertNull(restaurant.assign("ExtraL2", 8));// CXL2
		restaurant.assign("ExtraL3", 11);// CXL3
		LinkedList<Customer> queue = restaurant.dispatcher.getExtraLargeTable().getQueue();
		boolean found = false;
		for (int i = 0; i < queue.size(); i++) {
			if (queue.get(i).getName() == "ExtraL3")// CXL3
				found = true;
		}
		assertTrue(found);
		// System.out.println("el primero en la cola es:" + queue.getFirst().getName());
		// 76,79
		assertThrows(CustomerAlreadyExistsException.class, () -> {
			restaurant.assign("ExtraL2", 9);
		});

		// 85
		//System.out.println("el ultimo en la cola es:" + queue.getLast().getName());
		for (int i = 4; i < 21; i++) {
			restaurant.assign("ExtraL" + i, 14);
			//System.out.println("el ultimo en la cola es:" + queue.getLast().getName());
		}		
		//System.out.println("el tama�o de la cola es:" + rest1.dispatcher.getExtraLargeTable().getQueue().size());
		assertThrows(OutOfBoundQueueException.class, () -> {
			restaurant.assign("ExtraL"+ 22, 14);
		});
	}

	@Test
	void testFree() throws NumberNegativeException, CharactersOutOfBoundException, CustomerAlreadyExistsException, OutOfBoundQueueException {
		//166
		restaurant.assign("Extra", 12);
		restaurant.assign("Extra2", 12);
		restaurant.assign("Extra3", 12);
		assertTrue(dispatcher.getExtraLargeTable().getTables().get(0).isBusy());
		restaurant.free("EXTRA_LARGE-0", 10);
		assertTrue(dispatcher.getExtraLargeTable().getTables().get(0).isBusy());
		restaurant.free("EXTRA_LARGE-0", 10);
		assertFalse(dispatcher.getExtraLargeTable().getTables().get(0).isBusy());
		//169
		restaurant.assign("Extra2", 12);
		assertTrue(dispatcher.getExtraLargeTable().getTables().get(0).isBusy());
		restaurant.free("EXTRA_LARGE-5", 10);
		restaurant.free("", 10);
		assertTrue(dispatcher.getExtraLargeTable().getTables().get(0).isBusy());
		
		restaurant.assign("MEDIUM1", 3);
		restaurant.free("MEDIUM-0", 10);
		
		restaurant.assign("LARGE0", 5);
		restaurant.free("LARGE-0", 10);
		
		restaurant.free("LARGE-0", 10);

	}

	@Test
	void testCancelRes() throws NumberNegativeException, CharactersOutOfBoundException, CustomerAlreadyExistsException, OutOfBoundQueueException {
		//166
		restaurant.assign("Extra", 12);
		restaurant.assign("Extra2", 10);
		restaurant.assign("Extra3", 9);
		assertEquals(1,dispatcher.getExtraLargeTable().getQueue().size());
		restaurant.cancelRes("Extra3");
		assertEquals(0,dispatcher.getExtraLargeTable().getQueue().size());
		//178
		restaurant.assign("Extra3", 9);
		restaurant.cancelRes("Extraad3");
	}

	@Test
	void testShowTab_status() {
		restaurant.showTab_status();
	}

	@Test
	void testShowTab_users() {
		restaurant.showTab_users("SMALL");
		restaurant.showTab_users("SMALLBAD");
	}

	@Test
	void testEndDay() {
		restaurant.endDay();
		assertTrue( dispatcher.getSmallTable().getTables().get(0).getDailyRevenue().size()==2);
		assertTrue( dispatcher.getMediumTable().getTables().get(0).getDailyRevenue().size()==2);
		assertTrue( dispatcher.getLargeTable().getTables().get(0).getDailyRevenue().size()==2);
		assertTrue( dispatcher.getExtraLargeTable().getTables().get(0).getDailyRevenue().size()==2);
	}

	@Test
	void testShowRev() throws NumberNegativeException, CharactersOutOfBoundException, CustomerAlreadyExistsException, OutOfBoundQueueException {
		restaurant.assign("Small", 2);
		restaurant.free("SMALL-0", 10);
		restaurant.showRev();
		assertEquals(10,dispatcher.getSmallTable().getTables().get(0).getDailyRevenue().get(0).getRevenue());
	}

	@Test
	void testPrintDailyReport() throws NumberNegativeException, CharactersOutOfBoundException, CustomerAlreadyExistsException, OutOfBoundQueueException {
		restaurant.assign("Small", 2);
		restaurant.free("SMALL-0", 10);
		for (int i = 0; i < 7; i++) {
			restaurant.endDay();
		}
		restaurant.printDailyReport(dispatcher.getSmallTable().getDailyRevenues());
		assertEquals(10,dispatcher.getSmallTable().getTables().get(0).getDailyRevenue().get(0).getRevenue());
	}

	@Test
	void testShowRes() throws NumberNegativeException, CharactersOutOfBoundException, CustomerAlreadyExistsException, OutOfBoundQueueException {
		restaurant.assign("ExtraLarg", 12);
		restaurant.assign("ExtraLarg2", 10);
		restaurant.assign("ExtraLarg3", 10);
		restaurant.assign("Large", 5);
		restaurant.assign("Large2", 5);
		restaurant.assign("Large3", 5);
		restaurant.assign("Medium", 3);
		restaurant.assign("Medium2", 3);
		restaurant.assign("Medium3", 3);
		restaurant.assign("Small", 2);
		restaurant.assign("Small2", 2);
		restaurant.assign("Small3", 2);
		restaurant.showRes();
		assertEquals(2, dispatcher.getSmallTable().getQueue().size());
		assertEquals(1, dispatcher.getMediumTable().getQueue().size());
		assertEquals(1, dispatcher.getLargeTable().getQueue().size());
		assertEquals(1, dispatcher.getExtraLargeTable().getQueue().size());
	}

	@Test
	void testChangeTab() throws TableNotFoundException, NumberNegativeException {
		restaurant.changeTab("SMALL", 2);
		restaurant.changeTab("MEDIUM", 2);
		restaurant.changeTab("LARGE", 2);
		restaurant.changeTab("EXTRA_LARGE", 2);
		
		assertThrows(TableNotFoundException.class,()->{ restaurant.changeTab("BAD_TYPE", 2);});
	}

}
