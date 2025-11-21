package ie.tus.oop;

public class InvalidChoiceException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidChoiceException(String errorMessage) {
		super(ConsoleUtils.RED + errorMessage + ConsoleUtils.RESET);
	}
}
