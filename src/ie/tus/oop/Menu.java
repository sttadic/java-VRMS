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
			ConsoleUtils.clearScreen();
			showOptions();
			try {
				handleChoice();
			} catch (InvalidChoiceException e) {
				out.println(e.getMessage());
				ConsoleUtils.waitForEnter(scan);
			}
		}
		scan.close();
	}

	private void handleChoice() {
		int choice = inputHandler.readInt("\nSelect Option (1-8) > ");

		switch (choice) {
		case 1 -> {
			ConsoleUtils.clearScreen();
			out.println("VEHICLE INVENTORY\n");
			displayVehicleInventory();
			ConsoleUtils.waitForEnter(scan);
		}
		case 2 -> addNewVehicle();
		case 3 -> removeVehicle();
		case 4 -> updateRentalPrice();
		case 5 -> handleNewRental();
		case 6 -> handleVehicleReturn();
		case 7 -> viewRentals();
		case 8 -> keepRunning = false;
		default -> throw new InvalidChoiceException("\nInvalid Selection! Please choose an option from 1 to 8.");

		}
	}

	private void displayVehicleInventory() {
		var vehicles = vehicleManager.getAllVehicles();
		if (vehicles.isEmpty()) {
			out.println("No vehicles in inventory");
			return;
		}
		out.println();
		out.printf(ConsoleUtils.VEHICLE_HEADER_FORMAT, ConsoleUtils.VEHICLE_HEADER);
		out.printf(
				"----------------------------------------------------------------------------------------------------------------------------------%n");

		for (Vehicle vehicle : vehicles) {
			out.printf("%2d  | %s", vehicle.getVehicleId(), vehicle.toString());

			String additionalSpecs = vehicle.getSpecs();
			if (!additionalSpecs.isEmpty()) {
				out.println(additionalSpecs);
			}
		}
		out.println("\nTotal count of vehicles --> " + vehicleManager.getFleetSize());
		out.println("Available for rental -----> " + vehicleManager.getAvailableVehicleCount());
	}

	private void addNewVehicle() {
		ConsoleUtils.clearScreen();
		out.println("ADD NEW VEHICLE TO FLEET (Enter :q to cancel)\n");

		try {
			VehicleType vehicleType = selectVehicleType();
			String make = inputHandler.readString("Enter Make > ");
			String model = inputHandler.readString("Enter Model > ");
			String colour = inputHandler.readString("Enter Colour > ");
			double dailyRate = inputHandler.readDouble("Enter Daily Rate > ");
			FuelType fuelType = selectFuelType();

			Vehicle newVehicle = createVehicle(vehicleType, make, model, colour, fuelType, dailyRate);

			vehicleManager.addVehicles(newVehicle);
			out.println("\nVehicle added successfully.");

		} catch (OperationCancelledException e) {
			return;
		}

		ConsoleUtils.waitForEnter(scan);
	}

	private VehicleType selectVehicleType() {
		out.println("(1) Car");
		out.println("(2) Van");
		out.println("(3) Bike");

		while (true) {
			try {
				int typeChoice = inputHandler.readInt("Select vehicle type to add > ");
				return VehicleType.values()[typeChoice - 1];
			} catch (ArrayIndexOutOfBoundsException e) {
				out.println("Invalid value selected. Please enter one of the expected values.");
			}
		}
	}

	private FuelType selectFuelType() {
		out.println("\n(1) Petrol");
		out.println("(2) Diesel");
		out.println("(3) Electric");
		out.println("(4) Hybrid");
		out.println("(5) None");

		while (true) {
			try {
				int fuelChoice = inputHandler.readInt("Select Fuel Type > ");
				return FuelType.values()[fuelChoice - 1];
			} catch (ArrayIndexOutOfBoundsException e) {
				out.println("Invalid value selected. Please enter one of the expected values.");
			}
		}
	}

	private Vehicle createVehicle(VehicleType type, String make, String model, String colour, FuelType fuelType,
			double dailyRate) {
		return switch (type) {
		case CAR -> {
			boolean hasAirConditioning = inputHandler.readBoolean("Has Air Conditioning? (t)rue/(f)alse > ");
			boolean hasNavigation = inputHandler.readBoolean("Has Navigation? (t)rue/(f)alse > ");
			yield new Car(make, model, colour, fuelType, dailyRate, hasAirConditioning, hasNavigation);
		}
		case VAN -> {
			double cargoCapacity = inputHandler.readDouble("Enter Cargo Capacity > ");
			yield new Van(make, model, colour, fuelType, dailyRate, cargoCapacity);
		}
		case BIKE -> {
			int wheelSize = inputHandler.readInt("Enter Wheel Size > ");
			yield new Bike(make, model, colour, fuelType, dailyRate, wheelSize);
		}
		};
	}

	private void removeVehicle() {
		ConsoleUtils.clearScreen();
		out.println("REMOVE VEHICLE FROM FLEET (Enter :q to cancel)\n");
		displayVehicleInventory();

		int vehicleId;
		try {
			vehicleId = inputHandler.readInt("\nEnter ID of the vehicle you want to remove from fleet > ");
		} catch (OperationCancelledException e) {
			return;
		}

		var removed = vehicleManager.removeVehicleById(vehicleId);
		if (removed) {
			out.println("\nVehicle with ID " + vehicleId + " was successfully removed.");
		} else {
			out.println("\nVehicle with ID " + vehicleId + " does not exist!");
		}

		ConsoleUtils.waitForEnter(scan);
	}

	private void updateRentalPrice() {
		ConsoleUtils.clearScreen();
		out.println("UPDATE VEHICLE RENTAL PRICE (Enter :q to cancel)\n");
		displayVehicleInventory();

		try {
			int vehicleId = inputHandler.readInt("\nEnter the ID of the vehicle to update > ");
			Vehicle vehicleToUpdate = vehicleManager.getVehicleById(vehicleId);

			if (vehicleToUpdate == null) {
				out.println("Vehicle with ID " + vehicleId + " not found.");
				ConsoleUtils.waitForEnter(scan);
				return;
			}

			out.println("Current vehicle details: " + vehicleId + ". " + vehicleToUpdate.getMake() + " "
					+ vehicleToUpdate.getModel() + " - €" + String.format("%.2f", vehicleToUpdate.getDailyRate()));

			double newRate = inputHandler.readDouble("Enter new Daily Rate: ");

			vehicleToUpdate.setDailyRate(newRate);
			out.println("\nPrice updated.");
		} catch (OperationCancelledException e) {
			return;
		}

		ConsoleUtils.waitForEnter(scan);
	}

	private void handleNewRental() {
		ConsoleUtils.clearScreen();
		out.println("PROCESS NEW RENTAL (Enter :q to cancel)\n");

		try {
			String customerName = inputHandler.readString("Customer name > ");
			displayVehicleInventory();

			rentalService.startRental(customerName);
		} catch (OperationCancelledException e) {
			return;
		}

		ConsoleUtils.waitForEnter(scan);
	}

	private void handleVehicleReturn() {
		ConsoleUtils.clearScreen();
		out.println("PROCESS VEHICLE RETURN (Enter :q to cancel)\n\n");
		ConsoleUtils.displayActiveRentalsTable(rentalService.getActiveRentals());

		try {
			RentalReceipt receipt = rentalService.endRental();
			if (receipt != null) {
				out.println("\n- Vehicle Returned -");
				out.println(receipt.toString());
			}
		} catch (OperationCancelledException e) {
			return;
		}

		ConsoleUtils.waitForEnter(scan);
	}

	private void viewRentals() {
		ConsoleUtils.clearScreen();
		out.println("ACTIVE RENTAL RECORDS\n\n");
		ConsoleUtils.displayActiveRentalsTable(rentalService.getActiveRentals());
		ConsoleUtils.waitForEnter(scan);
	}

	private void showOptions() {
		out.println("************************************************************");
		out.println("*                                                          *");
		out.println("*             Vehicle Rental Management System             *");
		out.println("*                                                          *");
		out.println("************************************************************");

		out.println("(1) Display Vehicle Inventory");
		out.println("(2) Add New Vehicle to Fleet");
		out.println("(3) Remove Vehicle from Fleet");
		out.println("(4) Update Rental Price");
		out.println("(5) Process Vehicle Rental");
		out.println("(6) Process Vehicle Return");
		out.println("(7) View Active Rental Records");
		out.println("(8) Exit");
	}

}
