package exceptions;

public class NumberNegativeException extends Exception{

	public NumberNegativeException() {
		super("Number must be positive");
	}

}
