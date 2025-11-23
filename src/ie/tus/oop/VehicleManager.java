package ie.tus.oop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Manages the fleet of vehicles in the rental system. Provides methods for
 * adding, removing, and querying vehicles.
 *
 * @author Stjepan Tadic
 */
public class VehicleManager {
	private List<Vehicle> vehicles;

	/**
	 * Constructs a new VehicleManager and pre-populates the fleet with sample
	 * vehicles.
	 */
	public VehicleManager() {
		this.vehicles = new ArrayList<>();
		prepopulateFleet();
	}

	/**
	 * Pre-populates the fleet with sample vehicles for demonstration purposes.
	 */
	private void prepopulateFleet() {
		addVehicles(new Car("Toyota", "Auris", "Red", FuelType.PETROL, 26.0),
				new Car("VW", "ID.4", "Blue", FuelType.ELECTRIC, 35.0, true, true),
				new Van("Fiat", "Ducato", "White", FuelType.DIESEL, 48.0, 2000),
				new Bike("Mountain Bike", "Carrera Hellcat", "Black", FuelType.NONE, 8.50, 29),
				new Bike("Electric Bike", "Boardman ADV", "Red", FuelType.ELECTRIC, 11.0, 27));
	}

	/**
	 * Gets a copy of all vehicles in the fleet.
	 *
	 * @return a list of all vehicles
	 */
	public List<Vehicle> getAllVehicles() {
		return new ArrayList<>(vehicles);
	}

	/**
	 * Counts the number of vehicles currently available for rental.
	 *
	 * @return the count of available vehicles
	 */
	public long getAvailableVehicleCount() {
		// Stream with method reference
		return vehicles.stream().filter(Vehicle::isAvailable).count();
	}

	/**
	 * Gets the total number of vehicles in the fleet.
	 *
	 * @return the fleet size
	 */
	public int getFleetSize() {
		return vehicles.size();
	}

	/**
	 * Retrieves a vehicle by its ID.
	 *
	 * @param vehicleId the ID of the vehicle to retrieve
	 * @return the vehicle with the specified ID
	 * @throws VehicleNotAvailableException if the vehicle is not found
	 */
	public Vehicle getVehicleById(int vehicleId) throws VehicleNotAvailableException {
		for (Vehicle vehicle : vehicles) {
			if (vehicle.getVehicleId() == vehicleId) {
				return vehicle;
			}
		}
		throw new VehicleNotAvailableException("Vehicle with ID " + vehicleId + " not found.");
	}

	/**
	 * Adds one or more vehicles to the fleet.
	 *
	 * @param newVehicles the vehicles to add
	 */
	public void addVehicles(Vehicle... newVehicles) {
		vehicles.addAll(Arrays.asList(newVehicles));
	}

	/**
	 * Removes a vehicle from the fleet by its ID.
	 *
	 * @param vehicleId the ID of the vehicle to remove
	 * @return true if the vehicle was removed, false if not found
	 */
	public boolean removeVehicleById(int vehicleId) {
		return vehicles.removeIf(vehicle -> vehicle.getVehicleId() == vehicleId);
	}

}
