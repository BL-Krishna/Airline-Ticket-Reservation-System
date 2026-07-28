package airline.service;

import airline.enums.BookingStatus;
import airline.model.PaymentStatus;
import airline.model.Booking;
import airline.model.Flight;
import airline.model.Passenger;
import airline.model.Payment;
import airline.repository.BookingRepository;
import airline.repository.FlightRepository;
import airline.repository.PaymentRepository;
import airline.singleton.BookingManager;
import airline.singleton.FlightManager;
import airline.singleton.PaymentManager;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ReportingService {

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;
    private final PaymentRepository paymentRepository;

    public ReportingService() {
        this.bookingRepository = BookingManager.getInstance().getBookingRepository();
        this.flightRepository = FlightManager.getInstance().getFlightRepository();
        this.paymentRepository = PaymentManager.getInstance().getPaymentRepository();
    }

    // ===========================================
    // 1. Booking Reports
    // ===========================================

    // Daily Booking Report
    public List<Booking> getDailyBookingReport(LocalDate date) {
        if (date == null) return new ArrayList<>();
        return bookingRepository.findAll().stream()
                .filter(b -> b.getBookingTime().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }

    // Revenue by Date Range
    public double getRevenueByDateRange(LocalDate start, LocalDate end) {
        if (start == null || end == null) return 0.0;
        return bookingRepository.findAll().stream()
                .filter(b -> b.getBookingStatus() == BookingStatus.BOOKED)
                .filter(b -> {
                    LocalDate bDate = b.getBookingTime().toLocalDate();
                    return !bDate.isBefore(start) && !bDate.isAfter(end);
                })
                .mapToDouble(Booking::getTotalFare)
                .sum();
    }

    // Booking Count Grouped by Route
    public Map<String, Long> getBookingsByRoute() {
        return bookingRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        b -> b.getFlight().getRoute().toString(),
                        Collectors.counting()
                ));
    }

    // Booking Count Grouped by Airline
    public Map<String, Long> getBookingsByAirline() {
        return bookingRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        b -> b.getFlight().getAirlineName(),
                        Collectors.counting()
                ));
    }

    // Average Booking Value
    public double getAverageBookingValue() {
        return bookingRepository.findAll().stream()
                .mapToDouble(Booking::getTotalFare)
                .average()
                .orElse(0.0);
    }

    // Cancellation Rate
    public double getCancellationRate() {
        long total = bookingRepository.count();
        if (total == 0) return 0.0;
        long cancelled = bookingRepository.findAll().stream()
                .filter(b -> b.getBookingStatus() == BookingStatus.CANCELLED)
                .count();
        return (double) cancelled / total;
    }

    // Payment Success Rate
    public double getPaymentSuccessRate() {
        long total = paymentRepository.count();
        if (total == 0) return 0.0;
        long success = paymentRepository.findAll().stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.SUCCESS || p.getPaymentStatus() == PaymentStatus.REFUNDED)
                .count();
        return (double) success / total;
    }

    // ===========================================
    // 2. Flight Performance Reports
    // ===========================================

    // Occupancy Rate per Flight
    public Map<String, Double> getFlightOccupancyRates() {
        Map<String, Integer> bookedSeats = bookingRepository.findAll().stream()
                .filter(b -> b.getBookingStatus() == BookingStatus.BOOKED)
                .collect(Collectors.groupingBy(
                        b -> b.getFlight().getFlightNumber(),
                        Collectors.summingInt(Booking::getNumberOfSeats)
                ));

        Map<String, Double> occupancyRates = new HashMap<>();
        for (Flight flight : flightRepository.findAll()) {
            int capacity = flight.getAircraft().getEconomySeats() + 
                           flight.getAircraft().getBusinessSeats() + 
                           flight.getAircraft().getFirstClassSeats();
            int booked = bookedSeats.getOrDefault(flight.getFlightNumber(), 0);
            double rate = capacity > 0 ? (double) booked / capacity : 0.0;
            occupancyRates.put(flight.getFlightNumber(), rate);
        }
        return occupancyRates;
    }

    // Track Revenue per Flight
    public Map<String, Double> getRevenuePerFlight() {
        return bookingRepository.findAll().stream()
                .filter(b -> b.getBookingStatus() == BookingStatus.BOOKED)
                .collect(Collectors.groupingBy(
                        b -> b.getFlight().getFlightNumber(),
                        Collectors.summingDouble(Booking::getTotalFare)
                ));
    }

    // Compare Airline Performance
    public Map<String, Double> getRevenueByAirline() {
        return bookingRepository.findAll().stream()
                .filter(b -> b.getBookingStatus() == BookingStatus.BOOKED)
                .collect(Collectors.groupingBy(
                        b -> b.getFlight().getAirlineName(),
                        Collectors.summingDouble(Booking::getTotalFare)
                ));
    }

    // ===========================================
    // 3. Passenger Analytics
    // ===========================================

    // Group Passengers by Meal Preference
    public Map<String, Long> getPassengersByMealPreference() {
        return bookingRepository.findAll().stream()
                .map(Booking::getPassenger)
                .filter(p -> p.getMealPreference() != null)
                .collect(Collectors.groupingBy(
                        Passenger::getMealPreference,
                        Collectors.counting()
                ));
    }

    // Track Booking Count per Passenger (for repeat customers)
    public Map<String, Long> getBookingCountPerPassenger() {
        return bookingRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        b -> b.getPassenger().getName(),
                        Collectors.counting()
                ));
    }

    // Customer Lifetime Value (CLV)
    public Map<String, Double> getCustomerLifetimeValue() {
        return bookingRepository.findAll().stream()
                .filter(b -> b.getBookingStatus() == BookingStatus.BOOKED)
                .collect(Collectors.groupingBy(
                        b -> b.getPassenger().getName(),
                        Collectors.summingDouble(Booking::getTotalFare)
                ));
    }

    // Detailed Report Display
    public void generateAnalyticsReport() {
        System.out.println("\n=======================================================");
        System.out.println("               AIRLINE REPORT & ANALYTICS");
        System.out.println("=======================================================");
        System.out.println("Average Booking Value : ₹" + getAverageBookingValue());
        System.out.println("Cancellation Rate     : " + (getCancellationRate() * 100) + "%");
        
        System.out.println("\n[Booking Count Grouped By Route]:");
        getBookingsByRoute().forEach((route, count) -> System.out.println("  " + route + " : " + count + " bookings"));

        System.out.println("\n[Booking Count Grouped By Airline]:");
        getBookingsByAirline().forEach((airline, count) -> System.out.println("  " + airline + " : " + count + " bookings"));

        System.out.println("\n[Revenue Grouped By Airline]:");
        getRevenueByAirline().forEach((airline, rev) -> System.out.println("  " + airline + " : ₹" + rev));

        System.out.println("\n[Flight Occupancy Rates]:");
        getFlightOccupancyRates().forEach((flightNo, rate) -> System.out.println("  Flight " + flightNo + " : " + (rate * 100) + "% occupancy"));

        System.out.println("\n[Passenger Preference - Meal Preference Distribution]:");
        getPassengersByMealPreference().forEach((meal, count) -> System.out.println("  " + meal + " : " + count + " passengers"));

        System.out.println("\n[Customer Lifetime Value (CLV)]:");
        getCustomerLifetimeValue().forEach((name, clv) -> System.out.println("  " + name + " : Total spend ₹" + clv));
        System.out.println("=======================================================");
    }
}
