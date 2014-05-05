package malictus.konverti;

/**
 * A simple class to catch errors from the command line.
 * @author Jim Halliday
 */
public class ConsoleException extends Exception {

	public ConsoleException() {
		super();
	}
	
	public ConsoleException(String err) {
		super(err);
	}
	
}
