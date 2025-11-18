package ie.tus.oop;

public abstract class Vehicle {
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

	protected void setVehicleType(VehicleType type) {
		this.vehicleType = type;
	}

	public VehicleType getVehicleType() {
		return vehicleType;
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

	public double getDailyRate() {
		return dailyRate;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public double calculateRentalCost(int days) {
		return this.dailyRate * days;
	}

	public void setAvailable(boolean available) {
		this.isAvailable = available;
	}

	@Override
	public String toString() {
		return String.format("%-5s | %-15s | %-17s | %-8s | %-9s | €%-8.2f | %s", vehicleType, make, model, colour,
				fuelType, dailyRate, isAvailable ? "Available" : "Rented");
	}

}
