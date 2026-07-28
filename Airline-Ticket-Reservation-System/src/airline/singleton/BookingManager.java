package airline.singleton;

import airline.enums.BookingStatus;
import airline.enums.TravelClass;
import airline.model.Booking;
import airline.model.Flight;
import airline.model.Passenger;
import airline.repository.BookingRepository;
import airline.service.BookingQueueService;
import airline.enums.BookingPriority;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BookingManager {
    private static volatile BookingManager instance;
    private final BookingRepository bookingRepository;
    private final BookingQueueService bookingQueueService;
    private final Map<String, Booking> bookingCache = new ConcurrentHashMap<>();

    private BookingManager() {
        this.bookingRepository = new BookingRepository();
        this.bookingQueueService = new BookingQueueService();
    }

    public BookingQueueService getBookingQueueService() {
        return bookingQueueService;
    }

    public static BookingManager getInstance() {
        if (instance == null) {
            synchronized (BookingManager.class) {
                if (instance == null) {
                    instance = new BookingManager();
                }
            }
        }
        return instance;
    }

    public BookingRepository getBookingRepository() {
        return bookingRepository;
    }

    public synchronized Booking bookFlight(Passenger passenger, Flight flight, TravelClass travelClass, int seats) {
        String bookingId = "BK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        double fare = 0;
        switch (travelClass) {
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
        double totalFare = fare * seats;

        Booking booking = new Booking(
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
        bookingCache.put(bookingId, booking);
        return booking;
    }

    public Booking searchBooking(String bookingId) {
        if (bookingId == null) return null;
        return bookingCache.computeIfAbsent(bookingId, id -> {
            Booking b = bookingRepository.findBooking(id);
            return b;
        });
    }

    public Collection<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public void displayBookings() {
        if (bookingRepository.count() == 0) {
            System.out.println("No Bookings Found");
            return;
        }
        bookingRepository.findAll().forEach(System.out::println);
    }

    public synchronized boolean cancelBooking(String bookingId) {
        Booking booking = searchBooking(bookingId);
        if (booking == null) {
            return false;
        }
        booking.setBookingStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
        bookingCache.put(bookingId, booking);
        return true;
    }

    public long totalBookings() {
        return bookingRepository.count();
    }

    public long totalCancelledBookings() {
        return bookingRepository.findAll().stream()
                .filter(b -> b.getBookingStatus() == BookingStatus.CANCELLED)
                .count();
    }

    public double totalRevenue() {
        return bookingRepository.findAll().stream()
                .filter(b -> b.getBookingStatus() == BookingStatus.BOOKED)
                .mapToDouble(Booking::getTotalFare)
                .sum();
    }

    public Optional<Booking> highestBooking() {
        return bookingRepository.findAll().stream()
                .max(java.util.Comparator.comparingDouble(Booking::getTotalFare));
    }

    public Optional<Booking> lowestBooking() {
        return bookingRepository.findAll().stream()
                .min(java.util.Comparator.comparingDouble(Booking::getTotalFare));
    }
}
