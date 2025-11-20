package ie.tus.oop;

public final class Bike extends Vehicle {
	private int size;

	public Bike(String make, String model, String colour, FuelType fuelType, double dailyRate, int size) {
		Vehicle.validateDailyRate(dailyRate);
		super(make, model, colour, fuelType, dailyRate);
		super.setVehicleType(VehicleType.BIKE);
		this.size = size;
	}

	public int getSize() {
		return size;
	}

}
