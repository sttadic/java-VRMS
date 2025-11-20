package ie.tus.oop;

import static java.lang.System.out;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

public class RentalService {
	private ArrayList<RentalTransaction> transactions;
	private VehicleManager vehicleManager;
	private Scanner scan;

	public RentalService(Scanner scan, VehicleManager vehicleManager) {
		this.scan = scan;
		this.vehicleManager = vehicleManager;
		this.transactions = new ArrayList<>();
	}

	public void startRental(String customerName) {
		int vehicleId = -1;
		Vehicle vehicleToRent;

		while (true) {
			try {
				out.print("\nSelect a vehicle ID to rent: ");
				vehicleId = Integer.parseInt(scan.nextLine());
				vehicleToRent = vehicleManager.getVehicleById(vehicleId);
				if (vehicleToRent == null || !vehicleToRent.isAvailable()) {
					throw new VehicleNotAvailableException("Car not available!");
				}
				break;
			} catch (VehicleNotAvailableException e) {
				out.println(e.getMessage());
			} catch (NumberFormatException e) {
				out.println("Invalid input!");
			}
		}
		transactions.add(new RentalTransaction(customerName, vehicleId, vehicleToRent.getMake(),
				vehicleToRent.getModel(), LocalDate.now()));
		vehicleToRent.setAvailable(false);
		out.println("\nVehicle " + vehicleToRent.getMake() + " " + vehicleToRent.getModel() + " succesfully rented to "
				+ customerName + ".");
	}

	public void endRental() {
		int vehicleId = -1;
		Vehicle vehicleToReturn;
		while (true) {
			try {
				out.print("\nEnter a vehicle ID to register return: ");
				vehicleId = Integer.parseInt(scan.nextLine());
				vehicleToReturn = vehicleManager.getVehicleById(vehicleId);
				if (vehicleToReturn == null || vehicleToReturn.isAvailable()) {
					out.println("Invalid selection!");
					continue;
				}
				break;
			} catch (Exception e) {
				out.println("Invalid entry " + e.getMessage());
			}
		}
		int finalVehicleId = vehicleId;
		var transaction = transactions.stream().filter(t -> t.vehicleID() == finalVehicleId).findFirst().orElse(null);
		transactions.remove(transaction);
		vehicleToReturn.setAvailable(true);
		out.printf("\nVehicle returned!. Total cost is €%.2f%n",
				calculateTotalCost(vehicleToReturn.getDailyRate(), transaction.rentalStartDate(), LocalDate.now()));
	}

	public ArrayList<RentalTransaction> getActiveRentals() {
		return new ArrayList<>(transactions);
	}

	public double calculateTotalCost(double rate, LocalDate startDate, LocalDate endDate) {
		long diff = ChronoUnit.DAYS.between(startDate, endDate);
		return rate * (diff + 1);
	}

}
