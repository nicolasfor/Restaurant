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
import org.junit.jupiter.api.Test;

import businessLogic.Dispatcher;
import businessLogic.Restaurant;
import exceptions.CharactersOutOfBoundException;
import exceptions.NotValidFileFormatException;
import exceptions.NumberNegativeException;
import exceptions.TableNotFoundException;

class RestaurantTest {
	
	private Restaurant restaurant = new Restaurant(1, 2, 1, 2);
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
	private String showRes = "SHOW_RES";
	private String changeTab = "CHANGE_TAB LARGE 2";
	private String changeTabNegative = "CHANGE_TAB LARGE -2";
	private String help = "HELP";
	
	private final InputStream systemIn = System.in;

    private ByteArrayInputStream testIn;
	

    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }


    @After
    public void restoreSystemInputOutput() {
        System.setIn(systemIn);
    }


	
	@Test
	void testRestaurant() {
		fail("Not yet implemented");
	}

	@Test
	void testStart() throws IOException, NotValidFileFormatException {
		
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
		
		provideInput("adadad");
	}

	@Test
	void testReadLineByLineAndSplit() {
		fail("Not yet implemented");
	}

	@Test
	void testAssign() {
		fail("Not yet implemented");
	}

	@Test
	void testFree() throws NumberNegativeException, CharactersOutOfBoundException {
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
		
		

	}

	@Test
	void testCancelRes() throws NumberNegativeException, CharactersOutOfBoundException {
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
		fail("Not yet implemented");
	}

	@Test
	void testShowTab_users() {
		fail("Not yet implemented");
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
	void testShowRev() throws NumberNegativeException, CharactersOutOfBoundException {
		restaurant.assign("Small", 2);
		restaurant.free("SMALL-0", 10);
		restaurant.showRev();
		assertEquals(10,dispatcher.getSmallTable().getTables().get(0).getDailyRevenue().get(0).getRevenue());
	}

	@Test
	void testPrintDailyReport() throws NumberNegativeException, CharactersOutOfBoundException {
		restaurant.assign("Small", 2);
		restaurant.free("SMALL-0", 10);
		for (int i = 0; i < 7; i++) {
			restaurant.endDay();
		}
		restaurant.printDailyReport(dispatcher.getSmallTable().getDailyRevenues());
		assertEquals(10,dispatcher.getSmallTable().getTables().get(0).getDailyRevenue().get(0).getRevenue());
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
