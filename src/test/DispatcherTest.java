package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import businessLogic.Customer;
import businessLogic.Dispatcher;
import businessLogic.TableType;
import exceptions.CustomerAlreadyExistsException;
import exceptions.CustomerNotFoundException;
import exceptions.OutOfBoundQueueException;
import exceptions.TableNotFoundException;

class DispatcherTest {

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
	void testGetTableTypeByCapacity() {
		//43
		assertEquals(dispatcher.getSmallTable(), dispatcher.getTableTypeByCapacity(1));
		//45
		assertEquals(dispatcher.getMediumTable(), dispatcher.getTableTypeByCapacity(3));
		//47
		assertEquals(dispatcher.getLargeTable(), dispatcher.getTableTypeByCapacity(5));
		//49
		assertEquals(dispatcher.getExtraLargeTable(), dispatcher.getTableTypeByCapacity(9));
	}

	@Test
	void testGetTableTypeByType() throws TableNotFoundException {
		// 54
		assertEquals(dispatcher.getSmallTable(), dispatcher.getTableTypeByType(dispatcher.SMALL_TABLE_TYPE));
		//56
		assertEquals(dispatcher.getMediumTable(), dispatcher.getTableTypeByType(dispatcher.MEDIUM_TABLE_TYPE));
		//58
		assertEquals(dispatcher.getLargeTable(), dispatcher.getTableTypeByType(dispatcher.LARGE_TABLE_TYPE));
		//60
		assertEquals(dispatcher.getExtraLargeTable(), dispatcher.getTableTypeByType(dispatcher.EXTRA_LARGE_TABLE_TYPE));
		//62
		assertThrows(TableNotFoundException.class, () -> {
			dispatcher.getTableTypeByType("ffsdfsd");
		});
		
	}

	@Test
	void testAssignCustomer() throws OutOfBoundQueueException, CustomerAlreadyExistsException {
		//71 
		assertEquals(dispatcher.getLargeTable().getTables().get(0).getId(), dispatcher.assignCustomer(customerLarge));
		dispatcher.assignCustomer(customerSmall);
		//75
		assertEquals(dispatcher.getMediumTable().getTables().get(0).getId(), dispatcher.assignCustomer(customerSmall));
		//76,81
		dispatcher.assignCustomer(customerExtraLarge);
		assertNull(dispatcher.assignCustomer(customerExtraLarge2));
		dispatcher.assignCustomer(customerExtraLarge3);
		LinkedList<Customer> queue = dispatcher.getExtraLargeTable().getQueue();
		boolean found = false;
		for (int i = 0; i < queue.size(); i++) {
			if(queue.get(i).getName()== customerExtraLarge3.getName())
				found = true;
		}
		assertTrue(found);
		
		//76,79
		assertThrows(CustomerAlreadyExistsException.class, () -> {
			dispatcher.assignCustomer(customerExtraLarge2);
		});
		
		//85
		for (int i = 0; i < 18; i++) {
			dispatcher.assignCustomer(new Customer("juan"+i, 14));
		}
		assertThrows(OutOfBoundQueueException.class, () -> {
			dispatcher.assignCustomer(new Customer("juan"+18, 14));
		});
	}

	@Test
	void testCheckAndAssignOtherTables() throws OutOfBoundQueueException, CustomerAlreadyExistsException, TableNotFoundException {
		dispatcher.assignCustomer(customerSmall);
		dispatcher.assignCustomer(customerMedium);	
		dispatcher.assignCustomer(customerMedium2);	
		dispatcher.assignCustomer(customerLarge);	
		dispatcher.assignCustomer(customerLarge2);	
		dispatcher.assignCustomer(customerLarge3);	
		//105,107,109
		assertEquals(dispatcher.getExtraLargeTable().getTables().get(0).getId(), dispatcher.checkAndAssignOtherTables(new Customer("Daniel", 3)));
		LinkedList<Customer> queue = dispatcher.getLargeTable().getQueue();
		//105,113
		dispatcher.releaseTable(dispatcher.getLargeTable().getTables().get(0).getId(), 10);
		assertEquals(dispatcher.getLargeTable().getTables().get(0).getId(), dispatcher.checkAndAssignOtherTables(new Customer("Daniel2", 3)));
		//115
		dispatcher.releaseTable(dispatcher.getMediumTable().getTables().get(0).getId(), 10);
		assertEquals(dispatcher.getMediumTable().getTables().get(0).getId(), dispatcher.checkAndAssignOtherTables(new Customer("Daniel2", 3)));
	}

