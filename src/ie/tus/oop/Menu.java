package ie.tus.oop;

import static java.lang.System.out;

import java.util.Scanner;

public class Menu {
	private boolean keepRunning = true;
	private final Scanner scan = new Scanner(System.in);
	private final VehicleManager vehicleManager;
	private final InputHandler inputHandler;
	private final RentalService rentalService;

	public Menu() {
		vehicleManager = new VehicleManager();
		inputHandler = new InputHandler(scan);
		rentalService = new RentalService(vehicleManager, inputHandler);
	}

	public void runApplication() {
		while (keepRunning) {
			showOptions();
			handleChoice();
		}
	}

	private void handleChoice() {
		int choice = inputHandler.readInt("\nSelect Option (1-8) > ");

		switch (choice) {
		case 1 -> vehicleInventory();
		case 2 -> addNewVehicle();
		case 3 -> updateRentalPrice();
		case 4 -> removeVehicle();
		case 5 -> processRental();
		case 6 -> processReturn();
		case 7 -> viewRentals();
		case 8 -> {
			scan.close();
			keepRunning = false;
		}
		default -> out.println("\nInvalid Selection!");

		}
	}

	private void vehicleInventory() {
		RentalUtils.clearScreen();
		var vehicles = vehicleManager.getAllVehicles();

		if (vehicles.isEmpty()) {
			out.println("No vehicles in inventory");
			return;
		}
		out.println();
		out.printf("%3s | %-5s | %-15s | %-17s | %-8s | %-9s | %-4s | %-10s | %s%n", "ID", "TYPE", "MODEL", "MAKE",
				"COLOUR", "FUEL", "PRICE/DAY", "AVAILABILITY", "DETAILS");
		out.printf(
				"----------------------------------------------------------------------------------------------------------------------------------%n");

		for (Vehicle vehicle : vehicles) {
			out.printf("%2d  | %s", vehicle.getVehicleId(), vehicle.toString());
			// Pattern matching
			String specificDetails = switch (vehicle) {
			case Car c -> String.format("    | AC: %s, Navigation: %s", c.isAirConditioning(), c.isNavigation());
			case Van v -> String.format("    | Cargo Capacity: %.2f kg", v.getCargoCapacityKg());
			case Bike b -> String.format("    | Size: %d\"", b.getSize());
			default -> "";
			};
			if (!specificDetails.isEmpty()) {
				out.println(specificDetails);
			}
		}

		out.println("\nTotal number of vehicles: " + vehicleManager.getFleetSize());

		RentalUtils.waitForEnter(scan);
	}

	private void addNewVehicle() {
		RentalUtils.clearScreen();

		out.println("(1) Car");
		out.println("(2) Van");
		out.println("(3) Bike");
		int typeChoice = inputHandler.readInt("\nSelect vehicle type to add > ");

		String make = inputHandler.readString("Enter Make: ");
		String model = inputHandler.readString("Enter Model: ");
		String colour = inputHandler.readString("Enter Colour: ");
		double dailyRate = inputHandler.readDouble("Enter Daily Rate: ");
		out.println("\n(1) Petrol");
		out.println("(2) Diesel");
		out.println("(3) Electric");
		out.println("(4) Hybrid");
		out.println("(5) None");
		FuelType fuelType;
		while (true) {
			try {
				int fuelChoice = inputHandler.readInt("Select Fuel Type > ");
				fuelType = FuelType.values()[fuelChoice - 1];
				break;
			} catch (ArrayIndexOutOfBoundsException e) {
				out.println("Invalid value selected. Please enter one of the expected values.");
			}
		}

		Vehicle newVehicle = switch (typeChoice) {
		case 1 -> {
			boolean ac = inputHandler.readBoolean("Has Air Conditioning? (t)rue/(f)alse: ");
			boolean nav = inputHandler.readBoolean("Has Navigation? (t)rue/(f)alse: ");
			yield new Car(make, model, colour, fuelType, dailyRate, ac, nav);
		}
		case 2 -> {
			double cargoCapacity = inputHandler.readDouble("Enter Cargo Capacity: ");
			yield new Van(make, model, colour, fuelType, dailyRate, cargoCapacity);
		}
		case 3 -> {
			int engineSize = inputHandler.readInt("Enter Wheel Size: ");
			yield new Bike(make, model, colour, fuelType, dailyRate, engineSize);
		}
		default -> null;
		};

		if (newVehicle != null) {
			vehicleManager.addVehicles(newVehicle);
			out.println("\nVehicle added successfully.");
		} else {
			out.println("\nSomething went wrong. Please try again.");
			return;
		}

		RentalUtils.waitForEnter(scan);
	}

