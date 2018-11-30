package exceptions;

public class OutOfBoundQueueException extends Exception{

	public OutOfBoundQueueException() {
		super("Queue can only have 20 customers");
	}

}
