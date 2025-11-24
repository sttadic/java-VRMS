package ie.tus.oop;

import java.time.LocalDate;

/**
 * Immutable class representing a rental receipt generated when a vehicle is
 * returned. Contains all relevant information about the rental transaction
 * including customer details, vehicle information, rental period, and total
 * cost.
 *
 * @author Stjepan Tadic
 */
//ADVANCED custom immutable type
public final class RentalReceipt {
	private final String customerName;
	private final int vehicleId;
	private final String vehicleDescription;
	private final LocalDate rentalStartDate;
	private final LocalDate rentalEndDate;
	private final double totalCost;

	/**
	 * Constructs a new RentalReceipt with the specified details.
	 *
	 * @param customerName       the name of the customer
	 * @param vehicleID          the unique identifier of the rented vehicle
	 * @param vehicleDescription a description of the vehicle (make and model)
	 * @param rentalStartDate    the date the rental started
	 * @param rentalEndDate      the date the rental ended
	 * @param totalCost          the total cost of the rental
	 */
	public RentalReceipt(String customerName, int vehicleID, String vehicleDescription, LocalDate rentalStartDate,
			LocalDate rentalEndDate, double totalCost) {
		this.customerName = customerName;
		this.vehicleId = vehicleID;
		this.vehicleDescription = vehicleDescription;
		this.rentalStartDate = rentalStartDate;
		this.rentalEndDate = rentalEndDate;
		this.totalCost = totalCost;
	}

	/**
	 * Gets the customer's name.
	 *
	 * @return the customer name
	 */
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * Gets the vehicle's unique identifier.
	 *
	 * @return the vehicle ID
	 */
	public int getVehicleId() {
		return vehicleId;
	}

	/**
	 * Gets the vehicle description.
	 *
	 * @return the vehicle description (make and model)
	 */
	public String getVehicleDescription() {
		return vehicleDescription;
	}

	/**
	 * Gets the rental start date.
	 *
	 * @return the date the rental started
	 */
	public LocalDate getRentalStartDate() {
		return rentalStartDate;
	}

	/**
	 * Gets the rental end date.
	 *
	 * @return the date the rental ended
	 */
	public LocalDate getRentalEndDate() {
		return rentalEndDate;
	}

	/**
	 * Gets the total cost of the rental.
	 *
	 * @return the total rental cost
	 */
	public double getTotalCost() {
		return totalCost;
	}

	/**
	 * Returns a formatted string representation of the receipt. Includes customer
	 * name, vehicle details, rental period, and total cost.
	 *
	 * @return a formatted receipt string
	 */
	@Override
	public String toString() {
		return String.format(
				"Receipt for: %s%n" + "Vehicle: %s (ID: %d)%n" + "Rental Period: %s to %s%n" + "Total Cost: €%.2f",
				customerName, vehicleDescription, vehicleId, rentalStartDate, rentalEndDate, totalCost);
	}

}
