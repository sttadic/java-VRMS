package ie.tus.oop;

import static java.lang.System.out;

import java.util.Scanner;

public class RentalService {
	private RentalTransaction transaction;
	private VehicleManager vehicleManager;
	private Scanner scan;

	public RentalService(Scanner scan, VehicleManager vehicleManager) {
		this.scan = scan;
		this.vehicleManager = vehicleManager;
	}

	public void startRental(String customerName) {
		int vehicleId = -1;
		Vehicle vehicle;

		while (true) {
			try {
				out.println("Select a vehicle ID to rent: ");
				vehicleId = Integer.parseInt(scan.nextLine());
				vehicle = vehicleManager.getVehicleById(vehicleId);
				if (vehicle == null) {
					throw new VehicleNotAvailableException("Car not available!");
				}
				break;
			} catch (VehicleNotAvailableException e) {
				out.println(e.getMessage());
			} catch (NumberFormatException e) {
				out.println("Invalid input!");
			}
		}

		vehicle.setAvailable(false);
	}

	public void endRental() {
	}

	public double calculateTotalCost() {
		return 0;
	}

}
