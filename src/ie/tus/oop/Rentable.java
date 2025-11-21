package ie.tus.oop;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Interface defining the contract for rentable items in the system. Provides
 * methods for managing rental availability and calculating costs.
 *
 * @author Stjepan Tadic
 */
public interface Rentable {
	/**
	 * Gets the daily rental rate.
	 *
	 * @return the daily rate
	 */
	double getDailyRate();

	/**
	 * Checks if the item is available for rental.
	 *
	 * @return true if available, false otherwise
	 */
	boolean isAvailable();

	/**
	 * Sets the availability status of the item.
	 *
	 * @param available true to make the item available, false otherwise
	 */
	void setAvailable(boolean available);

	/**
	 * Gets the current rental status as a string. Default implementation returns
	 * "Available" or "Rented".
	 *
	 * @return the status string
	 */
	default String getStatus() {
		return isAvailable() ? "Available" : "Rented";
	}

	/**
	 * Calculates the total rental cost based on the daily rate and rental period.
	 * If the rental period is less than 1 day, charges for 1 day.
	 *
	 * @param dailyRate the daily rental rate
	 * @param startDate the rental start date
	 * @param endDate   the rental end date
	 * @return the total rental cost
	 */
	static double calculateRentalCost(double dailyRate, LocalDate startDate, LocalDate endDate) {
		long numberOfDays = ChronoUnit.DAYS.between(startDate, endDate);
		if (numberOfDays < 1) {
			return dailyRate;
		}
		return dailyRate * numberOfDays;
	}
}
