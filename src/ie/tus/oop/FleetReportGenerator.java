package ie.tus.oop;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Generates a fleet report by computing four sections concurrently using an
 * ExecutorService and Callable tasks.
 *
 * @author Stjepan Tadic
 */
// ADVANCED Concurrency - ExecutorService, Callable, Future
public class FleetReportGenerator {
	private final VehicleManager vehicleManager;
	private final RentalService rentalService;

	/**
	 * Constructs a FleetReportGenerator with the given manager and service.
	 *
	 * @param vehicleManager the vehicle manager providing fleet data
	 * @param rentalService  the rental service providing completed receipt data
	 */
	public FleetReportGenerator(VehicleManager vehicleManager, RentalService rentalService) {
		this.vehicleManager = vehicleManager;
		this.rentalService = rentalService;
	}

	/**
	 * Generates a full fleet report by executing four Callable report sections
	 * concurrently on a fixed thread pool, then assembling the results.
	 *
	 * @return a formatted multi-section report string
	 * @throws Exception if any report section fails or the thread is interrupted
	 */
	public String generateReport() throws Exception {
		List<Vehicle> vehicles = vehicleManager.getAllVehicles();
		List<RentalReceipt> receipts = rentalService.getCompletedRentals();

		// ADVANCED Callable<String> — each task returns a report section as a String
		Callable<String> inventorySummary = () -> {
			Map<VehicleType, Long> byType = vehicles.stream()
					.collect(Collectors.groupingBy(Vehicle::getVehicleType, Collectors.counting()));
			StringBuilder sb = new StringBuilder("--- Fleet Inventory Summary ---\n");
			sb.append(String.format("Total vehicles:    %d%n", vehicles.size()));
			sb.append(String.format("Available:         %d%n", vehicles.stream().filter(Vehicle::isAvailable).count()));
			sb.append(String.format("Currently rented:  %d%n", vehicles.stream().filter(v -> !v.isAvailable()).count()));
			byType.forEach((type, count) -> sb.append(String.format("  %-4s: %d%n", type, count)));
			return sb.toString();
		};

		Callable<String> revenueTotal = () -> {
			double total = receipts.stream().mapToDouble(RentalReceipt::getTotalCost).sum();
			return String.format("--- Revenue Total ---%nCompleted rentals: %d%nTotal revenue:     €%.2f%n",
					receipts.size(), total);
		};

		Callable<String> availabilityAnalysis = () -> {
			int total = vehicles.size();
			long available = vehicles.stream().filter(Vehicle::isAvailable).count();
			double availPct = total == 0 ? 0 : (available * 100.0) / total;
			double rentedPct = 100.0 - availPct;
			return String.format("--- Availability Analysis ---%nAvailable: %.1f%%%nRented:    %.1f%%%n",
					availPct, rentedPct);
		};

		Callable<String> mostRentedVehicle = () -> {
			if (receipts.isEmpty()) {
				return "--- Most Rented Vehicle ---\nNo completed rentals yet.\n";
			}
			String topVehicle = receipts.stream()
					.collect(Collectors.groupingBy(RentalReceipt::getVehicleDescription, Collectors.counting()))
					.entrySet().stream()
					.max(Comparator.comparingLong(Map.Entry::getValue))
					.map(e -> e.getKey() + " (" + e.getValue() + " rental(s))")
					.orElse("N/A");
			return "--- Most Rented Vehicle ---\n" + topVehicle + "\n";
		};

		// ADVANCED ExecutorService — fixed thread pool runs all four tasks concurrently
		ExecutorService executor = Executors.newFixedThreadPool(4);
		List<Future<String>> futures = executor.invokeAll(
				List.of(inventorySummary, revenueTotal, availabilityAnalysis, mostRentedVehicle));
		executor.shutdown();

		// ADVANCED Future.get() — blocks until each task result is ready, then assembles
		StringBuilder report = new StringBuilder("========== FLEET REPORT ==========\n\n");
		for (Future<String> future : futures) {
			report.append(future.get()).append("\n");
		}
		return report.toString();
	}
}
