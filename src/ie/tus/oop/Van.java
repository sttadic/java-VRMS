package ie.tus.oop;

/**
 * Represents a van in the vehicle rental system. This final class extends
 * Vehicle and includes van-specific features such as cargo capacity.
 *
 * @author Stjepan Tadic
 */
public final class Van extends Vehicle {
	private double cargoCapacityKg;

	/**
	 * Constructs a new Van with the specified details.
	 *
	 * @param make            the manufacturer of the van
	 * @param model           the model name of the van
	 * @param colour          the colour of the van
	 * @param fuelType        the fuel type used by the van
	 * @param dailyRate       the daily rental rate for the van
	 * @param cargoCapacityKg the cargo capacity in kilograms
	 * @throws IllegalArgumentException if dailyRate is not positive
	 */
	public Van(String make, String model, String colour, FuelType fuelType, double dailyRate, double cargoCapacityKg) {
		Vehicle.validateDailyRate(dailyRate);

		super(make, model, colour, fuelType, dailyRate);
		super.setVehicleType(VehicleType.VAN);
		this.cargoCapacityKg = cargoCapacityKg;
	}

	/**
	 * Gets the cargo capacity of this van.
	 *
	 * @return the cargo capacity in kilograms
	 */
	public double getCargoCapacityKg() {
		return cargoCapacityKg;
	}

	/**
	 * Gets the van-specific specifications.
	 *
	 * @return a formatted string containing the cargo capacity
	 */
	@Override
	public String getSpecs() {
		return String.format("    | Cargo Capacity: %.2f kg", getCargoCapacityKg());
	}

}
