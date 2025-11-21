package ie.tus.oop;

import static java.lang.System.out;

import java.util.Scanner;

public class InputHandler {
	private final Scanner scan;

	public InputHandler(Scanner scan) {
		this.scan = scan;
	}

	private void checkForQuit(String input) {
		if (input.equalsIgnoreCase(ConsoleUtils.QUIT_COMMAND)) {
			throw new OperationCancelledException("Operation cancelled by user.");
		}
	}

	public int readInt(String prompt) {
		while (true) {
			out.print(ConsoleUtils.GREEN + prompt + ConsoleUtils.RESET);
			String input = scan.nextLine().strip();
			checkForQuit(input);
			try {
				return Integer.parseInt(input);
			} catch (NumberFormatException e) {
				out.println(ConsoleUtils.RED + "Invalid input. Please enter a whole number." + ConsoleUtils.RESET);
			}
		}
	}

	public double readDouble(String prompt) {
		while (true) {
			out.print(ConsoleUtils.GREEN + prompt + ConsoleUtils.RESET);
			String input = scan.nextLine().strip();
			checkForQuit(input);
			try {
				return Double.parseDouble(input);
			} catch (NumberFormatException e) {
				out.println(ConsoleUtils.RED + "Invalid input. Please enter a number." + ConsoleUtils.RESET);
			}
		}
	}

	public String readString(String prompt) {
		while (true) {
			out.print(ConsoleUtils.GREEN + prompt + ConsoleUtils.RESET);
			String input = scan.nextLine().strip();
			checkForQuit(input);
			if (!input.isEmpty()) {
				return input;
			}
			out.println(ConsoleUtils.RED + "Input cannot be empty" + ConsoleUtils.RESET);
		}
	}

	public boolean readBoolean(String prompt) {
		while (true) {
			out.print(ConsoleUtils.GREEN + prompt + ConsoleUtils.RESET);
			try {
				String input = scan.nextLine().strip();
				checkForQuit(input);
				return switch (input.toLowerCase()) {
				case "t" -> true;
				case "f" -> false;
				default -> throw new IllegalArgumentException(ConsoleUtils.RED
						+ "Invalid input. Please enter either 't' for True or 'f' for False" + ConsoleUtils.RESET);
				};
			} catch (IllegalArgumentException e) {
				out.println(e.getMessage());
			}
		}
	}
}
