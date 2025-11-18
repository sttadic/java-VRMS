package ie.tus.oop;

import java.util.ArrayList;
import java.util.List;

public class VehicleManager {
	private List<Vehicle> vehicles;

	public VehicleManager() {
		this.vehicles = new ArrayList<>();
		prepopulateFleet();
	}

	private void prepopulateFleet() {
		vehicles.add(new Car("Toyota", "Auris", "Red", FuelType.PETROL, 26.0, true, true));
		vehicles.add(new Car("VW", "ID.4", "Blue", FuelType.ELECTRIC, 35.0, true, true));
		vehicles.add(new Van("Fiat", "Ducato", "White", FuelType.DIESEL, 48.0, 2000));
		vehicles.add(new Bike("Mountain Bike", "Carrera Hellcat", "Black", FuelType.NONE, 8.50, 29));
		vehicles.add(new Bike("Electric Bike", "Boardman ADV", "Red", FuelType.ELECTRIC, 11.0, 27));
	}

	public List<Vehicle> getAllVehicles() {
		return new ArrayList<>(vehicles);
	}

	public void addVehicle(Vehicle vehicle) {
		vehicles.add(vehicle);
	}

	public boolean removeVehicle(Vehicle vehicle) {
		return vehicles.remove(vehicle);
	}

	public int getFleetSize() {
		return vehicles.size();
	}

}
