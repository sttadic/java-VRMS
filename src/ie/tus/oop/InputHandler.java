package ie.tus.oop;

import static java.lang.System.out;

import java.util.Scanner;

public class InputHandler {
	private final Scanner scan;

	public InputHandler(Scanner scan) {
		this.scan = scan;
	}

	public int readInt(String prompt) {
		while (true) {
			out.print(prompt);
			try {
				return Integer.parseInt(scan.nextLine().strip());
			} catch (NumberFormatException e) {
				out.println("Invalid input. Please eneter a whole number.");
			}
		}
	}

	public double readDouble(String prompt) {
		while (true) {
			out.print(prompt);
			try {
				return Double.parseDouble(scan.nextLine().strip());
			} catch (NumberFormatException e) {
				out.println("Invalid input. Please enter a number.");
			}
		}
	}

	public String readString(String prompt) {
		while (true) {
			out.print(prompt);
			String input = scan.nextLine().strip();
			if (!input.isEmpty()) {
				return input;
			}
			out.println("Input cannot be empty");
		}
	}

	public boolean readBoolean(String prompt) {
		while (true) {
			out.print(prompt);
			try {
				String input = scan.nextLine().strip();
				return switch (input.toLowerCase()) {
				case "t" -> true;
				case "f" -> false;
				default -> throw new IllegalArgumentException(
						"Invalid input. Please enter either 't' for True or 'f' for False");
				};
			} catch (IllegalArgumentException e) {
				out.println(e.getMessage());
			}
		}
	}
}
