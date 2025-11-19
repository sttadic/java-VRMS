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
		case 3 -> updateRentalPrice();
		case 4 -> removeVehicle();
		case 5 -> processRental();
		case 6 -> processReturn();
		case 7 -> viewRentals();
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
		out.println("Select vehicle type to add: ");
		out.println("1) Car");
		out.println("2) Van");
		out.println("3) Bike");

		int typeChoice = -1;
		try {
			typeChoice = Integer.parseInt(scan.nextLine());

		} catch (NumberFormatException e) {
			out.println("Invalid choice.");
			return;
		}

		out.print("Enter Make: ");
		String make = scan.nextLine();
		out.print("Enter Model: ");
		String model = scan.nextLine();
		out.print("Enter Colour: ");
		String colour = scan.nextLine();
		out.print("Enter Daily Rate: ");
		double dailyRate = Double.parseDouble(scan.nextLine());
		out.print("Enter Fuel Type (must be exact match: PETROL, DIESEL, ELECTRIC, HYBRID, NONE): ");
		FuelType fuelType = FuelType.valueOf(scan.nextLine().toUpperCase());

		Vehicle newVehicle = switch (typeChoice) {
		case 1 -> {
			out.print("Has Air Conditioning? (true/false): ");
			boolean ac = Boolean.parseBoolean(scan.nextLine());
			out.print("Has Navigation? (true/false): ");
			boolean nav = Boolean.parseBoolean(scan.nextLine());
			yield new Car(make, model, colour, fuelType, dailyRate, ac, nav);
		}
		case 2 -> {
			out.print("Enter cargo capacity: ");
			double cargoCapacity = Double.parseDouble(scan.nextLine());
			yield new Van(make, model, colour, fuelType, dailyRate, cargoCapacity);
		}
		case 3 -> {
			out.print("Enter engine size: ");
			int engineSize = Integer.parseInt(scan.nextLine());
			yield new Bike(make, model, colour, fuelType, dailyRate, engineSize);
		}
		default -> null;
		};

		if (newVehicle != null) {
			vehicleManager.addVehicle(newVehicle);
			out.println("Vehicle added successfully!");
		} else {
			out.println("Invalid vehicle type selected.");
		}
	}

	private void updateRentalPrice() {
		int vehicleId = -1;
		Vehicle vehicleToUpdate = null;

		while (true) {
			out.print("Enter the ID of the vehicle to update: ");
			try {
				vehicleId = Integer.parseInt(scan.nextLine());
				vehicleToUpdate = vehicleManager.getVehicleById(vehicleId);
				if (vehicleToUpdate == null) {
					out.println("Vehicle with ID " + vehicleId + " not found.");
					continue;
				}
				break;
			} catch (NumberFormatException e) {
				out.println("Invalid ID format.");
			}
		}
		Double newRate;
		out.println("Current vehicle details: " + vehicleId + ". " + vehicleToUpdate.getMake() + " "
				+ vehicleToUpdate.getModel() + " - €" + vehicleToUpdate.getDailyRate());
		while (true) {
			out.print("Enter new daily rate: ");
			try {
				newRate = Double.parseDouble(scan.nextLine());
				if (newRate <= 0) {
					out.println("Daily rate must be a positive number.");
					continue;
				}
				break;
			} catch (NumberFormatException e) {
				out.println("Invalid format.");
			}
		}

		vehicleToUpdate.setDailyRate(newRate);
		out.println("Daily rate updated!");

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

	private void processReturn() {
		var activeRentals = rentalService.getActiveRentals();

		out.printf("%-18s | %-3s | %-20s | %s%n", "CUSTOMER", "VEH_ID", "VEHICLE", "RENT DATE");
		out.printf("----------------------------------------------------------------%n");
		for (RentalTransaction rental : activeRentals) {
			out.printf("%-18.18s | %-6s | %-20.20s | %s%n", rental.customerName(), rental.vehicleID(),
					rental.vehicleMake() + " " + rental.vehicleModel(), rental.rentalStartDate());
		}
		rentalService.endRental();

	}

	private void viewRentals() {
		var activeRentals = rentalService.getActiveRentals();

		out.printf("%-18s | %-3s | %-20s | %s%n", "CUSTOMER", "VEH_ID", "VEHICLE", "RENT DATE");
		out.printf("----------------------------------------------------------------%n");
		for (RentalTransaction rental : activeRentals) {
			out.printf("%-18.18s | %-6s | %-20.20s | %s%n", rental.customerName(), rental.vehicleID(),
					rental.vehicleMake() + " " + rental.vehicleModel(), rental.rentalStartDate());
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
		out.println("(3) Update Rental Price");
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
