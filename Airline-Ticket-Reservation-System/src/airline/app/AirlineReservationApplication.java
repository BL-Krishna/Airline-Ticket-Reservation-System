package airline.app;

import airline.enums.FlightStatus;
import airline.enums.TravelClass;
import airline.model.Aircraft;
import airline.model.Booking;
import airline.model.Passenger;
import airline.model.Route;
import airline.model.Flight;
import airline.repository.BookingRepository;
import airline.repository.FlightRepository;
import airline.service.BookingService;
import airline.service.FlightSearchService;
import airline.service.FlightService;
import airline.singleton.BookingManager;
import airline.singleton.FlightManager;
import airline.singleton.PaymentManager;
import airline.model.Airport;
import airline.service.AirportService;
import airline.enums.BookingPriority;
import airline.service.BookingQueueService;
import airline.model.Seat;
import airline.service.SeatService;
import airline.service.ReportingService;
import airline.model.BoardingPass;
import airline.service.CheckInService;
import airline.dto.FlightSearchRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AirlineReservationApplication {

    public static void main(String[] args) {

        // ===========================
        // UC11 - SINGLETON MANAGERS VERIFICATION
        // ===========================
        System.out.println("\n========== UC11 - SINGLETON MANAGERS VERIFICATION ==========");
        FlightManager flightManager1 = FlightManager.getInstance();
        FlightManager flightManager2 = FlightManager.getInstance();
        System.out.println("FlightManager Singleton check: " + (flightManager1 == flightManager2));

        BookingManager bookingManager1 = BookingManager.getInstance();
        BookingManager bookingManager2 = BookingManager.getInstance();
        System.out.println("BookingManager Singleton check: " + (bookingManager1 == bookingManager2));

        PaymentManager paymentManager1 = PaymentManager.getInstance();
        PaymentManager paymentManager2 = PaymentManager.getInstance();
        System.out.println("PaymentManager Singleton check: " + (paymentManager1 == paymentManager2));
        System.out.println("=============================================================");

        // ===========================
        // REPOSITORIES (Using Singletons)
        // ===========================

        FlightRepository flightRepository =
                FlightManager.getInstance().getFlightRepository();

        BookingRepository bookingRepository =
                BookingManager.getInstance().getBookingRepository();

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
        // UC12 - AIRPORT SETUP & SERVICE TEST
        // ===========================
        System.out.println("\n========== UC12 - AIRPORT MANAGEMENT DEMO ==========");
        AirportService airportService = new AirportService();

        // Add airports
        Airport hyd = airportService.addAirport("HYD", "Rajiv Gandhi International Airport", "Hyderabad", "India", "GMT+5:30", "+91 40 6654 6390");
        Airport del = airportService.addAirport("DEL", "Indira Gandhi International Airport", "Delhi", "India", "GMT+5:30", "+91 11 4719 7000");
        Airport bom = airportService.addAirport("BOM", "Chhatrapati Shivaji Maharaj International Airport", "Mumbai", "India", "GMT+5:30", "+91 22 6685 1010");
        Airport maa = airportService.addAirport("MAA", "Chennai International Airport", "Chennai", "India", "GMT+5:30", "+91 44 2256 0551");
        Airport blr = airportService.addAirport("BLR", "Kempegowda International Airport", "Bangalore", "India", "GMT+5:30", "+91 80 6678 2425");
        Airport ccu = airportService.addAirport("CCU", "Netaji Subhash Chandra Bose International Airport", "Kolkata", "India", "GMT+5:30", "+91 33 2511 8036");

        // Add facilities & terminals
        airportService.updateFacilities("HYD", java.util.List.of("WiFi", "Lounge", "Duty-Free", "Wheelchair-Accessible"));
        airportService.updateTerminals("HYD", java.util.List.of("T1", "T2", "Cargo"));

        // Display airports
        airportService.displayAirports();

        // Search & Retrieve
        System.out.println("Search by IATA code 'HYD':");
        System.out.println(airportService.searchByCode("HYD"));

        System.out.println("\nSearch by city 'Delhi':");
        airportService.searchByCity("Delhi").forEach(System.out::println);

        System.out.println("\nAuto-suggest for query 'international':");
        airportService.getAutoSuggestions("international").stream().limit(2).forEach(System.out::println);
        System.out.println("=====================================================");

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
                        hyd,
                        del
                );

        Route route2 =
                new Route(
                        maa,
                        bom
                );

        Route route3 =
                new Route(
                        blr,
                        ccu
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

        // ===========================
        // UC13 - PRIORITY BOOKING PROCESS TEST
        // ===========================
        System.out.println("\n========== UC13 - PRIORITY BOOKING QUEUE DEMO ==========");
        BookingQueueService queueService = BookingManager.getInstance().getBookingQueueService();
        Passenger testPassenger = new Passenger("P999", "Queue Tester", "tester@gmail.com", "9000000009", "Hyderabad");
        Passenger testPassenger2 = new Passenger("P999_2", "Queue Tester 2", "tester2@gmail.com", "9000000009", "Hyderabad");
        Passenger testPassenger3 = new Passenger("P999_3", "Queue Tester 3", "tester3@gmail.com", "9000000009", "Hyderabad");
        Passenger testPassenger4 = new Passenger("P999_4", "Queue Tester 4", "tester4@gmail.com", "9000000009", "Hyderabad");
        Flight demoFlight = flightService.searchFlight("AI101");
        
        // Create booking requests
        Booking booking1 = bookingService.bookFlight(testPassenger, demoFlight, TravelClass.ECONOMY, 1);
        Booking booking2 = bookingService.bookFlight(testPassenger2, demoFlight, TravelClass.BUSINESS, 2);
        Booking booking3 = bookingService.bookFlight(testPassenger3, demoFlight, TravelClass.FIRST_CLASS, 1);
        Booking booking4 = bookingService.bookFlight(testPassenger4, demoFlight, TravelClass.ECONOMY, 3);

        // Queue requests with different priorities
        queueService.addRequest(booking1, BookingPriority.REGULAR);
        queueService.addRequest(booking2, BookingPriority.EXPRESS); // Express (prioritized)
        queueService.addRequest(booking3, BookingPriority.EXPRESS); // Express (FIFO after booking2)
        queueService.addRequest(booking4, BookingPriority.REGULAR);

        // Display current queue sorting
        queueService.displayQueue();

        // Process queue in priority order
        queueService.processQueue();
        System.out.println("=========================================================");

        // ===========================
        // UC14 - BOOKING MODIFICATION TEST
        // ===========================
        System.out.println("\n========== UC14 - BOOKING MODIFICATION DEMO ==========");
        System.out.println("Original Booking1 details:\n" + booking1);

        // 1. Modify Flight
        Flight alternativeFlight = flightService.searchFlight("6E205");
        System.out.println("\n--- 1. Modifying Flight to '6E205' ---");
        bookingService.modifyFlight(booking1.getBookingId(), alternativeFlight);
        System.out.println("Booking1 details after flight modification:\n" + booking1);

        // 2. Modify Passenger Details (Preferences/Details)
        System.out.println("\n--- 2. Modifying Passenger Preferences (Meal: Veg, Assistance: Wheelchair) ---");
        bookingService.modifyPassengerDetails(booking1.getBookingId(), "Krrish CH corrected", "krrish.corrected@gmail.com", "9999988888", "Veg Meal", "Wheelchair");
        System.out.println("Booking1 details after passenger update:\n" + booking1);
        System.out.println("Passenger Preferred Meal: " + booking1.getPassenger().getMealPreference());
        System.out.println("Passenger Special Assistance: " + booking1.getPassenger().getSpecialAssistance());

        // 3. Change Seat
        System.out.println("\n--- 3. Changing Seat to Premium Seat '3A' (Business Class Seat) ---");
        SeatService applicationSeatService = new SeatService();
        Seat newSeat = applicationSeatService.searchSeat("3A");
        bookingService.changeSeat(booking1.getBookingId(), newSeat);
        System.out.println("Booking1 details after seat modification:\n" + booking1);
        System.out.println("=======================================================");

        // ===========================
        // UC15 - REPORTING & ANALYTICS TEST
        // ===========================
        ReportingService reportingService = new ReportingService();
        reportingService.generateAnalyticsReport();

        // ===========================
        // UC16 - ONLINE CHECK-IN TEST
        // ===========================
        System.out.println("\n========== UC16 - ONLINE CHECK-IN DEMO ==========");
        CheckInService checkInService = new CheckInService();

        // Let's create an international booking to test passport validation.
        Airport lhr = airportService.addAirport("LHR", "Heathrow Airport", "London", "United Kingdom", "GMT+0", "+44 20 8745 7899");
        Airport hydAirport = airportService.searchByCode("HYD");
        Route internationalRoute = new Route(hydAirport, lhr);
        Aircraft intAircraft = new Aircraft("A888", "Boeing 777", "Wide", 200, 20, 10);
        Flight intFlight = flightService.addFlight("SQ408", "Singapore Air", intAircraft, internationalRoute, 
            LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(12), 15000, 30000, 50000);
        
        Passenger intPassenger = new Passenger("P888", "Int Traveler", "int@travel.com", "9998887776", "London");
        Booking intBooking = bookingService.bookFlight(intPassenger, intFlight, TravelClass.BUSINESS, 1);

        System.out.println("\n--- 1. Check-In with Passport Missing (International Flight) ---");
        try {
            BoardingPass bpFail = checkInService.performOnlineCheckIn(intBooking.getBookingId(), "12B", 1, true);
        } catch (airline.exception.CheckInException e) {
            System.out.println("Caught Expected Exception: " + e.getMessage());
        }

        System.out.println("\n--- 2. Add Passport and Retry Check-In ---");
        intPassenger.setPassportNumber("L87654321");
        BoardingPass bpSuccess = checkInService.performOnlineCheckIn(intBooking.getBookingId(), "12B", 1, true);
        if (bpSuccess != null) {
            System.out.println("Generated Boarding Pass details:\n" + bpSuccess);
        }

        System.out.println("\n--- 3. Check-In with Excess Baggage (4 bags, max 3) ---");
        Passenger baggagePassenger = new Passenger("P999_B", "Baggage Tester", "baggage@gmail.com", "9000000009", "Hyderabad");
        Booking bookingForBaggageTest = bookingService.bookFlight(baggagePassenger, demoFlight, TravelClass.ECONOMY, 1);
        try {
            BoardingPass bpBaggageFail = checkInService.performOnlineCheckIn(bookingForBaggageTest.getBookingId(), "18A", 4, true);
        } catch (airline.exception.CheckInException e) {
            System.out.println("Caught Expected Exception: " + e.getMessage());
        }

        System.out.println("\n--- 4. Retry Check-In with Allowed Baggage (2 bags) ---");
        BoardingPass bpBaggageSuccess = checkInService.performOnlineCheckIn(bookingForBaggageTest.getBookingId(), "18A", 2, true);
        if (bpBaggageSuccess != null) {
            System.out.println("Generated Boarding Pass details:\n" + bpBaggageSuccess);
        }
        System.out.println("=================================================");

        // ===========================
        // UC17 - SMART SEARCH DEMO
        // ===========================
        System.out.println("\n========== UC17 - SMART SEARCH DEMO ==========");

        // 1. Search for Flights from HYD to Delhi with maximum fare 10000
        System.out.println("\n--- 1. Search from HYD (Code/City) to DEL (Delhi) with Max Fare 10000 ---");
        FlightSearchRequest request1 = new FlightSearchRequest();
        request1.setSource("HYD");
        request1.setDestination("Delhi");
        request1.setMaximumFare(10000);
        java.util.List<Flight> results1 = searchService.smartSearch(request1);
        System.out.println("Found " + results1.size() + " flights:");
        results1.forEach(System.out::println);

        // 2. Search for IndiGo flights with budget under 4000
        System.out.println("\n--- 2. Search for IndiGo flights with Max Fare 4000 ---");
        FlightSearchRequest request2 = new FlightSearchRequest();
        request2.setAirline("indigo");
        request2.setMaximumFare(4000);
        java.util.List<Flight> results2 = searchService.smartSearch(request2);
        System.out.println("Found " + results2.size() + " flights:");
        results2.forEach(System.out::println);

        // 3. Search with multiple criteria: Source "Chennai", date "2026-08-11"
        System.out.println("\n--- 3. Search with Source 'Chennai' (City) and Departure Date 2026-08-11 ---");
        FlightSearchRequest request3 = new FlightSearchRequest();
        request3.setSource("Chennai");
        request3.setDepartureDate(LocalDate.of(2026, 8, 11));
        java.util.List<Flight> results3 = searchService.smartSearch(request3);
        System.out.println("Found " + results3.size() + " flights:");
        results3.forEach(System.out::println);

        // 4. Search with no matches
        System.out.println("\n--- 4. Search with non-existent source 'XYZ' ---");
        FlightSearchRequest request4 = new FlightSearchRequest();
        request4.setSource("XYZ");
        java.util.List<Flight> results4 = searchService.smartSearch(request4);
        System.out.println("Found " + results4.size() + " flights.");
        System.out.println("=============================================");

        // ===========================
        // UC18 - BUSINESS RULES DEMO
        // ===========================
        System.out.println("\n========== UC18 - BUSINESS RULES DEMO ==========");

        // Add demo flights for proximity tests
        Flight nearFlight = flightService.addFlight(
                "6E101",
                "IndiGo",
                aircraft2,
                route2,
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(2).plusHours(2),
                5000,
                10000,
                15000
        );

        Flight farFlight = flightService.addFlight(
                "6E303",
                "IndiGo",
                aircraft2,
                route2,
                LocalDateTime.now().plusDays(40),
                LocalDateTime.now().plusDays(40).plusHours(2),
                5000,
                10000,
                15000
        );

        // 1. Test Child Discount (Age: 8, child discount 15%)
        System.out.println("\n--- 1. Testing Child Discount (Age: 8, Expected: 15% discount) ---");
        Passenger childPassenger = new Passenger("P18_1", "Child Passenger", "child@gmail.com", "9999911111", "pass123");
        childPassenger.setAge(8);
        Booking childBooking = bookingService.bookFlight(childPassenger, flightService.searchFlight("6E205"), TravelClass.ECONOMY, 1);
        if (childBooking != null) {
            System.out.println("Child Booking Total Fare: ₹" + childBooking.getTotalFare() + " (Base: ₹3900.0)");
        }

        // 2. Test Senior Discount (Age: 65, senior discount 10%)
        System.out.println("\n--- 2. Testing Senior Discount (Age: 65, Expected: 10% discount) ---");
        Passenger seniorPassenger = new Passenger("P18_2", "Senior Passenger", "senior@gmail.com", "9999922222", "pass123");
        seniorPassenger.setAge(65);
        Booking seniorBooking = bookingService.bookFlight(seniorPassenger, flightService.searchFlight("6E205"), TravelClass.ECONOMY, 1);
        if (seniorBooking != null) {
            System.out.println("Senior Booking Total Fare: ₹" + seniorBooking.getTotalFare() + " (Base: ₹3900.0)");
        }

        // 3. Test Proximity Surcharge (Booking within 2 days, Expected: 20% surcharge)
        System.out.println("\n--- 3. Testing Proximity Surcharge (Departing in 2 days, Expected: 20% surcharge) ---");
        Passenger regularPassenger = new Passenger("P18_3", "Regular Passenger", "regular@gmail.com", "9999933333", "pass123");
        regularPassenger.setAge(30); // No age discount
        Booking surchargeBooking = bookingService.bookFlight(regularPassenger, nearFlight, TravelClass.ECONOMY, 1);
        if (surchargeBooking != null) {
            System.out.println("Proximity Surcharge Booking Total Fare: ₹" + surchargeBooking.getTotalFare() + " (Base: ₹5000.0)");
        }

        // 4. Test Proximity Discount (Booking departing in 40 days, Expected: 10% discount)
        System.out.println("\n--- 4. Testing Proximity Discount (Departing in 40 days, Expected: 10% discount) ---");
        Booking discountBooking = bookingService.bookFlight(regularPassenger, farFlight, TravelClass.ECONOMY, 1);
        if (discountBooking != null) {
            System.out.println("Proximity Discount Booking Total Fare: ₹" + discountBooking.getTotalFare() + " (Base: ₹5000.0)");
        }

        // 5. Test Combined Child + Proximity Discount (Age: 8, 40 days advance, Expected: 15% then 10% discount)
        System.out.println("\n--- 5. Testing Combined Discount (Child + Proximity, Expected: ₹3825.0) ---");
        Booking combinedBooking = bookingService.bookFlight(childPassenger, farFlight, TravelClass.ECONOMY, 1);
        if (combinedBooking != null) {
            System.out.println("Combined Booking Total Fare: ₹" + combinedBooking.getTotalFare() + " (Base: ₹5000.0)");
        }

        // 6. Test Max Seat Limit failure (Attempt to book 7 seats)
        System.out.println("\n--- 6. Testing Max Seat Limit Failure (Attempting to book 7 seats) ---");
        try {
            Booking failedSeatBooking = bookingService.bookFlight(regularPassenger, farFlight, TravelClass.ECONOMY, 7);
            System.out.println("Booking reference returned: " + failedSeatBooking);
        } catch (airline.exception.SeatLimitException e) {
            System.out.println("Caught Expected Exception: " + e.getMessage());
        }

        // 7. Test Duplicate Booking failure
        System.out.println("\n--- 7. Testing Duplicate Booking Failure ---");
        System.out.println("Attempting to book the same flight (6E205) again for seniorPassenger...");
        try {
            Booking failedDuplicateBooking = bookingService.bookFlight(seniorPassenger, flightService.searchFlight("6E205"), TravelClass.ECONOMY, 1);
            System.out.println("Booking reference returned: " + failedDuplicateBooking);
        } catch (airline.exception.DuplicateBookingException e) {
            System.out.println("Caught Expected Exception: " + e.getMessage());
        }
        System.out.println("=================================================");

        // ===========================
        // UC19 - EXCEPTION HANDLING DEMO
        // ===========================
        System.out.println("\n========== UC19 - EXCEPTION HANDLING DEMO ==========");

        // 1. Trigger FlightNotFoundException
        System.out.println("\n--- 1. Triggering FlightNotFoundException (Search flight: AI999) ---");
        try {
            flightService.searchFlight("AI999");
        } catch (airline.exception.FlightNotFoundException e) {
            System.out.println("Caught Expected Exception: " + e.getMessage());
        }

        // 2. Trigger SeatLimitException
        System.out.println("\n--- 2. Triggering SeatLimitException (Attempting to book 10 seats) ---");
        try {
            bookingService.bookFlight(regularPassenger, farFlight, TravelClass.ECONOMY, 10);
        } catch (airline.exception.SeatLimitException e) {
            System.out.println("Caught Expected Exception: " + e.getMessage());
        }

        // 3. Trigger DuplicateBookingException
        System.out.println("\n--- 3. Triggering DuplicateBookingException ---");
        try {
            Passenger dupPassenger = new Passenger("P19_DUP", "Duplicate Tester", "dup@gmail.com", "9999944444", "pass123");
            bookingService.bookFlight(dupPassenger, farFlight, TravelClass.ECONOMY, 1);
            System.out.println("First booking successful. Attempting second booking for same flight & passenger...");
            bookingService.bookFlight(dupPassenger, farFlight, TravelClass.ECONOMY, 1);
        } catch (airline.exception.DuplicateBookingException e) {
            System.out.println("Caught Expected Exception: " + e.getMessage());
        }

        // 4. Trigger CheckInException (Check-in for cancelled booking)
        System.out.println("\n--- 4. Triggering CheckInException (Check-in on cancelled booking) ---");
        try {
            Passenger cancelPassenger = new Passenger("P19_CANCEL", "Cancelled Tester", "cancel@gmail.com", "9999955555", "pass123");
            Booking cancelBooking = bookingService.bookFlight(cancelPassenger, farFlight, TravelClass.ECONOMY, 1);
            bookingService.cancelBooking(cancelBooking.getBookingId());
            checkInService.performOnlineCheckIn(cancelBooking.getBookingId(), "12A", 1, true);
        } catch (airline.exception.CheckInException e) {
            System.out.println("Caught Expected Exception: " + e.getMessage());
        }
        System.out.println("====================================================");
    }
}