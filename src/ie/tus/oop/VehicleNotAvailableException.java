package ie.tus.oop;

public class VehicleNotAvailableException extends Exception {
	private static final long serialVersionUID = 1L;

	public VehicleNotAvailableException(String errorMessage) {
		super(errorMessage);
	}
}
