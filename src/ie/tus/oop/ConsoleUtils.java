package ie.tus.oop;

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleUtils {
	public static final String VEHICLE_HEADER_FORMAT = "%3s | %-5s | %-15s | %-17s | %-8s | %-9s | %-4s | %-10s | %s%n";
	public static final String VEHICLE_ROW_FORMAT = "%-5s | %-15s | %-17s | %-8s | %-9s | €%-8.2f | %-9s";
	public static final String RENTAL_HEADER_FORMAT = "%-18s | %-3s | %-20s | %s%n";
	public static final String RENTAL_ROW_FORMAT = "%-18.18s | %-6s | %-20.20s | %s%n";
	public static final Object[] VEHICLE_HEADER = { "ID", "TYPE", "MODEL", "MAKE", "COLOUR", "FUEL", "PRICE/DAY",
			"AVAILABILITY", "DETAILS" };
	public static final Object[] RENTAL_HEADER = { "CUSTOMER", "VEH_ID", "VEHICLE", "RENT DATE" };
	public static final String QUIT_COMMAND = ":q";

	public static void waitForEnter(Scanner scan) {
		System.out.println("\nPress Enter to return to menu...");
		scan.nextLine();
	}

	// Clears terminal screen - does not work in IDE console
	public static void clearScreen() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

	static void displayActiveRentalsTable(ArrayList<RentalTransaction> rentals) {
		out.printf(ConsoleUtils.RENTAL_HEADER_FORMAT, ConsoleUtils.RENTAL_HEADER);
		out.printf("----------------------------------------------------------------%n");

		for (RentalTransaction rental : rentals) {
			out.printf(ConsoleUtils.RENTAL_ROW_FORMAT, rental.customerName(), rental.vehicleID(),
					rental.vehicleMake() + " " + rental.vehicleModel(), rental.rentalStartDate());
		}
	}
}
