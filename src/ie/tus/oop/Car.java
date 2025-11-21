package ie.tus.oop;

/**
 * Represents a car in the vehicle rental system. This final class extends
 * Vehicle and includes car-specific features such as air conditioning and
 * navigation systems.
 *
 * @author Stjepan Tadic
 */
public final class Car extends Vehicle {
	private boolean airConditioning;
	private boolean navigation;

	/**
	 * Constructs a new Car with full specifications.
	 *
	 * @param make               the manufacturer of the car
	 * @param model              the model name of the car
	 * @param colour             the colour of the car
	 * @param fuelType           the fuel type used by the car
	 * @param dailyRate          the daily rental rate for the car
	 * @param hasAirConditioning true if the car has air conditioning
	 * @param hasNavigation      true if the car has a navigation system
	 * @throws IllegalArgumentException if dailyRate is not positive
	 */
	public Car(String make, String model, String colour, FuelType fuelType, double dailyRate,
			boolean hasAirConditioning, boolean hasNavigation) {
		// Flexible constructor body demonstration
		Vehicle.validateDailyRate(dailyRate);

		super(make, model, colour, fuelType, dailyRate);
		super.setVehicleType(VehicleType.CAR);
		this.airConditioning = hasAirConditioning;
		this.navigation = hasNavigation;
	}

	/**
	 * Constructs a new Car with basic specifications. Air conditioning and
	 * navigation are set to false by default.
	 *
	 * @param make      the manufacturer of the car
	 * @param model     the model name of the car
	 * @param colour    the colour of the car
	 * @param fuelType  the fuel type used by the car
	 * @param dailyRate the daily rental rate for the car
	 */
	public Car(String make, String model, String colour, FuelType fuelType, double dailyRate) {
		this(make, model, colour, fuelType, dailyRate, false, false);
	}

	/**
	 * Checks if this car has air conditioning.
	 *
	 * @return true if the car has air conditioning, false otherwise
	 */
	public boolean isAirConditioning() {
		return airConditioning;
	}

	/**
	 * Checks if this car has a navigation system.
	 *
	 * @return true if the car has navigation, false otherwise
	 */
	public boolean isNavigation() {
		return navigation;
	}

	/**
	 * Gets the car-specific specifications.
	 *
	 * @return a formatted string containing air conditioning and navigation status
	 */
	@Override
	public String getSpecs() {
		return String.format("    | AC: %s, Navigation: %s", isAirConditioning(), isNavigation());
	}

}
