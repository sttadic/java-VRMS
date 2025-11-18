package ie.tus.oop;

import static java.lang.System.out;

import java.util.Scanner;

public class Menu {
	private boolean keepRunning = true;
	private Scanner scan = new Scanner(System.in);
	private VehicleManager vehicleManager;

	public Menu() {
		vehicleManager = new VehicleManager();
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
				break;
			} catch (NumberFormatException e) {
				out.println("Invalid Entry! Please try again.");
			}
		}

		switch (choice) {
		case 1 -> vehicleInventory();
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
		out.printf("%s | %-15s | %-15s | %-4s | %-9s | %-4s | %s%n", "ID", "MODEL", "MAKE", "COLOUR", "FUEL",
				"PRICE/DAY", "AVAILABILITY");
		out.printf("----------------------------------------------------------------------------------------%n");
		for (int i = 0; i < vehicles.size(); i++) {
			Vehicle vehicle = vehicles.get(i);
			out.printf("%d  | %s%n", i + 1, vehicle.toString(), vehicle.isAvailable() ? "Available" : "Rented");
		}
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
