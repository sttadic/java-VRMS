package ie.tus.oop;

public final class Car extends Vehicle {
	private boolean airConditioning;
	private boolean navigation;

	public Car(String make, String model, String colour, FuelType fuelType, double dailyRate, boolean hasAirConditioning,
			boolean hasNavigation) {
		// Flexible constructor body demonstration
		Vehicle.validateDailyRate(dailyRate);

		super(make, model, colour, fuelType, dailyRate);
		super.setVehicleType(VehicleType.CAR);
		this.airConditioning = hasAirConditioning;
		this.navigation = hasNavigation;
	}

	public Car(String make, String model, String colour, FuelType fuelType, double dailyRate) {
		this(make, model, colour, fuelType, dailyRate, false, false);
	}

	public boolean isAirConditioning() {
		return airConditioning;
	}

	public boolean isNavigation() {
		return navigation;
	}

	@Override
	public String getSpecs() {
		return String.format("    | AC: %s, Navigation: %s", isAirConditioning(), isNavigation());
	}

}
