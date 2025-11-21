package ie.tus.oop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VehicleManager {
	private List<Vehicle> vehicles;

	public VehicleManager() {
		this.vehicles = new ArrayList<>();
		prepopulateFleet();
	}

	private void prepopulateFleet() {
		addVehicles(new Car("Toyota", "Auris", "Red", FuelType.PETROL, 26.0),
				new Car("VW", "ID.4", "Blue", FuelType.ELECTRIC, 35.0, true, true),
				new Van("Fiat", "Ducato", "White", FuelType.DIESEL, 48.0, 2000),
				new Bike("Mountain Bike", "Carrera Hellcat", "Black", FuelType.NONE, 8.50, 29),
				new Bike("Electric Bike", "Boardman ADV", "Red", FuelType.ELECTRIC, 11.0, 27));
	}

	public List<Vehicle> getAllVehicles() {
		return new ArrayList<>(vehicles);
	}

	public long getAvailableVehicleCount() {
		// Stream with method reference
		return vehicles.stream().filter(Vehicle::isAvailable).count();
	}

	public int getFleetSize() {
		return vehicles.size();
	}

	public Vehicle getVehicleById(int vehicleId) {
		for (Vehicle vehicle : vehicles) {
			if (vehicle.getVehicleId() == vehicleId) {
				return vehicle;
			}
		}
		return null;
	}

	public void addVehicles(Vehicle... newVehicles) {
		vehicles.addAll(Arrays.asList(newVehicles));
	}

	public boolean removeVehicleById(int vehicleId) {
		return vehicles.removeIf(vehicle -> vehicle.getVehicleId() == vehicleId);
	}

}
