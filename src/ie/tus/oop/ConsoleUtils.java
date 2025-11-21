package ie.tus.oop;

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Utility class providing console output formatting and display helpers.
 * Contains constants for table formatting and ANSI color codes, as well as
 * methods for managing console display.
 *
 * @author Stjepan Tadic
 */
public class ConsoleUtils {
	/** Format string for vehicle inventory table header */
	public static final String VEHICLE_HEADER_FORMAT = "%3s | %-5s | %-15s | %-17s | %-8s | %-9s | %-4s | %-10s | %s%n";
	/** Format string for vehicle inventory table rows */
	public static final String VEHICLE_ROW_FORMAT = "%-5s | %-15s | %-17s | %-8s | %-9s | €%-8.2f | %-9s";
	/** Format string for rental records table header */
	public static final String RENTAL_HEADER_FORMAT = "%-18s | %-3s | %-20s | %s%n";
	/** Format string for rental records table rows */
	public static final String RENTAL_ROW_FORMAT = "%-18.18s | %-6s | %-20.20s | %s%n";
	/** Column headers for vehicle inventory table */
	public static final Object[] VEHICLE_HEADER = { "ID", "TYPE", "MODEL", "MAKE", "COLOUR", "FUEL", "PRICE/DAY",
			"AVAILABILITY", "DETAILS" };
	/** Column headers for rental records table */
	public static final Object[] RENTAL_HEADER = { "CUSTOMER", "VEH_ID", "VEHICLE", "RENT DATE" };
	/** Command string to quit/cancel an operation */
	public static final String QUIT_COMMAND = ":q";
	/** ANSI escape code for green text color */
	public static final String GREEN = "\u001B[32m";
	/** ANSI escape code for red text color */
	public static final String RED = "\u001B[31m";
	/** ANSI escape code to reset text formatting */
	public static final String RESET = "\u001B[0m";

	/**
	 * Waits for the user to press Enter before continuing. Displays a prompt
	 * message and consumes the next line of input.
	 *
	 * @param scan the Scanner to read input from
	 */
	public static void waitForEnter(Scanner scan) {
		System.out.println("\nPress Enter to return to menu...");
		scan.nextLine();
	}

	/**
	 * Clears the terminal screen using ANSI escape codes. Note: This does not work
	 * in IDE consoles, only in actual terminals.
	 */
	public static void clearScreen() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

	/**
	 * Displays a formatted table of active rental transactions. Shows customer
	 * name, vehicle ID, vehicle description, and rental start date.
	 *
	 * @param rentals the list of active rental transactions to display
	 */
	static void displayActiveRentalsTable(ArrayList<RentalTransaction> rentals) {
		out.printf(ConsoleUtils.RENTAL_HEADER_FORMAT, ConsoleUtils.RENTAL_HEADER);
		out.printf("----------------------------------------------------------------%n");

		for (RentalTransaction rental : rentals) {
			out.printf(ConsoleUtils.RENTAL_ROW_FORMAT, rental.customerName(), rental.vehicleID(),
					rental.vehicleMake() + " " + rental.vehicleModel(), rental.rentalStartDate());
		}
	}
}
