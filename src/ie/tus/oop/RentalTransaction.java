package ie.tus.oop;

import java.time.LocalDate;

public record RentalTransaction(String customerName, int vehicleID, String vehicleMake, String vehicleModel,
		LocalDate rentalStartDate) {

}
