package ie.tus.oop;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public interface Rentable {
	double getDailyRate();

	boolean isAvailable();

	void setAvailable(boolean available);

	// Default method
	default String getStatus() {
		return isAvailable() ? "Available" : "Rented";
	}

	// Static method
	static double calculateRentalCost(double dailyRate, LocalDate startDate, LocalDate endDate) {
		long numberOfDays = ChronoUnit.DAYS.between(startDate, endDate);
		if (numberOfDays < 1) {
			return dailyRate;
		}
		return dailyRate * numberOfDays;
	}
}
