package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import businessLogic.Customer;
import businessLogic.TableType;

class TableTypeTest {

	private TableType tableType = new TableType(2, "SMALL", 2);
	private Customer customerSmall = new Customer("Small", 2);
	private Customer customerSmall2 = new Customer("Small2", 2);
	private Customer customerSmall3 = new Customer("Small3", 2);
	
	@Test
	void testTableTypeTest() {
		//25 
		TableType tableType2 = new TableType(1, "EXTRALARGE", 5);
		assertTrue(tableType2.getTables().size()>0);
		//25 false
		TableType tableTypeNull = new TableType(0, "LARGE", 5);
		assertTrue(tableTypeNull.getTables().size()==0);
	}
	
	@Test
	void testBookTable() {
		String id = tableType.bookTable(customerSmall);
		//32
		assertEquals(tableType.getTables().get(0).getId(), id);
		tableType.bookTable(customerSmall2);
		//32 False
		String idNull = tableType.bookTable(customerSmall3);
		assertNull(idNull);
	}

	@Test
	void testAddNewDay() {
		//45
		tableType.addNewDay(2);
		assertEquals(2, tableType.getTables().get(0).getDailyRevenue().get(1).getDay());
		// 25 false
		TableType tableTypeNull = new TableType(0, "LARGE", 5);
		tableTypeNull.addNewDay(2);
		assertTrue(tableTypeNull.getTables().size()==0);
	}

	@Test
	void testGetDailyRevenues() {
		fail("Not yet implemented");
	}

	@Test
	void testAlreadyExistsCustomerInQueue() {
		fail("Not yet implemented");
	}

	@Test
	void testFreeTable() {
		fail("Not yet implemented");
	}

	@Test
	void testAddToQueue() {
		fail("Not yet implemented");
	}

	@Test
	void testFindAndRemoveCustomerInQueued() {
		fail("Not yet implemented");
	}

	@Test
	void testGetQueue() {
		fail("Not yet implemented");
	}

	@Test
	void testSetQueue() {
		fail("Not yet implemented");
	}

	@Test
	void testGetNumAvailableTables() {
		fail("Not yet implemented");
	}

	@Test
	void testSetNumAvailableTables() {
		fail("Not yet implemented");
	}

	@Test
	void testGetType() {
		fail("Not yet implemented");
	}

	@Test
	void testSetType() {
		fail("Not yet implemented");
	}

	@Test
	void testGetCapacity() {
		fail("Not yet implemented");
	}

	@Test
	void testSetCapacity() {
		fail("Not yet implemented");
	}

	@Test
	void testGetTables() {
		fail("Not yet implemented");
	}

	@Test
	void testSetTables() {
		fail("Not yet implemented");
	}

	@Test
	void testGetNumTables() {
		fail("Not yet implemented");
	}

	@Test
	void testSetNumTables() {
		fail("Not yet implemented");
	}

}
