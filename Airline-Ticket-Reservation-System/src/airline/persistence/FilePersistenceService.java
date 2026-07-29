package airline.persistence;

import airline.model.*;
import airline.enums.*;
import airline.repository.*;
import airline.singleton.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Collection;

public class FilePersistenceService {

    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;
    private final AirportRepository airportRepository;

    public FilePersistenceService() {
        this.flightRepository = FlightManager.getInstance().getFlightRepository();
        this.bookingRepository = BookingManager.getInstance().getBookingRepository();
        this.airportRepository = FlightManager.getInstance().getAirportRepository();
    }

    public void saveFlights(String filepath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            // Write CSV header
            writer.write("flightNumber,airlineName,aircraftId,aircraftName,aircraftType,economySeats,businessSeats,firstClassSeats,srcAirportCode,destAirportCode,departureTime,arrivalTime,economyFare,businessFare,firstClassFare,status");
            writer.newLine();

            for (Flight f : flightRepository.findAll()) {
                String aircraftId = f.getAircraft() != null ? f.getAircraft().getAircraftId() : "";
                String aircraftName = f.getAircraft() != null ? f.getAircraft().getAircraftName() : "";
                String aircraftType = f.getAircraft() != null ? f.getAircraft().getAircraftType() : "";
                int totalSeats = f.getAircraft() != null ? f.getAircraft().getEconomySeats() : 0;
                int businessSeats = f.getAircraft() != null ? f.getAircraft().getBusinessSeats() : 0;
                int firstSeats = f.getAircraft() != null ? f.getAircraft().getFirstClassSeats() : 0;

                String srcCode = (f.getRoute() != null && f.getRoute().getSourceAirport() != null) ? f.getRoute().getSourceAirport().getCode() : "";
                String destCode = (f.getRoute() != null && f.getRoute().getDestinationAirport() != null) ? f.getRoute().getDestinationAirport().getCode() : "";

                String depTime = f.getDepartureTime() != null ? f.getDepartureTime().toString() : "";
                String arrTime = f.getArrivalTime() != null ? f.getArrivalTime().toString() : "";

                writer.write(String.format("%s,%s,%s,%s,%s,%d,%d,%d,%s,%s,%s,%s,%.2f,%.2f,%.2f,%s",
                        f.getFlightNumber(),
                        f.getAirlineName(),
                        aircraftId,
                        aircraftName,
                        aircraftType,
                        totalSeats,
                        businessSeats,
                        firstSeats,
                        srcCode,
                        destCode,
                        depTime,
                        arrTime,
                        f.getEconomyFare(),
                        f.getBusinessFare(),
                        f.getFirstClassFare(),
                        f.getStatus().name()
                ));
                writer.newLine();
            }
        }
    }

    public void loadFlights(String filepath) throws IOException {
        File file = new File(filepath);
        if (!file.exists()) {
            System.out.println("Flights file not found: " + filepath);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String header = reader.readLine(); // skip header
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",", -1);
                if (parts.length < 16) continue;

                String flightNumber = parts[0];
                String airlineName = parts[1];
                
                String aircraftId = parts[2];
                String aircraftName = parts[3];
                String aircraftType = parts[4];
                int totalSeats = Integer.parseInt(parts[5]);
                int businessSeats = Integer.parseInt(parts[6]);
                int firstSeats = Integer.parseInt(parts[7]);
                Aircraft aircraft = new Aircraft(aircraftId, aircraftName, aircraftType, totalSeats, businessSeats, firstSeats);

                String srcCode = parts[8];
                String destCode = parts[9];
                Airport srcAirport = airportRepository.findAirport(srcCode);
                Airport destAirport = airportRepository.findAirport(destCode);
                
                // If airport not found in repo, create stub airports so flight route still functions
                if (srcAirport == null && !srcCode.isEmpty()) {
                    srcAirport = new Airport(srcCode, srcCode + " Airport", srcCode, "Unknown", "GMT", "");
                    airportRepository.save(srcAirport);
                }
                if (destAirport == null && !destCode.isEmpty()) {
                    destAirport = new Airport(destCode, destCode + " Airport", destCode, "Unknown", "GMT", "");
                    airportRepository.save(destAirport);
                }
                Route route = new Route(srcAirport, destAirport);

                LocalDateTime depTime = parts[10].isEmpty() ? null : LocalDateTime.parse(parts[10]);
                LocalDateTime arrTime = parts[11].isEmpty() ? null : LocalDateTime.parse(parts[11]);
                double econFare = Double.parseDouble(parts[12]);
                double busFare = Double.parseDouble(parts[13]);
                double firstFare = Double.parseDouble(parts[14]);
                FlightStatus status = FlightStatus.valueOf(parts[15]);

                Flight flight = new Flight(flightNumber, airlineName, aircraft, route, depTime, arrTime, econFare, busFare, firstFare, status);
                flightRepository.save(flight);
            }
        }
    }

    public void saveBookings(String filepath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            // Write CSV header
            writer.write("bookingId,passengerId,passengerName,passengerEmail,passengerPhone,passengerAge,flightNumber,travelClass,seats,totalFare,bookingTime,status");
            writer.newLine();

            for (Booking b : bookingRepository.findAll()) {
                String passId = b.getPassenger() != null ? b.getPassenger().getUserId() : "";
                String passName = b.getPassenger() != null ? b.getPassenger().getFullName() : "";
                String passEmail = b.getPassenger() != null ? b.getPassenger().getEmail() : "";
                String passPhone = b.getPassenger() != null ? b.getPassenger().getPhoneNumber() : "";
                int passAge = b.getPassenger() != null ? b.getPassenger().getAge() : 0;

                String flightNum = b.getFlight() != null ? b.getFlight().getFlightNumber() : "";
                String travelClass = b.getTravelClass() != null ? b.getTravelClass().name() : "";
                String bookTime = b.getBookingTime() != null ? b.getBookingTime().toString() : "";

                writer.write(String.format("%s,%s,%s,%s,%s,%d,%s,%s,%d,%.2f,%s,%s",
                        b.getBookingId(),
                        passId,
                        passName,
                        passEmail,
                        passPhone,
                        passAge,
                        flightNum,
                        travelClass,
                        b.getNumberOfSeats(),
                        b.getTotalFare(),
                        bookTime,
                        b.getBookingStatus().name()
                ));
                writer.newLine();
            }
        }
    }

    public void loadBookings(String filepath) throws IOException {
        File file = new File(filepath);
        if (!file.exists()) {
            System.out.println("Bookings file not found: " + filepath);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String header = reader.readLine(); // skip header
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",", -1);
                if (parts.length < 12) continue;

                String bookingId = parts[0];
                String passId = parts[1];
                String passName = parts[2];
                String passEmail = parts[3];
                String passPhone = parts[4];
                int passAge = Integer.parseInt(parts[5]);
                Passenger passenger = new Passenger(passId, passName, passEmail, passPhone, "pass123");
                passenger.setAge(passAge);

                String flightNum = parts[6];
                Flight flight = flightRepository.findByFlightNumber(flightNum);

                TravelClass travelClass = TravelClass.valueOf(parts[7]);
                int seats = Integer.parseInt(parts[8]);
                double totalFare = Double.parseDouble(parts[9]);
                LocalDateTime bookTime = parts[10].isEmpty() ? null : LocalDateTime.parse(parts[10]);
                BookingStatus status = BookingStatus.valueOf(parts[11]);

                Booking booking = new Booking(bookingId, passenger, flight, travelClass, seats, totalFare, bookTime, status);
                bookingRepository.save(booking);
            }
        }
    }
}
