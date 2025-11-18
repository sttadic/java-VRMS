package ie.tus.oop;

public abstract class Vehicle {
	private String make;
	private String model;
	private String colour;
	private FuelType fuelType;
	private double dailyRate;
	private boolean isAvailable;

	public Vehicle(String make, String model, String colour, FuelType fuelType, double dailyRate) {
		this.make = make;
		this.model = model;
		this.colour = colour;
		this.fuelType = fuelType;
		this.dailyRate = dailyRate;
		this.isAvailable = true;
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
}
