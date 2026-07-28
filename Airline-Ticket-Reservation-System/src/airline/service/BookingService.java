package airline.service;

import airline.enums.BookingStatus;
import airline.enums.TravelClass;
import airline.model.Booking;
import airline.model.Flight;
import airline.model.Passenger;
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

}