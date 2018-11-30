package exceptions;

@SuppressWarnings("serial")
public class TableNotFoundException extends Exception{

	public TableNotFoundException() {
		super("Table does not exist");
	}

}
