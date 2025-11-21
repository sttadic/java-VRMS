package ie.tus.oop;

public final class Bike extends Vehicle {
	private int wheelSize;

	public Bike(String make, String model, String colour, FuelType fuelType, double dailyRate, int wheelSize) {
		Vehicle.validateDailyRate(dailyRate);
		super(make, model, colour, fuelType, dailyRate);
		super.setVehicleType(VehicleType.BIKE);
		this.wheelSize = wheelSize;
	}

	public int getWheelSize() {
		return wheelSize;
	}

	@Override
	public String getSpecs() {
		return String.format("    | Size: %d\"", getWheelSize());
	}

}
