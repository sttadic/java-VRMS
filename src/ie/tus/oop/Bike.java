package ie.tus.oop;

public class Bike extends Vehicle {
	private int size;

	public Bike(String make, String model, String colour, FuelType fuelType, double dailyRate, int size) {
		super(make, model, colour, fuelType, dailyRate);
		this.size = size;
	}

	public int getSize() {
		return size;
	}

}
