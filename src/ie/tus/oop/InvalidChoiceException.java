package ie.tus.oop;

/**
 * Exception thrown when the user makes an invalid menu choice. This is a
 * RuntimeException that automatically formats the error message with color.
 *
 * @author Stjepan Tadic
 */
public class InvalidChoiceException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new InvalidChoiceException with the specified error message. The
	 * message is automatically formatted with red color.
	 *
	 * @param errorMessage the error message to display
	 */
	public InvalidChoiceException(String errorMessage) {
		super(ConsoleUtils.RED + errorMessage + ConsoleUtils.RESET);
	}
}
