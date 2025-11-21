package ie.tus.oop;

/**
 * Represents a bike in the vehicle rental system. This final class extends
 * Vehicle and includes bike-specific features such as wheel size.
 *
 * @author Stjepan Tadic
 */
public final class Bike extends Vehicle {
	private int wheelSize;

	/**
	 * Constructs a new Bike with the specified details.
	 *
	 * @param make      the manufacturer of the bike
	 * @param model     the model name of the bike
	 * @param colour    the colour of the bike
	 * @param fuelType  the fuel type (typically NONE or ELECTRIC for bikes)
	 * @param dailyRate the daily rental rate for the bike
	 * @param wheelSize the wheel size in inches
	 * @throws IllegalArgumentException if dailyRate is not positive
	 */
	public Bike(String make, String model, String colour, FuelType fuelType, double dailyRate, int wheelSize) {
		Vehicle.validateDailyRate(dailyRate);
		super(make, model, colour, fuelType, dailyRate);
		super.setVehicleType(VehicleType.BIKE);
		this.wheelSize = wheelSize;
	}

	/**
	 * Gets the wheel size of this bike.
	 *
	 * @return the wheel size in inches
	 */
	public int getWheelSize() {
		return wheelSize;
	}

	/**
	 * Gets the bike-specific specifications.
	 *
	 * @return a formatted string containing the wheel size
	 */
	@Override
	public String getSpecs() {
		return String.format("    | Size: %d\"", getWheelSize());
	}

}
