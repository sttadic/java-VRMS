package ie.tus.oop;

import static java.lang.System.out;

public class Menu {
	private boolean keepRunning = true;

	public void runApplication() {
		while (keepRunning) {
			showOptions();
		}
	}

	private void showOptions() {
		out.println("************************************************************");
		out.println("*                                                          *");
		out.println("*                   Car Rental Manager                     *");
		out.println("*                                                          *");
		out.println("************************************************************");
	}
}
