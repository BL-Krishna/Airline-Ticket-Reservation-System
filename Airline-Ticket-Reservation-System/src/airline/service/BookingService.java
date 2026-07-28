package airline.service;

import airline.enums.BookingStatus;
import airline.enums.TravelClass;
import airline.model.Booking;
import airline.model.Flight;
import airline.model.Passenger;
import airline.model.Seat;
import airline.repository.BookingRepository;
import airline.singleton.BookingManager;

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

        String bookingId = generateBookingId();

        double totalFare =
                calculateFare(flight, travelClass, seats);

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
                                 int seats){

        double fare = 0;

        switch (travelClass){

            case ECONOMY:
                fare = flight.getEconomyFare();
                break;

            case BUSINESS:
                fare = flight.getBusinessFare();
                break;

            case FIRST_CLASS:
                fare = flight.getFirstClassFare();
                break;

            default:
                fare = flight.getEconomyFare();
        }

        return fare * seats;
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