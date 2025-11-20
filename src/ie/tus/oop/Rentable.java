package ie.tus.oop;

public interface Rentable {
	double getDailyRate();

	boolean isAvailable();

	void setAvailable(boolean available);

	// Default method
	default String getStatus() {
		return isAvailable() ? "Available" : "Rented";
	}

	// Static method
	static double calculateRentalCost(double dailyRate, long numberOfDays) {
		if (numberOfDays < 1) {
			return dailyRate;
		}
		return dailyRate * numberOfDays;
	}
}
