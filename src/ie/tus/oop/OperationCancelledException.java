package ie.tus.oop;

public class OperationCancelledException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public OperationCancelledException(String errorMessage) {
		super(errorMessage);
	}
}
