package ie.tus.oop;

/**
 * Enumeration representing the different fuel types for vehicles.
 *
 * @author Stjepan Tadic
 */
public enum FuelType {
	/** Petrol fuel type */
	PETROL,
	/** Diesel fuel type */
	DIESEL,
	/** Electric fuel type */
	ELECTRIC,
	/** Hybrid fuel type */
	HYBRID,
	/** No fuel (for manual bikes) */
	NONE;

	/**
	 * Returns the lowercase string representation of the fuel type.
	 *
	 * @return the fuel type name in lowercase
	 */
	@Override
	public String toString() {
		return this.name().toLowerCase();
	}
}
