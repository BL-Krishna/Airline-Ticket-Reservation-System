package airline.service;

import airline.enums.BookingStatus;
import airline.model.BoardingPass;
import airline.model.Booking;
import airline.model.Passenger;
import airline.repository.BookingRepository;
import airline.singleton.BookingManager;
import airline.exception.CheckInException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

public class CheckInService {

    private final BookingRepository bookingRepository;

    public CheckInService() {
        this.bookingRepository = BookingManager.getInstance().getBookingRepository();
    }

    public BoardingPass performOnlineCheckIn(String bookingId, String seatNumber, int baggageCount, boolean bypassWindowCheck) {
        Booking booking = bookingRepository.findBooking(bookingId);
        if (booking == null) {
            throw new CheckInException("Booking not found for ID " + bookingId);
        }

        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new CheckInException("Booking " + bookingId + " has been cancelled.");
        }

        LocalDateTime departure = booking.getFlight().getDepartureTime();
        LocalDateTime now = LocalDateTime.now();
        long hoursRemaining = Duration.between(now, departure).toHours();

        // 1. Check-In Window Check (24 - 3 hours before departure)
        boolean windowValid = hoursRemaining >= 3 && hoursRemaining <= 24;
        if (!windowValid && !bypassWindowCheck) {
            throw new CheckInException("Online check-in is only allowed between 24 and 3 hours before departure. Current hours remaining: " + hoursRemaining);
        }

        Passenger passenger = booking.getPassenger();

        // 2. Passport check for International Flights
        if (booking.getFlight().getRoute() != null) {
            airline.model.Airport src = booking.getFlight().getRoute().getSourceAirport();
            airline.model.Airport dest = booking.getFlight().getRoute().getDestinationAirport();
            if (src != null && dest != null && !src.getCountry().equalsIgnoreCase(dest.getCountry())) {
                // International flight
                if (passenger.getPassportNumber() == null || passenger.getPassportNumber().isBlank()) {
                    throw new CheckInException("Valid passport number is required for international flights!");
                }
            }
        }

        // 3. Baggage Allowance Check (Max 3 pieces)
        if (baggageCount > 3) {
            throw new CheckInException("Baggage exceeds maximum online check-in allowance of 3 pieces.");
        }

        // Assign the seat to the booking
        if (booking.getSeat() == null || !booking.getSeat().getSeatNumber().equalsIgnoreCase(seatNumber)) {
            airline.model.Seat s = new airline.model.Seat(seatNumber, airline.enums.SeatClass.valueOf(booking.getTravelClass().name()));
            booking.setSeat(s);
        }

        // 4. Update check-in status
        booking.setBookingStatus(BookingStatus.CHECKED_IN);
        bookingRepository.save(booking);

        // Generate Boarding Pass
        String bpId = "BP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String gate = "GATE-" + (char) ('A' + new Random().nextInt(6)) + (new Random().nextInt(15) + 1);
        LocalDateTime boardingTime = departure.minusMinutes(45);

        BoardingPass boardingPass = new BoardingPass(bpId, booking, seatNumber, gate, boardingTime, baggageCount);
        
        System.out.println("Online check-in successful for booking: " + bookingId + "!");
        return boardingPass;
    }
}