	private void updateRentalPrice() {
		RentalUtils.clearScreen();

		int vehicleId = inputHandler.readInt("Enter the ID of the vehicle to update: ");
		Vehicle vehicleToUpdate = vehicleManager.getVehicleById(vehicleId);

		if (vehicleToUpdate == null) {
			out.println("Vehicle with ID " + vehicleId + " not found.");
			RentalUtils.waitForEnter(scan);
			return;
		}

		out.println("Current vehicle details: " + vehicleId + ". " + vehicleToUpdate.getMake() + " "
				+ vehicleToUpdate.getModel() + " - €" + vehicleToUpdate.getDailyRate());

		double newRate = inputHandler.readDouble("Enter new Daily Rate");

		vehicleToUpdate.setDailyRate(newRate);
		out.println("\nPrice updated.");
		RentalUtils.waitForEnter(scan);
	}

	private void removeVehicle() {
		RentalUtils.clearScreen();

		int vehicleId = inputHandler.readInt("Enter ID of the vehicle you want to remove from fleet: ");
		var removed = vehicleManager.removeVehicleById(vehicleId);

		if (removed) {
			out.println("\nVehicle succesfully removed.");
		} else {
			out.println("\nVehicle with id=" + vehicleId + " does not exist!");
		}

		RentalUtils.waitForEnter(scan);
	}

	private void processRental() {
		RentalUtils.clearScreen();

		out.print("Customer name: ");
		String customerName = inputHandler.readString("Customer name: ");

		out.println();
		out.printf("%3s | %-5s | %-15s | %-17s | %-8s | %-9s | %-4s | %s%n", "ID", "TYPE", "MODEL", "MAKE", "COLOUR",
				"FUEL", "PRICE/DAY", "AVAILABILITY");
		out.printf(
				"----------------------------------------------------------------------------------------------------%n");

		var vehicles = vehicleManager.getAllVehicles();
		vehicles.stream().filter(Vehicle::isAvailable)
				.forEach(vehicle -> out.printf("%2d  | %s%n", vehicle.getVehicleId(), vehicle.toString()));

		rentalService.startRental(customerName);
		RentalUtils.waitForEnter(scan);
	}

	private void processReturn() {
		RentalUtils.clearScreen();

		var activeRentals = rentalService.getActiveRentals();

		out.printf("%-18s | %-3s | %-20s | %s%n", "CUSTOMER", "VEH_ID", "VEHICLE", "RENT DATE");
		out.printf("----------------------------------------------------------------%n");

		for (RentalTransaction rental : activeRentals) {
			out.printf("%-18.18s | %-6s | %-20.20s | %s%n", rental.customerName(), rental.vehicleID(),
					rental.vehicleMake() + " " + rental.vehicleModel(), rental.rentalStartDate());
		}

		RentalReceipt receipt = rentalService.endRental();

		if (receipt != null) {
			out.println("\n- Vehicle Returned -");
			out.println(receipt.toString());
		}

		RentalUtils.waitForEnter(scan);
	}

	private void viewRentals() {
		RentalUtils.clearScreen();
		var activeRentals = rentalService.getActiveRentals();

		out.printf("%-18s | %-3s | %-20s | %s%n", "CUSTOMER", "VEH_ID", "VEHICLE", "RENT DATE");
		out.printf("----------------------------------------------------------------%n");
		for (RentalTransaction rental : activeRentals) {
			out.printf("%-18.18s | %-6s | %-20.20s | %s%n", rental.customerName(), rental.vehicleID(),
					rental.vehicleMake() + " " + rental.vehicleModel(), rental.rentalStartDate());
		}

		RentalUtils.waitForEnter(scan);
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
	}

}
