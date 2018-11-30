package exceptions;

@SuppressWarnings("serial")
public class CustomerNotFoundException extends Exception{

	public CustomerNotFoundException() 
	{
		super("Customer does not exist");
	}
	
}
