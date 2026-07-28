package airline.app;

import airline.enums.FlightStatus;
import airline.enums.TravelClass;
import airline.model.Aircraft;
import airline.model.Booking;
import airline.model.Passenger;
import airline.model.Route;
import airline.repository.BookingRepository;
import airline.repository.FlightRepository;
import airline.service.BookingService;
import airline.service.FlightSearchService;
import airline.service.FlightService;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AirlineReservationApplication {

    public static void main(String[] args) {

        // ===========================
        // REPOSITORIES
        // ===========================

        FlightRepository flightRepository =
                new FlightRepository();

        BookingRepository bookingRepository =
                new BookingRepository();

        // ===========================
        // SERVICES
        // ===========================

        FlightService flightService =
                new FlightService(flightRepository);

        FlightSearchService searchService =
                new FlightSearchService(flightRepository);

        BookingService bookingService =
                new BookingService(bookingRepository);

        // ===========================
        // AIRCRAFTS
        // ===========================

        Aircraft aircraft1 =
                new Aircraft(
                        "A101",
                        "Airbus A320",
                        "Narrow Body",
                        180,
                        24,
                        12
                );

        Aircraft aircraft2 =
                new Aircraft(
                        "A102",
                        "Boeing 737",
                        "Narrow Body",
                        170,
                        18,
                        8
                );

        // ===========================
        // ROUTES
        // ===========================

        Route route1 =
                new Route(
                        "Hyderabad",
                        "Delhi"
                );

        Route route2 =
                new Route(
                        "Chennai",
                        "Mumbai"
                );

        Route route3 =
                new Route(
                        "Bangalore",
                        "Kolkata"
                );

        // ===========================
        // ADD FLIGHTS
        // ===========================

        flightService.addFlight(
                "AI101",
                "Air India",
                aircraft1,
                route1,
                LocalDateTime.of(2026, 8, 10, 8, 0),
                LocalDateTime.of(2026, 8, 10, 10, 30),
                4500,
                8500,
                14500
        );

        flightService.addFlight(
                "6E205",
                "IndiGo",
                aircraft2,
                route2,
                LocalDateTime.of(2026, 8, 11, 13, 45),
                LocalDateTime.of(2026, 8, 11, 15, 55),
                3900,
                7200,
                12000
        );

        flightService.addFlight(
                "UK777",
                "Vistara",
                aircraft1,
                route3,
                LocalDateTime.of(2026, 8, 12, 6, 15),
                LocalDateTime.of(2026, 8, 12, 9, 20),
                5200,
                9800,
                16500
        );

        System.out.println("\n========== ALL FLIGHTS ==========");

        flightService.displayFlights();

        // ===========================
        // UC5 - SEARCH OPERATIONS
        // ===========================

        System.out.println("\n========== SEARCH BY SOURCE ==========");

        searchService
                .searchBySource("Hyderabad")
                .forEach(System.out::println);

        System.out.println("\n========== SEARCH BY DESTINATION ==========");

        searchService
                .searchByDestination("Mumbai")
                .forEach(System.out::println);

        System.out.println("\n========== SEARCH BY ROUTE ==========");

        searchService
                .searchByRoute(
                        "Hyderabad",
                        "Delhi")
                .forEach(System.out::println);

        System.out.println("\n========== SEARCH BY AIRLINE ==========");

        searchService
                .searchByAirline("Air India")
                .forEach(System.out::println);

        System.out.println("\n========== SEARCH BY DATE ==========");

        searchService
                .searchByDate(LocalDate.of(2026,8,10))
                .forEach(System.out::println);

        System.out.println("\n========== CHEAPEST FLIGHT ==========");

        System.out.println(
                searchService.getCheapestFlight()
        );

        System.out.println("\n========== SORT BY FARE ==========");

        searchService
                .sortByFare()
                .forEach(System.out::println);

        System.out.println("\n========== SORT BY DEPARTURE ==========");

        searchService
                .sortByDepartureTime()
                .forEach(System.out::println);

        // ===========================
        // UC6 BOOKING
        // ===========================

        Passenger passenger =
                new Passenger(
                        "P101",
                        "Krrish CH",
                        "krrish@gmail.com",
                        "9876543210",
                        "Hyderabad"
                );

        Booking booking =
                bookingService.bookFlight(
                        passenger,
                        flightService.searchFlight("AI101"),
                        TravelClass.ECONOMY,
                        2
                );

        System.out.println("\n========== BOOKING CREATED ==========");

        System.out.println(booking);

        System.out.println("\n========== ALL BOOKINGS ==========");

        bookingService.displayBookings();

        System.out.println("\n========== SEARCH BOOKING ==========");

        System.out.println(
                bookingService.searchBooking(
                        booking.getBookingId()
                )
        );

        System.out.println("\n========== BOOKING REPORT ==========");

        System.out.println(
                "Total Bookings : "
                        + bookingService.totalBookings()
        );

        System.out.println(
                "Revenue : "
                        + bookingService.totalRevenue()
        );

        bookingService
                .highestBooking()
                .ifPresent(b -> {

                    System.out.println("\nHighest Booking");

                    System.out.println(b);

                });

        bookingService
                .lowestBooking()
                .ifPresent(b -> {

                    System.out.println("\nLowest Booking");

                    System.out.println(b);

                });

        System.out.println("\n========== CANCEL BOOKING ==========");

        bookingService.cancelBooking(
                booking.getBookingId()
        );

        System.out.println(
                bookingService.searchBooking(
                        booking.getBookingId()
                )
        );

        System.out.println();

        System.out.println(
                "Cancelled Bookings : "
                        + bookingService.totalCancelledBookings()
        );

        // ===========================
        // EXISTING UC4 OPERATIONS
        // ===========================

        System.out.println("\n========== UPDATE STATUS ==========");

        flightService.updateFlightStatus(
                "AI101",
                FlightStatus.DELAYED
        );

        System.out.println(
                flightService.searchFlight("AI101")
        );

        System.out.println("\n========== UPDATE FARE ==========");

        flightService.updateEconomyFare(
                "AI101",
                5000
        );

        System.out.println(
                flightService.searchFlight("AI101")
        );

        System.out.println("\n========== DELETE FLIGHT ==========");

        flightService.deleteFlight("UK777");

        flightService.displayFlights();
    }
}