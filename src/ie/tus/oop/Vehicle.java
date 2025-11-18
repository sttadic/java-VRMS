package ie.tus.oop;

public abstract class Vehicle {
	private String fuelType;
	private double dailyRate;
	private boolean isAvailable;

	public double calculateRentalCost(int days) {
		return this.dailyRate * days;
	}
}
