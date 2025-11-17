package ie.tus.oop;

import static java.lang.System.out;

public class Menu {
	private boolean keepRunning = true;

	public void runApplication() {
		while (keepRunning) {
			showOptions();
			keepRunning = false;
		}
	}

	private void handleChoice(int choice) {
		switch (choice) {
		case 1:
			/* TODO */;
		case 2:
			/* TODO */;

		}
	}

	private void showOptions() {
		out.println("************************************************************");
		out.println("*                                                          *");
		out.println("*               Vehicle Rental Management System           *");
		out.println("*                                                          *");
		out.println("************************************************************");

		out.println("(1) Display Vehicle Inventory");
		out.println("(2) Add New Vehicle to Fleet");
		out.println("(3) Update Vehicle Information");
		out.println("(4) Remove Vehicle from Fleet");
		out.println("(5) Process Vehicle Rental");
		out.println("(6) Process Vehicle Return");
		out.println("(7) View Active Rental Records");
		out.println("(8) Exit");

		// Option selection block
		out.println("");
		out.println("Select Options (1-8) > ");
	}
}