	@Test
	void testReleaseTable() throws TableNotFoundException, OutOfBoundQueueException, CustomerAlreadyExistsException {
		//123,126,129
		dispatcher.assignCustomer(customerSmall);
		dispatcher.releaseTable(dispatcher.getSmallTable().getTables().get(0).getId(), 20);
		assertFalse(dispatcher.getSmallTable().getTables().get(0).isBusy());
		//123,126,133
		assertThrows(TableNotFoundException.class, () -> {
			dispatcher.releaseTable("Table-5", 20);
		});
		//123,134
		assertThrows(TableNotFoundException.class, () -> {
			dispatcher.releaseTable("-12", 20);
		});
		//137
		assertThrows(TableNotFoundException.class, () -> {
			dispatcher.releaseTable("dasd", 20);
		});
	}

	@Test
	void testShuffleTables() throws TableNotFoundException, OutOfBoundQueueException, CustomerAlreadyExistsException {
		//142,145
		dispatcher.assignCustomer(customerExtraLarge);
		dispatcher.assignCustomer(customerExtraLarge2);
		Customer customerInQueue = dispatcher.getExtraLargeTable().getQueue().get(0);
		dispatcher.releaseTable(dispatcher.getExtraLargeTable().getTables().get(0).getId(), 10);
		assertEquals(customerInQueue.getName(),dispatcher.getExtraLargeTable().getTables().get(0).getCustomer().getName());
		//142,149 UNREACHABLE porque mesa liberada siempre va a tener disponibilidad (freeTable) cuando se ejecuta el metodo
		//150
		assertTrue(dispatcher.getExtraLargeTable().getQueue().size()==0);
		dispatcher.releaseTable(dispatcher.getExtraLargeTable().getTables().get(0).getId(), 10);
		assertTrue(dispatcher.getExtraLargeTable().getQueue().size()==0);
		
	}

	@Test
	void testCancelReservation() throws OutOfBoundQueueException, CustomerAlreadyExistsException, CustomerNotFoundException {
		
		dispatcher.assignCustomer(customerExtraLarge);
		dispatcher.assignCustomer(customerExtraLarge2);
		dispatcher.assignCustomer(customerLarge);
		dispatcher.assignCustomer(customerLarge2);
		dispatcher.assignCustomer(customerLarge3);
		dispatcher.assignCustomer(customerLarge4);
		dispatcher.assignCustomer(customerMedium);
		dispatcher.assignCustomer(customerMedium2);
		dispatcher.assignCustomer(customerMedium3);
		dispatcher.assignCustomer(customerSmall);
		dispatcher.assignCustomer(customerSmall2);
		//157
		assertTrue(dispatcher.getMediumTable().getQueue().size()>0);
		dispatcher.cancelReservation(customerMedium3.getName());
		assertTrue(dispatcher.getMediumTable().getQueue().size()==0);
		//157 en true
		assertTrue(dispatcher.getSmallTable().getQueue().size()>0);
		dispatcher.cancelReservation(customerSmall2.getName());
		assertTrue(dispatcher.getSmallTable().getQueue().size()==0);
		//159
		assertTrue(dispatcher.getLargeTable().getQueue().size()>0);
		dispatcher.cancelReservation(customerLarge4.getName());
		assertTrue(dispatcher.getLargeTable().getQueue().size()==0);
		//161
		assertTrue(dispatcher.getExtraLargeTable().getQueue().size()>0);
		dispatcher.cancelReservation(customerExtraLarge2.getName());
		assertTrue(dispatcher.getExtraLargeTable().getQueue().size()==0);
		//164
		assertThrows(CustomerNotFoundException.class, () -> {
			dispatcher.cancelReservation("dasd");
		});
	}

}
