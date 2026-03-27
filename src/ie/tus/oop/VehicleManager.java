package ie.tus.oop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
		// ADVANCED method reference
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
	// FUNDAMENTALS varargs - variable number of arguments
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

	/**
	 * Returns a sorted copy of the fleet using the given comparator.
	 *
	 * @param comparator the sort order to apply
	 * @return a sorted list of all vehicles
	 */
	// ADVANCED Comparator — passed as a parameter, applied via sorted()
	public List<Vehicle> getSortedVehicles(Comparator<Vehicle> comparator) {
		return vehicles.stream().sorted(comparator).collect(Collectors.toList());
	}

	/**
	 * Applies the given action to every vehicle in the fleet.
	 *
	 * @param action the Consumer to apply to each vehicle
	 */
	// ADVANCED Consumer<Vehicle> lambda — forEach terminal operation
	public void forEachVehicle(Consumer<Vehicle> action) {
		vehicles.stream().forEach(action);
	}

	/**
	 * Maps each vehicle in the fleet to a String using the given mapper function.
	 *
	 * @param mapper the Function that converts a Vehicle to a String
	 * @return a list of mapped strings, one per vehicle
	 */
	// ADVANCED Function<Vehicle, String> lambda — map intermediate operation
	public List<String> getVehicleSummaries(Function<Vehicle, String> mapper) {
		return vehicles.stream().map(mapper).collect(Collectors.toList());
	}

	/**
	 * Computes a formatted fleet statistics report covering a broad range of stream
	 * terminal and intermediate operations.
	 *
	 * @return a formatted multi-line string containing the statistics
	 */
	// ADVANCED Streams — terminal: min, max, findAny, allMatch, anyMatch, noneMatch, forEach, groupingBy, partitioningBy, toMap
	// ADVANCED Streams — intermediate: map, distinct, sorted, limit
	// ADVANCED Supplier<Optional<Vehicle>> — deferred min/max evaluation
	public String getFleetStatistics() {
		StringBuilder sb = new StringBuilder();

		// ADVANCED Supplier<Optional<Vehicle>> — captures live fleet state; evaluated only when .get() is called
		Supplier<Optional<Vehicle>> cheapest = () -> vehicles.stream()
				.min(Comparator.comparing(Vehicle::getDailyRate));
		Supplier<Optional<Vehicle>> mostExpensive = () -> vehicles.stream()
				.max(Comparator.comparing(Vehicle::getDailyRate));

		// min() — cheapest vehicle (invoked via Supplier)
		cheapest.get().ifPresent(v -> sb.append(
				String.format("Cheapest vehicle:       %s %s (€%.2f/day)%n", v.getMake(), v.getModel(), v.getDailyRate())));

		// max() — most expensive vehicle (invoked via Supplier)
		mostExpensive.get().ifPresent(v -> sb.append(
				String.format("Most expensive vehicle: %s %s (€%.2f/day)%n", v.getMake(), v.getModel(), v.getDailyRate())));

		// findAny() — any available electric vehicle
		Optional<Vehicle> anyElectric = vehicles.stream()
				.filter(v -> v.getFuelType() == FuelType.ELECTRIC && v.isAvailable())
				.findAny();
		sb.append(String.format("Any electric available: %s%n",
				anyElectric.map(v -> v.getMake() + " " + v.getModel()).orElse("None")));

		// allMatch() — are all vehicles currently available?
		boolean allAvailable = vehicles.stream().allMatch(Vehicle::isAvailable);
		sb.append(String.format("All vehicles available: %b%n", allAvailable));

		// anyMatch() — are there any electric vehicles in the fleet?
		boolean hasElectric = vehicles.stream().anyMatch(v -> v.getFuelType() == FuelType.ELECTRIC);
		sb.append(String.format("Electric in fleet:      %b%n", hasElectric));

		// noneMatch() — are no vehicles currently rented?
		boolean noneRented = vehicles.stream().noneMatch(v -> !v.isAvailable());
		sb.append(String.format("No vehicles rented:     %b%n%n", noneRented));

		// map() + distinct() — unique fuel types present in the fleet
		List<FuelType> fuelTypes = vehicles.stream()
				.map(Vehicle::getFuelType)
				.distinct()
				.collect(Collectors.toList());
		sb.append("Fuel types in fleet:  ").append(fuelTypes).append("\n");

		// sorted() + limit() — top 3 cheapest vehicles
		List<Vehicle> top3 = vehicles.stream()
				.sorted(Comparator.comparing(Vehicle::getDailyRate))
				.limit(3)
				.collect(Collectors.toList());
		sb.append("Top 3 cheapest:       ");
		top3.forEach(v -> sb.append(String.format("%s %s (€%.2f)  ", v.getMake(), v.getModel(), v.getDailyRate())));
		sb.append("\n\n");

		// collect(groupingBy()) — vehicles grouped by VehicleType
		Map<VehicleType, List<Vehicle>> byType = vehicles.stream()
				.collect(Collectors.groupingBy(Vehicle::getVehicleType));
		sb.append("By vehicle type:\n");
		byType.forEach((type, list) -> sb.append(String.format("  %-4s: %d%n", type, list.size())));

		// collect(partitioningBy()) — split into available and rented
		Map<Boolean, List<Vehicle>> partition = vehicles.stream()
				.collect(Collectors.partitioningBy(Vehicle::isAvailable));
		sb.append(String.format("Available: %d  |  Rented: %d%n%n",
				partition.get(true).size(), partition.get(false).size()));

		// collect(toMap()) — vehicle ID mapped to its daily rate
		Map<Integer, Double> idToRate = vehicles.stream()
				.collect(Collectors.toMap(Vehicle::getVehicleId, Vehicle::getDailyRate));
		sb.append("ID → Daily rate:\n");
		idToRate.forEach((id, rate) -> sb.append(String.format("  %d → €%.2f%n", id, rate)));

		return sb.toString();
	}

}
