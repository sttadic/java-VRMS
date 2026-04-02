package ie.tus.oop;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages file-based import and export of vehicle fleet data using NIO2 APIs.
 * Handles CSV serialization and deserialization of the vehicle inventory.
 *
 * @author Stjepan Tadic
 */
public class FleetDataManager {

	/**
	 * Exports the vehicle fleet to a CSV file at the specified path.
	 * CSV format: TYPE,make,model,colour,FUELTYPE,dailyRate[,spec1,spec2,...]
	 *
	 * @param vehicles the list of vehicles to export
	 * @param path     the file path to write to
	 * @throws IOException if an I/O error occurs during export
	 */
	// ADVANCED NIO2 - Path, Files.createDirectories, Files.writeString
	public void exportFleet(List<Vehicle> vehicles, Path path) throws IOException {
		// NIO2 - ensure parent directory exists
		if (path.getParent() != null && !Files.exists(path.getParent())) {
			Files.createDirectories(path.getParent());
		}

		// Build CSV content
		StringBuilder csvContent = new StringBuilder();
		// Header
		csvContent.append("TYPE,Make,Model,Colour,FuelType,DailyRate,Specs\n");

		for (Vehicle v : vehicles) {
			csvContent.append(vehicleToCsvLine(v)).append("\n");
		}

		// NIO2 - write string to file
		Files.writeString(path, csvContent.toString());
	}

	/**
	 * Imports a vehicle fleet from a CSV file at the specified path.
	 *
	 * @param path the file path to read from
	 * @return the list of vehicles loaded from the file
	 * @throws IOException if an I/O error occurs or the file format is invalid
	 */
	// ADVANCED NIO2 - Path, Files.exists, Files.lines (returns Stream<String>)
	public List<Vehicle> importFleet(Path path) throws IOException {
		// NIO2 - check file exists
		if (!Files.exists(path)) {
			throw new IOException("File not found: " + path);
		}

		List<Vehicle> vehicles = new ArrayList<>();

		// NIO2 - Files.lines returns a Stream<String>
		// FUNDAMENTALS streams - skip(1) skips header, forEach processes each line
		Files.lines(path)
				.skip(1)
				.forEach(line -> {
					try {
						vehicles.add(parseCsvLine(line));
					} catch (IllegalArgumentException e) {
						// Skip invalid lines with a warning
						System.err.println("Skipping invalid line: " + line + " (" + e.getMessage() + ")");
					}
				});

		return vehicles;
	}

	/**
	 * Converts a Vehicle object to a CSV line.
	 *
	 * @param v the vehicle to convert
	 * @return the CSV representation
	 */
	private String vehicleToCsvLine(Vehicle v) {
		// Base fields: TYPE,make,model,colour,fuelType,dailyRate
		String base = String.format("%s,%s,%s,%s,%s,%.2f",
				v.getVehicleType(),
				v.getMake(),
				v.getModel(),
				v.getColour(),
				v.getFuelType(),
				v.getDailyRate());

		// FUNDAMENTALS pattern matching for instanceof - extract type-specific specs
		String specs = switch (v) {
			case Car c -> String.format("%b,%b", c.isAirConditioning(), c.isNavigation());
			case Van van -> String.format("%.2f", van.getCargoCapacityKg());
			case Bike b -> String.format("%d", b.getWheelSize());
		};

		return base + "," + specs;
	}

	/**
	 * Parses a CSV line into a Vehicle object.
	 *
	 * @param line the CSV line to parse
	 * @return the parsed Vehicle
	 * @throws IllegalArgumentException if the line format is invalid
	 */
	private Vehicle parseCsvLine(String line) {
		String[] parts = line.split(",");
		if (parts.length < 7) {
			throw new IllegalArgumentException("Insufficient fields (expected at least 7)");
		}

		VehicleType type = VehicleType.valueOf(parts[0].toUpperCase().trim());
		String make = parts[1].trim();
		String model = parts[2].trim();
		String colour = parts[3].trim();
		FuelType fuelType = FuelType.valueOf(parts[4].toUpperCase().trim());
		double dailyRate = Double.parseDouble(parts[5].trim());

		// FUNDAMENTALS switch expression with pattern matching
		return switch (type) {
			case CAR -> {
				if (parts.length < 8) {
					throw new IllegalArgumentException("Car requires 8 fields");
				}
				boolean airConditioning = Boolean.parseBoolean(parts[6].trim());
				boolean navigation = Boolean.parseBoolean(parts[7].trim());
				yield new Car(make, model, colour, fuelType, dailyRate, airConditioning, navigation);
			}
			case VAN -> {
				if (parts.length < 7) {
					throw new IllegalArgumentException("Van requires 7 fields");
				}
				double cargoCapacity = Double.parseDouble(parts[6].trim());
				yield new Van(make, model, colour, fuelType, dailyRate, cargoCapacity);
			}
			case BIKE -> {
				if (parts.length < 7) {
					throw new IllegalArgumentException("Bike requires 7 fields");
				}
				int wheelSize = Integer.parseInt(parts[6].trim());
				yield new Bike(make, model, colour, fuelType, dailyRate, wheelSize);
			}
		};
	}
}
