package malictus.konverti;

//a simple class to catch errors from the console
public class ConsoleException extends Exception {

	public ConsoleException() {
		super();
	}
	
	public ConsoleException(String err) {
		super(err);
	}
	
}
