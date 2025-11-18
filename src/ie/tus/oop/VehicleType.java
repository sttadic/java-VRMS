package ie.tus.oop;

public enum VehicleType {
	CAR, VAN, BIKE;

	@Override
	public String toString() {
		return this.name().toLowerCase();
	}
}
