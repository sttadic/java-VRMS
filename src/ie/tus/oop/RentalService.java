package ie.tus.oop;

import static java.lang.System.out;

import java.time.LocalDate;
import java.util.ArrayList;

public class RentalService {
	private ArrayList<RentalTransaction> transactions;
	private VehicleManager vehicleManager;
	private final InputHandler inputHandler;

	public RentalService(VehicleManager vehicleManager, InputHandler inputHandler) {
		this.vehicleManager = vehicleManager;
		this.inputHandler = inputHandler;
		this.transactions = new ArrayList<>();
	}

	public void startRental(String customerName) {
		int vehicleId = inputHandler.readInt("\nEnter vehicle ID to rent > ");
		Vehicle vehicleToRent = null;

		try {
			vehicleToRent = vehicleManager.getVehicleById(vehicleId);
			if (vehicleToRent == null || !vehicleToRent.isAvailable()) {
				throw new VehicleNotAvailableException("Vehicle unavailable.");
			}
		} catch (VehicleNotAvailableException e) {
			out.println(e.getMessage());
			return;
		}

		transactions.add(new RentalTransaction(customerName, vehicleId, vehicleToRent.getMake(),
				vehicleToRent.getModel(), LocalDate.now()));
		vehicleToRent.setAvailable(false);
		out.println("\nVehicle " + vehicleToRent.getMake() + " " + vehicleToRent.getModel() + " rented successfully to "
				+ customerName + ".");
	}

	public RentalReceipt endRental() {
		int vehicleId = inputHandler.readInt("\nEnter vehicle ID to return > ");
		Vehicle vehicleToReturn = null;

		try {
			vehicleToReturn = vehicleManager.getVehicleById(vehicleId);
			if (vehicleToReturn == null || vehicleToReturn.isAvailable()) {
				throw new VehicleNotAvailableException("Vehicle unavailable.");
			}
		} catch (VehicleNotAvailableException e) {
			out.println(e.getMessage());
			return null;
		}

		int finalVehicleId = vehicleId;
		var transaction = transactions.stream().filter(t -> t.vehicleID() == finalVehicleId).findFirst().orElse(null);

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
	}

	public ArrayList<RentalTransaction> getActiveRentals() {
		return new ArrayList<>(transactions);
	}

}
