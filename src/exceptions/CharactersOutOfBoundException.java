package exceptions;

public class CharactersOutOfBoundException extends Exception{

	public CharactersOutOfBoundException() {
		super("Total characters must be less than 10");
	}

}
