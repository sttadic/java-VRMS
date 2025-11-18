package ie.tus.oop;

public class Van extends Vehicle {
	private double cargoCapacityKg;

	public Van(String make, String model, String colour, FuelType fuelType, double dailyRate, double capacity) {
		super(make, model, colour, fuelType, dailyRate);
		this.cargoCapacityKg = capacity;
	}

	public double getCargoCapacityKg() {
		return cargoCapacityKg;
	}

}
