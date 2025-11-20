package ie.tus.oop;

import java.time.LocalDate;

public final class RentalReceipt {
	private final String customerName;
	private final int vehicleId;
	private final String vehicleDescription;
	private final LocalDate rentalStartDate;
	private final LocalDate rentalEndDate;
	private final double totalCost;

	public RentalReceipt(String customerName, int vehicleID, String vehicleDescription, LocalDate rentalStartDate,
			LocalDate rentalEndDate, double totalCost) {
		this.customerName = customerName;
		this.vehicleId = vehicleID;
		this.vehicleDescription = vehicleDescription;
		this.rentalStartDate = rentalStartDate;
		this.rentalEndDate = rentalEndDate;
		this.totalCost = totalCost;
	}

	public String getCustomerName() {
		return customerName;
	}

	public int getVehicleId() {
		return vehicleId;
	}

	public String getVehicleDescription() {
		return vehicleDescription;
	}

	public LocalDate getRentalStartDate() {
		return rentalStartDate;
	}

	public LocalDate getRentalEndDate() {
		return rentalEndDate;
	}

	public double getTotalCost() {
		return totalCost;
	}

	@Override
	public String toString() {
		return String.format(
				"Receipt for: %s%n" + "Vehicle: %s (ID: %d)%n" + "Rental Period: %s to %s%n" + "Total Cost: €%.2f",
				customerName, vehicleDescription, vehicleId, rentalStartDate, rentalEndDate, totalCost);
	}

}
