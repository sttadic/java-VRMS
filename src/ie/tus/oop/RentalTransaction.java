package ie.tus.oop;

import java.util.Date;

public record RentalTransaction(String customerName, int vehicleID, Date rentalStartDate) {

}
