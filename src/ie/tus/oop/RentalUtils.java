package ie.tus.oop;

import java.util.Scanner;

public class RentalUtils {

	public static void waitForEnter(Scanner scan) {
		System.out.println("\nPress Enter to return to main menu...");
		scan.nextLine();
	}

	// Clears terminal screen - does not work in IDE console
	public static void clearScreen() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}
}
