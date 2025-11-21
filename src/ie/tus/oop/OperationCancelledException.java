package ie.tus.oop;

/**
 * Exception thrown when the user cancels an operation. This is a
 * RuntimeException that automatically formats the error message with color.
 *
 * @author Stjepan Tadic
 */
public class OperationCancelledException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new OperationCancelledException with the specified error
	 * message. The message is automatically formatted with red color.
	 *
	 * @param errorMessage the error message to display
	 */
	public OperationCancelledException(String errorMessage) {
		super(ConsoleUtils.RED + errorMessage + ConsoleUtils.RESET);
	}
}
