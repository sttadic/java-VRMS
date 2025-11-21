package ie.tus.oop;

import static java.lang.System.out;

import java.util.Scanner;

/**
 * Handles user input with validation and support for operation cancellation.
 * Provides methods for reading different data types from the console.
 *
 * @author Stjepan Tadic
 */
public class InputHandler {
	private final Scanner scan;

	/**
	 * Constructs a new InputHandler with the specified scanner.
	 *
	 * @param scan the Scanner to use for reading input
	 */
	public InputHandler(Scanner scan) {
		this.scan = scan;
	}

	/**
	 * Checks if the user entered the quit command and throws an exception if so.
	 *
	 * @param input the user input to check
	 * @throws OperationCancelledException if the input is the quit command
	 */
	private void checkForQuit(String input) {
		if (input.equalsIgnoreCase(ConsoleUtils.QUIT_COMMAND)) {
			throw new OperationCancelledException("Operation cancelled by user.");
		}
	}

	/**
	 * Reads an integer from the user with validation. Repeatedly prompts until a
	 * valid integer is entered.
	 *
	 * @param prompt the prompt message to display
	 * @return the integer entered by the user
	 * @throws OperationCancelledException if the user enters the quit command
	 */
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

	/**
	 * Reads a double from the user with validation. Repeatedly prompts until a
	 * valid number is entered.
	 *
	 * @param prompt the prompt message to display
	 * @return the double entered by the user
	 * @throws OperationCancelledException if the user enters the quit command
	 */
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

	/**
	 * Reads a non-empty string from the user. Repeatedly prompts until a non-empty
	 * string is entered.
	 *
	 * @param prompt the prompt message to display
	 * @return the string entered by the user
	 * @throws OperationCancelledException if the user enters the quit command
	 */
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

	/**
	 * Reads a boolean value from the user. Accepts 't' for true and 'f' for false.
	 *
	 * @param prompt the prompt message to display
	 * @return the boolean value entered by the user
	 * @throws OperationCancelledException if the user enters the quit command
	 */
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
