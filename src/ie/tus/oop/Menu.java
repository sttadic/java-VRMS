package ie.tus.oop;

import static java.lang.System.out;

import java.util.Scanner;

public class Menu {
	private boolean keepRunning = true;
	private Scanner scan = new Scanner(System.in);
	private VehicleManager vehicleManager;
	private RentalService rentalService;

	public Menu() {
		vehicleManager = new VehicleManager();
		rentalService = new RentalService(scan, vehicleManager);
	}

	public void runApplication() {
		while (keepRunning) {
			showOptions();
			handleChoice();
		}
	}

	private void handleChoice() {
		int choice = -1;
		while (true) {
			try {
				choice = Integer.parseInt(scan.nextLine());
				out.println();
				break;
			} catch (NumberFormatException e) {
				out.println("Invalid Entry! Please try again.");
			}
		}

		switch (choice) {
		case 1 -> vehicleInventory();
		case 2 -> addNewVehicle();
		case 4 -> removeVehicle();
		case 5 -> processRental();
		case 8 -> keepRunning = false;
		default -> out.println("Invalid Selection! Please try again.");

		}
	}

	private void vehicleInventory() {
		var vehicles = vehicleManager.getAllVehicles();

		if (vehicles.isEmpty()) {
			out.println("No vehicles in inventory");
			return;
		}
		out.println();
		out.printf("%3s | %-5s | %-15s | %-17s | %-8s | %-9s | %-4s | %s%n", "ID", "TYPE", "MODEL", "MAKE", "COLOUR",
				"FUEL", "PRICE/DAY", "AVAILABILITY");
		out.printf(
				"----------------------------------------------------------------------------------------------------%n");
		for (int i = 0; i < vehicles.size(); i++) {
			Vehicle vehicle = vehicles.get(i);
			out.printf("%2d  | %s%n", vehicle.getVehicleId(), vehicle.toString(),
					vehicle.isAvailable() ? "Available" : "Rented");
		}

		out.println("\nPress Enter to return to main menu...");
		scan.nextLine();
	}

	private void addNewVehicle() {
		// Hardcoded example
		vehicleManager.addVehicle(new Car("Seat", "Arona", "Grey", FuelType.PETROL, 28.0, true, false));
	}

	private void removeVehicle() {
		out.println("Enter ID of the vehicle you want to remove from fleet: ");
		int vehicleId = -1;
		while (true) {
			try {
				vehicleId = Integer.parseInt(scan.nextLine());
				break;
			} catch (NumberFormatException e) {
				out.println("Invalid value! Try again.");
			}
		}
		var removed = vehicleManager.removeVehicleById(vehicleId);

		if (removed) {
			out.println("Vehicle succesfully removed!");
		} else {
			out.println("Vehicle with id=" + vehicleId + " does not exist!");
		}
	}

	private void processRental() {
		out.println("Customer name: ");
		var customerName = "";
		while (true) {
			customerName = scan.nextLine().strip();
			if (customerName.isEmpty()) {
				out.println("Custmer name is a required field. Please enter a customer name: ");
				continue;
			}
			break;
		}

		out.println();
		out.printf("%3s | %-5s | %-15s | %-17s | %-8s | %-9s | %-4s | %s%n", "ID", "TYPE", "MODEL", "MAKE", "COLOUR",
				"FUEL", "PRICE/DAY", "AVAILABILITY");
		out.printf(
				"----------------------------------------------------------------------------------------------------%n");
		var vehicles = vehicleManager.getAllVehicles();
		vehicles.forEach(vehicle -> {
			if (vehicle.isAvailable() == true) {
				out.printf("%2d  | %s%n", vehicle.getVehicleId(), vehicle.toString(),
						vehicle.isAvailable() ? "Available" : "Rented");
			}
		});

		rentalService.startRental(customerName);
	}

	private void showOptions() {
		out.println("************************************************************");
		out.println("*                                                          *");
		out.println("*             Vehicle Rental Management System             *");
		out.println("*                                                          *");
		out.println("************************************************************");

		out.println("(1) Display Vehicle Inventory");
		out.println("(2) Add New Vehicle to Fleet");
		out.println("(3) Update Vehicle Information");
		out.println("(4) Remove Vehicle from Fleet");
		out.println("(5) Process Vehicle Rental");
		out.println("(6) Process Vehicle Return");
		out.println("(7) View Active Rental Records");
		out.println("(8) Exit");

		// Option selection block
		out.println("");
		out.println("Select Option (1-8) > ");
	}

}
