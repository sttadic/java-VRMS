package ie.tus.oop;

public class Car extends Vehicle {
	private boolean airConditioning;
	private boolean navigation;

	public Car(String make, String model, String colour, FuelType fuelType, double dailyRate, boolean ac,
			boolean navigation) {
		super(make, model, colour, fuelType, dailyRate);
		super.setVehicleType(VehicleType.CAR);
		this.airConditioning = ac;
		this.navigation = navigation;
	}

	public boolean isAirConditioning() {
		return airConditioning;
	}

	public boolean isNavigation() {
		return navigation;
	}

}
