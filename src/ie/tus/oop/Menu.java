package ie.tus.oop;

import static java.lang.System.out;

import java.util.Scanner;

/**
 * Main menu controller for the Vehicle Rental Management System. Handles user
 * interaction and coordinates between different system components.
 *
 * @author Stjepan Tadic
 */
public class Menu {
	private boolean keepRunning = true;
	private final Scanner scan = new Scanner(System.in);
	private final VehicleManager vehicleManager;
	private final InputHandler inputHandler;
	private final RentalService rentalService;

	/**
	 * Constructs a new Menu and initializes all system components.
	 */
	public Menu() {
		vehicleManager = new VehicleManager();
		inputHandler = new InputHandler(scan);
		rentalService = new RentalService(vehicleManager, inputHandler);
	}

	/**
	 * Runs the main application loop. Displays the menu, handles user choices, and
	 * manages exceptions.
	 */
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

	/**
	 * Handles the user's menu choice and delegates to appropriate methods.
	 *
	 * @throws InvalidChoiceException if the user enters an invalid choice
	 */
	private void handleChoice() {
		int choice = inputHandler.readInt(ConsoleUtils.GREEN + "\nSelect Option (1-8) > " + ConsoleUtils.RESET);

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

	/**
	 * Displays the complete vehicle inventory including all vehicle details.
	 */
	private void displayVehicleInventory() {
		var vehicles = vehicleManager.getAllVehicles();

		ConsoleUtils.displayVehicleInventoryTable(vehicles, vehicleManager.getFleetSize(),
				vehicleManager.getAvailableVehicleCount());
	}

	/**
	 * Handles the process of adding a new vehicle to the fleet. Prompts for all
	 * necessary vehicle details and creates the appropriate vehicle type.
	 */
	private void addNewVehicle() {
		ConsoleUtils.clearScreen();
		out.println("ADD NEW VEHICLE TO FLEET (Enter :q to cancel)\n");

		try {
			VehicleType vehicleType = selectVehicleType();
			String make = inputHandler.readString("Enter Make > ");
			String model = inputHandler.readString("Enter Model > ");
			String colour = inputHandler.readString("Enter Colour > ");
			double dailyRate = inputHandler.readDouble("Enter Daily Rate > ");
			Vehicle.validateDailyRate(dailyRate);
			FuelType fuelType = selectFuelType();

			Vehicle newVehicle = createVehicle(vehicleType, make, model, colour, fuelType, dailyRate);

			vehicleManager.addVehicles(newVehicle);
			out.println("\nVehicle added successfully.");

		} catch (OperationCancelledException e) {
			return;
		} catch (IllegalArgumentException e) {
			out.println(e.getMessage());
		}

		ConsoleUtils.waitForEnter(scan);
	}

	/**
	 * Prompts the user to select a vehicle type.
	 *
	 * @return the selected VehicleType
	 */
	private VehicleType selectVehicleType() {
		out.println("(1) Car");
		out.println("(2) Van");
		out.println("(3) Bike");

		while (true) {
			try {
				int typeChoice = inputHandler.readInt("Select vehicle type to add > ");
				return VehicleType.values()[typeChoice - 1];
			} catch (ArrayIndexOutOfBoundsException e) {
				out.println(ConsoleUtils.RED + "Invalid value selected. Please enter one of the expected values."
						+ ConsoleUtils.RESET);
			}
		}
	}

	/**
	 * Prompts the user to select a fuel type.
	 *
	 * @return the selected FuelType
	 */
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
				out.println(ConsoleUtils.RED + "Invalid value selected. Please enter one of the expected values."
						+ ConsoleUtils.RESET);
			}
		}
	}

	/**
	 * Creates a vehicle of the specified type with the given details. Prompts for
	 * type-specific attributes.
	 *
	 * @param type      the type of vehicle to create
	 * @param make      the manufacturer
	 * @param model     the model name
	 * @param colour    the colour
	 * @param fuelType  the fuel type
	 * @param dailyRate the daily rental rate
	 * @return the created Vehicle
	 */
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

	/**
	 * Handles the process of removing a vehicle from the fleet.
	 *
	 */
	private void removeVehicle() {
		ConsoleUtils.clearScreen();
		out.println("REMOVE VEHICLE FROM FLEET (Enter :q to cancel)\n");
		displayVehicleInventory();

		while (true) {
			int vehicleId;
			try {
				vehicleId = inputHandler.readInt("\nEnter ID of the vehicle you want to remove from fleet > ");

				var removed = vehicleManager.removeVehicleById(vehicleId);
				if (removed) {
					out.println("\nVehicle with ID " + vehicleId + " was successfully removed.");
					break;
				}
				out.println(
						ConsoleUtils.RED + "Vehicle with ID " + vehicleId + " does not exist!" + ConsoleUtils.RESET);
			} catch (OperationCancelledException e) {
				return;
			}
		}

		ConsoleUtils.waitForEnter(scan);
	}

	/**
	 * Handles the process of updating a vehicle's rental price.
	 *
	 */
	private void updateRentalPrice() {
		ConsoleUtils.clearScreen();
		out.println("UPDATE VEHICLE RENTAL PRICE (Enter :q to cancel)\n");
		displayVehicleInventory();

		try {
			int vehicleId = inputHandler.readInt("\nEnter the ID of the vehicle to update > ");
			Vehicle vehicleToUpdate = vehicleManager.getVehicleById(vehicleId);

			out.println("Current vehicle details: " + vehicleId + ". " + vehicleToUpdate.getMake() + " "
					+ vehicleToUpdate.getModel() + " - " + vehicleToUpdate.formatDailyRate());

			double newRate = inputHandler.readDouble("Enter new Daily Rate: ");
			Vehicle.validateDailyRate(newRate);

			vehicleToUpdate.setDailyRate(newRate);
			out.println("\nPrice updated.");
		} catch (OperationCancelledException e) {
			return;
		} catch (VehicleNotAvailableException e) {
			out.println(e.getMessage());
		}
		ConsoleUtils.waitForEnter(scan);
	}

	/**
	 * Handles the process of creating a new rental transaction.
	 */
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

	/**
	 * Handles the process of returning a rented vehicle and generating a receipt.
	 */
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

	/**
	 * Displays all active rental transactions.
	 */
	private void viewRentals() {
		ConsoleUtils.clearScreen();
		out.println("ACTIVE RENTAL RECORDS\n\n");
		ConsoleUtils.displayActiveRentalsTable(rentalService.getActiveRentals());
		ConsoleUtils.waitForEnter(scan);
	}

	/**
	 * Displays the main menu options.
	 */
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
