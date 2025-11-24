package ie.tus.oop;

/**
 * Abstract base class representing a vehicle in the rental system. This sealed
 * class permits only Car, Van, and Bike as subclasses. Implements the Rentable
 * interface to provide rental functionality.
 *
 * @author Stjepan Tadic
 */
//ADVANCED sealed class
public abstract sealed class Vehicle implements Rentable permits Car, Van, Bike {
	// FUNDAMENTALS encapsulation - private fields + access via getters/setters
	private static int nextId = 1;
	private final int vehicleId;
	private VehicleType vehicleType;
	private String make;
	private String model;
	private String colour;
	private FuelType fuelType;
	private double dailyRate;
	private boolean isAvailable;

	/**
	 * Constructs a new Vehicle with the specified details.
	 *
	 * @param make      the manufacturer of the vehicle
	 * @param model     the model name of the vehicle
	 * @param colour    the colour of the vehicle
	 * @param fuelType  the fuel type used by the vehicle
	 * @param dailyRate the daily rental rate for the vehicle
	 */
	public Vehicle(String make, String model, String colour, FuelType fuelType, double dailyRate) {
		// FUNDAMENTALS this. - reference to the current object instance
		this.vehicleId = nextId++;
		this.make = make;
		this.model = model;
		this.colour = colour;
		this.fuelType = fuelType;
		this.dailyRate = dailyRate;
		this.isAvailable = true;
	}

	/**
	 * Gets the type of this vehicle.
	 *
	 * @return the vehicle type
	 */
	public VehicleType getVehicleType() {
		return vehicleType;
	}

	/**
	 * Gets the specific specifications of this vehicle. This method must be
	 * implemented by subclasses to provide vehicle-specific details.
	 *
	 * @return a formatted string containing vehicle specifications
	 */
	public abstract String getSpecs();

	/**
	 * Sets the type of this vehicle. This method is protected and can only be
	 * called by subclasses.
	 *
	 * @param type the vehicle type to set
	 */
	protected void setVehicleType(VehicleType type) {
		this.vehicleType = type;
	}

	/**
	 * Gets the unique identifier for this vehicle.
	 *
	 * @return the vehicle ID
	 */
	public int getVehicleId() {
		return vehicleId;
	}

	/**
	 * Gets the manufacturer of this vehicle.
	 *
	 * @return the vehicle make
	 */
	public String getMake() {
		return make;
	}

	/**
	 * Gets the model name of this vehicle.
	 *
	 * @return the vehicle model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * Gets the colour of this vehicle.
	 *
	 * @return the vehicle colour
	 */
	public String getColour() {
		return colour;
	}

	/**
	 * Gets the fuel type of this vehicle.
	 *
	 * @return the fuel type
	 */
	public FuelType getFuelType() {
		return fuelType;
	}

	/**
	 * Gets the daily rental rate for this vehicle.
	 *
	 * @return the daily rate
	 */
	@Override
	public double getDailyRate() {
		return dailyRate;
	}

	/**
	 * Sets the daily rental rate for this vehicle.
	 *
	 * @param rate the new daily rate
	 */
	public void setDailyRate(double rate) {
		this.dailyRate = rate;
	}

	/**
	 * Validates that the daily rate is positive.
	 *
	 * @param dailyRate the daily rate to validate
	 * @throws IllegalArgumentException if the daily rate is not positive
	 */
	protected static void validateDailyRate(double dailyRate) {
		if (dailyRate <= 0) {
			throw new IllegalArgumentException(
					ConsoleUtils.RED + "Daily rate must be a positive number." + ConsoleUtils.RESET);
		}
	}

	/**
	 * Checks if this vehicle is available for rental.
	 *
	 * @return true if the vehicle is available, false otherwise
	 */
	@Override
	public boolean isAvailable() {
		return isAvailable;
	}

	/**
	 * Sets the availability status of this vehicle.
	 *
	 * @param available true to make the vehicle available, false otherwise
	 */
	@Override
	public void setAvailable(boolean available) {
		this.isAvailable = available;
	}

	/**
	 * Returns a formatted string representation of this vehicle.
	 *
	 * @return a formatted string containing vehicle details
	 */
	@Override
	public String toString() {
		// FUNDAMENTALS String class
		return String.format(ConsoleUtils.VEHICLE_ROW_FORMAT, vehicleType, make, model, colour, fuelType, dailyRate,
				getStatus());
	}

}
