package ie.tus.oop;

/**
 * Exception thrown when attempting to rent or return a vehicle that is not
 * available. This is a checked exception that automatically formats the error
 * message with color.
 *
 * @author Stjepan Tadic
 */
public class VehicleNotAvailableException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new VehicleNotAvailableException with the specified error
	 * message. The message is automatically formatted with red color.
	 *
	 * @param errorMessage the error message to display
	 */
	public VehicleNotAvailableException(String errorMessage) {
		super(ConsoleUtils.RED + errorMessage + ConsoleUtils.RESET);
	}
}
