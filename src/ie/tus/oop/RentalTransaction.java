package ie.tus.oop;

import java.time.LocalDate;

/**
 * Record representing a rental transaction. Contains immutable information
 * about a vehicle rental.
 *
 * @param customerName    the name of the customer
 * @param vehicleID       the ID of the rented vehicle
 * @param vehicleMake     the manufacturer of the rented vehicle
 * @param vehicleModel    the model of the rented vehicle
 * @param rentalStartDate the date the rental started
 *
 * @author Stjepan Tadic
 */
//ADVANCED record
public record RentalTransaction(String customerName, int vehicleID, String vehicleMake, String vehicleModel,
		LocalDate rentalStartDate) {

}
