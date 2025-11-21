package ie.tus.oop;

/**
 * Enumeration representing the different types of vehicles available in the
 * rental system.
 *
 * @author Stjepan Tadic
 */
public enum VehicleType {
	/** Car vehicle type */
	CAR,
	/** Van vehicle type */
	VAN,
	/** Bike vehicle type */
	BIKE;

	/**
	 * Returns the lowercase string representation of the vehicle type.
	 *
	 * @return the vehicle type name in lowercase
	 */
	@Override
	public String toString() {
		return this.name().toLowerCase();
	}
}
