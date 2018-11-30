package exceptions;

public class CustomerAlreadyExistsException extends Exception{

	public CustomerAlreadyExistsException() {
		super("Customer already exists in the queue");
	}

}
