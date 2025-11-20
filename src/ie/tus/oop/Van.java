package ie.tus.oop;

public final class Van extends Vehicle {
	private double cargoCapacityKg;

	public Van(String make, String model, String colour, FuelType fuelType, double dailyRate, double capacity) {
		Vehicle.validateDailyRate(dailyRate);

		super(make, model, colour, fuelType, dailyRate);
		super.setVehicleType(VehicleType.VAN);
		this.cargoCapacityKg = capacity;
	}

	public double getCargoCapacityKg() {
		return cargoCapacityKg;
	}

}
