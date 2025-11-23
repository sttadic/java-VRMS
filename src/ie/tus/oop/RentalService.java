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
		int vehicleId = inputHandler.readInt("\nEnter vehicle ID to rent > ");

		try {
			Vehicle vehicleToRent = vehicleManager.getVehicleById(vehicleId);
			if (!vehicleToRent.isAvailable()) {
				throw new VehicleNotAvailableException("Selected vehicle is currently rented.");
			}

			transactions.add(new RentalTransaction(customerName, vehicleId, vehicleToRent.getMake(),
					vehicleToRent.getModel(), LocalDate.now()));
			vehicleToRent.setAvailable(false);
			out.println("\nVehicle " + vehicleToRent.getMake() + " " + vehicleToRent.getModel()
					+ " rented successfully to " + customerName + ".");
		} catch (VehicleNotAvailableException e) {
			out.println(e.getMessage());
		}
	}

	/**
	 * Ends a rental transaction and generates a receipt. Applies discounts: 15% for
	 * electric cars, 10% for other electric vehicles.
	 *
	 * @return a RentalReceipt containing transaction details, or null if the
	 *         transaction failed
	 */
	public RentalReceipt endRental() {
		int vehicleId = inputHandler.readInt("\nEnter vehicle ID to return > ");

		try {
			Vehicle vehicleToReturn = vehicleManager.getVehicleById(vehicleId);
			if (vehicleToReturn.isAvailable()) {
				throw new VehicleNotAvailableException("Selected vehicle is not currently rented.");
			}

			final int finalVehicleId = vehicleId;
			var transaction = transactions.stream().filter(t -> t.vehicleID() == finalVehicleId).findFirst()
					.orElse(null);

			if (transaction == null) {
				out.println("Could not find matching rental transaction.");
				return null;
			}

			transactions.remove(transaction);
			vehicleToReturn.setAvailable(true);
			LocalDate endDate = LocalDate.now();

			// Pattern matching for instanceof
			double effectiveRate = vehicleToReturn.getDailyRate();
			if (vehicleToReturn instanceof Car c && c.getFuelType() == FuelType.ELECTRIC) {
				out.println("Applying 15% electric car discount!");
				effectiveRate *= .85;
			} else if (vehicleToReturn.getFuelType() == FuelType.ELECTRIC) {
				out.println("Applying 10% electric vehicle discount!");
				effectiveRate *= .90;
			}

			double totalCost = Rentable.calculateRentalCost(effectiveRate, transaction.rentalStartDate(), endDate);
			String vehicleDescription = vehicleToReturn.getMake() + " " + vehicleToReturn.getModel();

			return new RentalReceipt(transaction.customerName(), finalVehicleId, vehicleDescription,
					transaction.rentalStartDate(), endDate, totalCost);
		} catch (VehicleNotAvailableException e) {
			out.println(e.getMessage());
			return null;
		}
	}

	/**
	 * Gets a copy of all active rental transactions.
	 *
	 * @return a list of active rental transactions
	 */
	public ArrayList<RentalTransaction> getActiveRentals() {
		return new ArrayList<>(transactions);
	}

}
