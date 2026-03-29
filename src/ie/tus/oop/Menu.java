package ie.tus.oop;

import static java.lang.System.out;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
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
	private final FleetDataManager fleetDataManager;

	/**
	 * Constructs a new Menu and initializes all system components.
	 */
	public Menu() {
		vehicleManager = new VehicleManager();
		inputHandler = new InputHandler(scan);
		rentalService = new RentalService(vehicleManager, inputHandler);
		fleetDataManager = new FleetDataManager();
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
		int choice = inputHandler.readInt(ConsoleUtils.GREEN + "\nSelect Option (1-12) > " + ConsoleUtils.RESET);

		switch (choice) {
		case 1 -> {
			ConsoleUtils.clearScreen();
			out.println("VEHICLE INVENTORY\n");
			displayVehicleInventory();
			displaySortSubMenu();
		}
		case 2 -> addNewVehicle();
		case 3 -> removeVehicle();
		case 4 -> updateRentalPrice();
		case 5 -> handleNewRental();
		case 6 -> handleVehicleReturn();
		case 7 -> viewRentals();
		case 8 -> showFleetStatistics();
		case 9 -> exportFleet();
		case 10 -> importFleet();
		case 11 -> changeLanguage();
		case 12 -> keepRunning = false;
		default -> throw new InvalidChoiceException("\nInvalid Selection! Please choose an option from 1 to 12.");

		}
	}

	/**
	 * Displays the complete vehicle inventory including all vehicle details.
	 */
	private void displayVehicleInventory() {
		// FUNDAMENTALS lvti - local variable type inference
		var vehicles = vehicleManager.getAllVehicles();

		ConsoleUtils.displayVehicleInventoryTable(vehicles, vehicleManager.getFleetSize(),
				vehicleManager.getAvailableVehicleCount());
	}

	/**
	 * Displays the sort sub-menu and re-displays the inventory in the chosen order.
	 */
	private void displaySortSubMenu() {
		while (true) {
			out.println("\nSort by:");
			out.println("(1) Daily rate (low \u2192 high)");
			out.println("(2) Model (A \u2192 Z)");
			out.println("(3) Vehicle type");
			out.println("(4) Available first");
			out.println("(5) Rate, then model");
			out.println("(6) Back to main menu");

			try {
				int choice = inputHandler.readInt("Select sort option > ");

				if (choice == 6) {
					break;
				}

				// ADVANCED Comparator.comparing() with method references and chaining
				Comparator<Vehicle> comparator = switch (choice) {
				case 1 -> Comparator.comparing(Vehicle::getDailyRate);
				case 2 -> Comparator.comparing(Vehicle::getModel);
				case 3 -> Comparator.comparing(Vehicle::getVehicleType);
				case 4 -> Comparator.comparing(Vehicle::isAvailable).reversed();
				case 5 -> Comparator.comparing(Vehicle::getDailyRate).thenComparing(Vehicle::getModel);
				default -> null;
				};

				if (comparator != null) {
					ConsoleUtils.clearScreen();
					out.println("VEHICLE INVENTORY\n");
					var sorted = vehicleManager.getSortedVehicles(comparator);
					ConsoleUtils.displayVehicleInventoryTable(sorted, vehicleManager.getFleetSize(),
							vehicleManager.getAvailableVehicleCount());
				}
			} catch (OperationCancelledException e) {
				break;
			}
		}
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
		// ADVANCED switch expression
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

		try {
			Vehicle vehicleToRemove = inputHandler
					.selectVehicle("\nEnter ID of the vehicle you want to remove from fleet > ", vehicleManager);

			var removed = vehicleManager.removeVehicleById(vehicleToRemove.getVehicleId());
			if (!removed) {
				out.println(ConsoleUtils.RED + "Something went wrong. Please try again." + ConsoleUtils.RESET);
			} else {
				out.println("\nVehicle with ID " + vehicleToRemove.getVehicleId() + " was successfully removed.");
			}
		} catch (OperationCancelledException e) {
			return;
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
			Vehicle vehicleToUpdate = inputHandler.selectVehicle("\nEnter the ID of the vehicle to update > ",
					vehicleManager);

			out.println("Current vehicle details: " + vehicleToUpdate.getVehicleId() + ". " + vehicleToUpdate.getMake()
					+ " " + vehicleToUpdate.getModel() + " - " + vehicleToUpdate.formatDailyRate());

			double newRate = inputHandler.readDouble("Enter new Daily Rate: ");
			vehicleToUpdate.setDailyRate(newRate);
			out.println("\nPrice updated.");
		} catch (OperationCancelledException e) {
			return;
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
	 * Displays a comprehensive fleet statistics report using a broad range of
	 * stream terminal and intermediate operations.
	 */
	private void showFleetStatistics() {
		ConsoleUtils.clearScreen();
		out.println("FLEET STATISTICS\n");
		out.println(vehicleManager.getFleetStatistics());

		// ADVANCED Consumer<Vehicle> lambda — forEach used to print each vehicle summary
		out.println("Full fleet:");
		vehicleManager.forEachVehicle(
				v -> out.printf("  %d. %s %s%n", v.getVehicleId(), v.getMake(), v.getModel()));

		// ADVANCED Stream Gatherers (JEP 485)
		out.println("\n--- VEHICLE TIERS ---\n");
		out.println(vehicleManager.getVehicleTiers());

		ConsoleUtils.waitForEnter(scan);
	}
	
	/**
	 * Handles exporting the fleet to a CSV file.
	 */
	// ADVANCED NIO2 - Path.of, Files operations
	private void exportFleet() {
		ConsoleUtils.clearScreen();
		out.println("EXPORT FLEET TO FILE (Enter :q to cancel)\n");

		try {
			String filename = inputHandler.readString("Enter filename in .csv format (e.g. data/fleet.csv) > ");
			if (!filename.trim().endsWith(".csv")) {
				out.println(ConsoleUtils.RED + "Invalid file type. Defaulting to data/fleet.csv");
				filename = "data/fleet.csv";
			}

			// ADVANCED NIO2 - Path.of creates a Path object
			Path path = Path.of(filename);
			List<Vehicle> vehicles = vehicleManager.getAllVehicles();

			fleetDataManager.exportFleet(vehicles, path);
			out.println(ConsoleUtils.GREEN + "\nFleet exported successfully to: " + path + ConsoleUtils.RESET);
			out.println("Total vehicles exported: " + vehicles.size());

		} catch (OperationCancelledException e) {
			return;
		} catch (IOException e) {
			out.println(ConsoleUtils.RED + "\nExport failed: " + e.getMessage() + ConsoleUtils.RESET);
		}

		ConsoleUtils.waitForEnter(scan);
	}

	/**
	 * Handles importing the fleet from a CSV file.
	 */
	// ADVANCED NIO2 - Path.of, Files operations
	private void importFleet() {
		ConsoleUtils.clearScreen();
		out.println("IMPORT FLEET FROM FILE (Enter :q to cancel)\n");
		out.println(ConsoleUtils.RED + "WARNING: This will replace the current fleet!" + ConsoleUtils.RESET);

		try {
			String filename = inputHandler.readString("Enter filename in .csv format (e.g. data/fleet.csv) > ");

			// ADVANCED NIO2 - Path.of creates a Path object
			Path path = Path.of(filename);

			boolean confirm = inputHandler.readBoolean("Are you sure? (y)es/(n)o > ");
			if (!confirm) {
				out.println("\nImport cancelled.");
				ConsoleUtils.waitForEnter(scan);
				return;
			}

			List<Vehicle> importedVehicles = fleetDataManager.importFleet(path);
			vehicleManager.replaceFleet(importedVehicles);

			out.println(ConsoleUtils.GREEN + "\nFleet imported successfully from: " + path + ConsoleUtils.RESET);
			out.println("Total vehicles imported: " + importedVehicles.size());

		} catch (OperationCancelledException e) {
			return;
		} catch (IOException e) {
			out.println(ConsoleUtils.RED + "\nImport failed: " + e.getMessage() + ConsoleUtils.RESET);
		}

		ConsoleUtils.waitForEnter(scan);
	}

	/**
	 * Handles toggling the application language between English and Irish.
	 */
	// ADVANCED Localisation - switching locale at runtime
	private void changeLanguage() {
		ConsoleUtils.clearScreen();
		out.println("CHANGE LANGUAGE / ATHRAIGH TEANGA\n");
		out.println("(1) English");
		out.println("(2) Croatian (Hrvatski)");

		try {
			int choice = inputHandler.readInt("Select language > ");
			switch (choice) {
			case 1 -> LocaleManager.setLocale(Locale.of("en", "IE"));
			case 2 -> LocaleManager.setLocale(Locale.of("hr", "HR"));
			default -> out.println("Invalid choice.");
			}
		} catch (OperationCancelledException e) {
			return;
		}

		ConsoleUtils.waitForEnter(scan);
	}

	/**
	 * Displays the main menu options.
	*/
	// ADVANCED Localisation - menu strings sourced from active ResourceBundle
	private void showOptions() {
		String title = LocaleManager.getString("menu.title");
		int innerWidth = 58;
		int padding = (innerWidth - title.length()) / 2;
		String centeredTitle = "*" + " ".repeat(padding) + title
				+ " ".repeat(innerWidth - title.length() - padding) + "*";

		out.println("************************************************************");
		out.println("*                                                          *");
		out.println(centeredTitle);
		out.println("*                                                          *");
		out.println("************************************************************");

		out.println("(1)  " + LocaleManager.getString("menu.option1"));
		out.println("(2)  " + LocaleManager.getString("menu.option2"));
		out.println("(3)  " + LocaleManager.getString("menu.option3"));
		out.println("(4)  " + LocaleManager.getString("menu.option4"));
		out.println("(5)  " + LocaleManager.getString("menu.option5"));
		out.println("(6)  " + LocaleManager.getString("menu.option6"));
		out.println("(7)  " + LocaleManager.getString("menu.option7"));
		out.println("(8)  " + LocaleManager.getString("menu.option8"));
		out.println("(9)  " + LocaleManager.getString("menu.option9"));
		out.println("(10) " + LocaleManager.getString("menu.option10"));
		out.println("(11) " + LocaleManager.getString("menu.option11"));
		out.println("(12) " + LocaleManager.getString("menu.option12"));
	}


}
