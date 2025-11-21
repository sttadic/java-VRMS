package ie.tus.oop;

public abstract sealed class Vehicle implements Rentable permits Car, Van, Bike {
	private static int nextId = 1;
	private final int vehicleId;
	private VehicleType vehicleType;
	private String make;
	private String model;
	private String colour;
	private FuelType fuelType;
	private double dailyRate;
	private boolean isAvailable;

	public Vehicle(String make, String model, String colour, FuelType fuelType, double dailyRate) {
		this.vehicleId = nextId++;
		this.make = make;
		this.model = model;
		this.colour = colour;
		this.fuelType = fuelType;
		this.dailyRate = dailyRate;
		this.isAvailable = true;
	}

	public VehicleType getVehicleType() {
		return vehicleType;
	}

	public abstract String getSpecs();

	protected void setVehicleType(VehicleType type) {
		this.vehicleType = type;
	}

	public int getVehicleId() {
		return vehicleId;
	}

	public String getMake() {
		return make;
	}

	public String getModel() {
		return model;
	}

	public String getColour() {
		return colour;
	}

	public FuelType getFuelType() {
		return fuelType;
	}

	@Override
	public double getDailyRate() {
		return dailyRate;
	}

	public void setDailyRate(double rate) {
		this.dailyRate = rate;
	}

	protected static void validateDailyRate(double dailyRate) {
		if (dailyRate <= 0) {
			throw new IllegalArgumentException("Daily rate must be a positive number");
		}
	}

	@Override
	public boolean isAvailable() {
		return isAvailable;
	}

	@Override
	public void setAvailable(boolean available) {
		this.isAvailable = available;
	}

	@Override
	public String toString() {
		return String.format(ConsoleUtils.VEHICLE_ROW_FORMAT, vehicleType, make, model, colour, fuelType, dailyRate,
				getStatus());
	}

}
