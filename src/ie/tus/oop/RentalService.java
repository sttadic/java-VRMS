package ie.tus.oop;

import static java.lang.System.out;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Manages vehicle rental transactions including starting and ending rentals.
 * Applies discounts for electric vehicles and handles rental receipts.
 *
 * @author Stjepan Tadic
 */
public class RentalService {
	// FUNDAMENTALS List/ArrayList
	private ArrayList<RentalTransaction> transactions;
	private VehicleManager vehicleManager;
	private final InputHandler inputHandler;

	/**
	 * Constructs a new RentalService with the specified vehicle manager and input
	 * handler.
	 *
	 * @param vehicleManager the vehicle manager to use
	 * @param inputHandler   the input handler for user interactions
	 */
	public RentalService(VehicleManager vehicleManager, InputHandler inputHandler) {
		this.vehicleManager = vehicleManager;
		this.inputHandler = inputHandler;
		this.transactions = new ArrayList<>();
	}

	/**
	 * Starts a new rental transaction for the specified customer.
	 *
	 * @param customerName the name of the customer renting the vehicle
	 */
	public void startRental(String customerName) {
		Vehicle vehicleToRent;

		while (true) {
			vehicleToRent = inputHandler.selectVehicle("\nEnter vehicle ID to rent > ", vehicleManager);
			if (!vehicleToRent.isAvailable()) {
				out.println(ConsoleUtils.RED + "Selected vehicle is currently rented." + ConsoleUtils.RESET);
				continue;
			}
			break;
		}
		vehicleToRent.setAvailable(false);
		transactions.add(new RentalTransaction(customerName, vehicleToRent.getVehicleId(), vehicleToRent.getMake(),
				vehicleToRent.getModel(), LocalDate.now()));

		out.println("\nVehicle " + vehicleToRent.getMake() + " " + vehicleToRent.getModel() + " rented successfully to "
				+ customerName + ".");

	}

	/**
	 * Ends a rental transaction and generates a receipt. Applies discounts: 15% for
	 * electric cars, 10% for other electric vehicles.
	 *
	 * @return a RentalReceipt containing transaction details, or null if the
	 *         transaction failed
	 */
	public RentalReceipt endRental() {
		Vehicle vehicleToReturn;

		while (true) {
			vehicleToReturn = inputHandler.selectVehicle("\nEnter vehicle ID to return > ", vehicleManager);
			if (vehicleToReturn.isAvailable()) {
				out.println(ConsoleUtils.RED + "Selected vehicle is not currently rented." + ConsoleUtils.RESET);
				continue;
			}
			break;
		}

		// ADVANCED lambdas/predicate/final
		final var finalVehicleToReturn = vehicleToReturn;
		var transaction = transactions.stream().filter(t -> t.vehicleID() == finalVehicleToReturn.getVehicleId())
				.findFirst().orElse(null);

		if (transaction == null) {
			out.println(ConsoleUtils.RED + "Could not find matching rental transaction." + ConsoleUtils.RESET);
			return null;
		}

		transactions.remove(transaction);
		vehicleToReturn.setAvailable(true);
		// FUNDAMENTALS LocalDate
		LocalDate endDate = LocalDate.now();

		double effectiveRate = vehicleToReturn.getDailyRate();
		// ADVANCED pattern matching for instanceof
		if (vehicleToReturn instanceof Car c && c.getFuelType() == FuelType.ELECTRIC) {
			out.println("Applying 15% electric car discount!");
			effectiveRate *= .85;
		} else if (vehicleToReturn.getFuelType() == FuelType.ELECTRIC) {
			out.println("Applying 10% electric vehicle discount!");
			effectiveRate *= .90;
		}

		double totalCost = Rentable.calculateRentalCost(effectiveRate, transaction.rentalStartDate(), endDate);
		String vehicleDescription = vehicleToReturn.getMake() + " " + vehicleToReturn.getModel();

		return new RentalReceipt(transaction.customerName(), finalVehicleToReturn.getVehicleId(), vehicleDescription,
				transaction.rentalStartDate(), endDate, totalCost);
	}

	/**
	 * Gets a copy of all active rental transactions.
	 *
	 * @return a list of active rental transactions
	 */
	// ADVANCED call-by-vale and defensive copying
	public ArrayList<RentalTransaction> getActiveRentals() {
		return new ArrayList<>(transactions);
	}

}
