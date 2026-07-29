package airline.service;

import airline.enums.BookingStatus;
import airline.enums.TravelClass;
import airline.model.Booking;
import airline.model.Flight;
import airline.model.Passenger;
import airline.model.Seat;
import airline.repository.BookingRepository;
import airline.singleton.BookingManager;
import airline.exception.DuplicateBookingException;
import airline.exception.SeatLimitException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;

public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService() {
        this.bookingRepository = BookingManager.getInstance().getBookingRepository();
    }

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking bookFlight(Passenger passenger,
                              Flight flight,
                              TravelClass travelClass,
                              int seats) {

        // 1. Max Seat Limit check
        if (seats <= 0 || seats > 6) {
            throw new SeatLimitException("Cannot book " + seats + " seats. Limit is between 1 and 6 seats per transaction.");
        }

        // 2. Duplicate Booking check
        if (passenger != null && passenger.getEmail() != null && flight != null) {
            boolean duplicateExists = bookingRepository.findAll().stream()
                    .anyMatch(b -> b.getPassenger() != null 
                            && passenger.getEmail().equalsIgnoreCase(b.getPassenger().getEmail())
                            && flight.getFlightNumber().equalsIgnoreCase(b.getFlight().getFlightNumber())
                            && (b.getBookingStatus() == BookingStatus.BOOKED || b.getBookingStatus() == BookingStatus.CHECKED_IN));
            if (duplicateExists) {
                throw new DuplicateBookingException("Passenger " + passenger.getFullName() + " already has an active booking for flight " + flight.getFlightNumber() + ".");
            }
        }

        String bookingId = generateBookingId();

        double totalFare =
                calculateFare(flight, travelClass, seats, passenger);

        Booking booking =
                new Booking(
                        bookingId,
                        passenger,
                        flight,
                        travelClass,
                        seats,
                        totalFare,
                        LocalDateTime.now(),
                        BookingStatus.BOOKED
                );

        bookingRepository.save(booking);

        return booking;
    }

    private String generateBookingId() {

        return "BK-"
                + UUID.randomUUID()
                .toString()
                .substring(0,8)
                .toUpperCase();

    }

    private double calculateFare(Flight flight,
                                 TravelClass travelClass,
                                 int seats,
                                 Passenger passenger){

        double baseFare = 0;

        switch (travelClass){

            case ECONOMY:
                baseFare = flight.getEconomyFare();
                break;

            case BUSINESS:
                baseFare = flight.getBusinessFare();
                break;

            case FIRST_CLASS:
                baseFare = flight.getFirstClassFare();
                break;

            default:
                baseFare = flight.getEconomyFare();
        }

        double totalFare = baseFare * seats;

        // Apply proximity pricing: surcharge if within 7 days, discount if >= 30 days
        if (flight != null && flight.getDepartureTime() != null) {
            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            long daysToDeparture = java.time.Duration.between(now, flight.getDepartureTime()).toDays();
            if (daysToDeparture >= 0 && daysToDeparture <= 7) {
                totalFare *= 1.20; // 20% surcharge
                System.out.println("[Business Rule] Booking within 7 days (" + daysToDeparture + " days remaining). 20% surcharge applied.");
            } else if (daysToDeparture >= 30) {
                totalFare *= 0.90; // 10% discount
                System.out.println("[Business Rule] Booking more than 30 days in advance (" + daysToDeparture + " days remaining). 10% discount applied.");
            }
        }

        // Apply age-based discount
        if (passenger != null && passenger.getAge() > 0) {
            if (passenger.getAge() < 12) {
                totalFare *= 0.85; // 15% discount for child
                System.out.println("[Business Rule] Child discount (Age: " + passenger.getAge() + "). 15% discount applied.");
            } else if (passenger.getAge() > 60) {
                totalFare *= 0.90; // 10% discount for senior
                System.out.println("[Business Rule] Senior citizen discount (Age: " + passenger.getAge() + "). 10% discount applied.");
            }
        }

        return totalFare;
    }

    public Booking searchBooking(String bookingId){

        return bookingRepository.findBooking(bookingId);

    }

    public Collection<Booking> getAllBookings(){

        return bookingRepository.findAll();

    }

    public void displayBookings(){

        if(bookingRepository.count()==0){

            System.out.println("No Bookings Found");
            return;

        }

        bookingRepository
                .findAll()
                .forEach(System.out::println);

    }

    public boolean cancelBooking(String bookingId){

        Booking booking =
                bookingRepository.findBooking(bookingId);

        if(booking==null){

            return false;

        }

        booking.setBookingStatus(
                BookingStatus.CANCELLED
        );

        return true;

    }

    public long totalBookings(){

        return bookingRepository
                .findAll()
                .stream()
                .count();

    }

    public long totalCancelledBookings(){

        return bookingRepository
                .findAll()
                .stream()
                .filter(b->

                        b.getBookingStatus()==
                                BookingStatus.CANCELLED

                )
                .count();

    }

    public double totalRevenue(){

        return bookingRepository
                .findAll()
                .stream()
                .filter(b->

                        b.getBookingStatus()==
                                BookingStatus.BOOKED

                )
                .mapToDouble(
                        Booking::getTotalFare
                )
                .sum();

    }

    public Optional<Booking> highestBooking(){

        return bookingRepository
                .findAll()
                .stream()
                .max(
                        Comparator.comparingDouble(
                                Booking::getTotalFare
                        )
                );

    }

    public Optional<Booking> lowestBooking(){

        return bookingRepository
                .findAll()
                .stream()
                .min(
                        Comparator.comparingDouble(
                                Booking::getTotalFare
                        )
                );

    }

    // ===========================================
    // UC14 - Booking Modifications
    // ===========================================

    public boolean modifyFlight(String bookingId, Flight newFlight) {
        Booking booking = searchBooking(bookingId);
        if (booking == null || newFlight == null) {
            return false;
        }

        // Release old seat if assigned
        if (booking.getSeat() != null) {
            booking.getSeat().setSeatStatus(airline.enums.SeatStatus.AVAILABLE);
            booking.setSeat(null);
        }

        double oldFare = booking.getTotalFare();
        double newUnitFare = 0;
        switch (booking.getTravelClass()) {
            case ECONOMY:
                newUnitFare = newFlight.getEconomyFare();
                break;
            case BUSINESS:
                newUnitFare = newFlight.getBusinessFare();
                break;
            case FIRST_CLASS:
                newUnitFare = newFlight.getFirstClassFare();
                break;
        }
        double newFare = newUnitFare * booking.getNumberOfSeats();
        double fareDiff = newFare - oldFare;
        double modificationFee = 500.0;

        booking.setFlight(newFlight);
        booking.setTotalFare(oldFare + fareDiff + modificationFee);
        booking.setBookingTime(LocalDateTime.now());
        bookingRepository.save(booking);

        System.out.println("Flight changed for booking " + bookingId + " to flight " + newFlight.getFlightNumber() + ". Fare difference: ₹" + fareDiff + ", Fee: ₹" + modificationFee);
        return true;
    }

    public boolean modifyPassengerDetails(String bookingId, String newName, String newEmail, String newPhone, String mealPreference, String specialAssistance) {
        Booking booking = searchBooking(bookingId);
        if (booking == null) {
            return false;
        }

        Passenger passenger = booking.getPassenger();
        if (newName != null && !newName.isBlank()) {
            passenger.setFullName(newName);
        }
        if (newEmail != null && !newEmail.isBlank()) {
            passenger.setEmail(newEmail);
        }
        if (newPhone != null && !newPhone.isBlank()) {
            passenger.setPhoneNumber(newPhone);
        }
        if (mealPreference != null) {
            passenger.setMealPreference(mealPreference);
        }
        if (specialAssistance != null) {
            passenger.setSpecialAssistance(specialAssistance);
        }

        bookingRepository.save(booking);
        System.out.println("Passenger details modified for booking " + bookingId);
        return true;
    }

    public boolean changeSeat(String bookingId, Seat newSeat) {
        Booking booking = searchBooking(bookingId);
        if (booking == null || newSeat == null) {
            return false;
        }

        if (newSeat.getSeatStatus() == airline.enums.SeatStatus.RESERVED) {
            System.out.println("Requested seat " + newSeat.getSeatNumber() + " is already reserved.");
            return false;
        }

        // Release old seat
        if (booking.getSeat() != null) {
            booking.getSeat().setSeatStatus(airline.enums.SeatStatus.AVAILABLE);
        }

        // Reserve new seat
        newSeat.setSeatStatus(airline.enums.SeatStatus.RESERVED);
        booking.setSeat(newSeat);

        // Calculate seat upgrade charge (e.g. premium seats cost extra)
        double upgradeCharge = 0;
        if (newSeat.getSeatClass() == airline.enums.SeatClass.FIRST_CLASS) {
            upgradeCharge = 2000.0;
        } else if (newSeat.getSeatClass() == airline.enums.SeatClass.BUSINESS) {
            upgradeCharge = 1000.0;
        }

        booking.setTotalFare(booking.getTotalFare() + upgradeCharge);
        bookingRepository.save(booking);

        System.out.println("Seat changed for booking " + bookingId + " to seat " + newSeat.getSeatNumber() + ". Upgrade charge: ₹" + upgradeCharge);
        return true;
    }

}