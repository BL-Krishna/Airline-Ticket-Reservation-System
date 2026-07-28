package airline.app;

import airline.enums.FlightStatus;
import airline.model.Aircraft;
import airline.model.Route;
import airline.repository.FlightRepository;
import airline.service.FlightService;

import java.time.LocalDateTime;

public class AirlineReservationApplication {

    public static void main(String[] args) {

        FlightRepository flightRepository =
                new FlightRepository();

        FlightService flightService =
                new FlightService(flightRepository);

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

        flightService.addFlight(
                "AI101",
                "Air India",
                aircraft1,
                route1,
                LocalDateTime.of(2026,8,10,8,0),
                LocalDateTime.of(2026,8,10,10,30),
                4500,
                8500,
                14500
        );

        flightService.addFlight(
                "6E205",
                "IndiGo",
                aircraft2,
                route2,
                LocalDateTime.of(2026,8,11,13,45),
                LocalDateTime.of(2026,8,11,15,55),
                3900,
                7200,
                12000
        );

        flightService.addFlight(
                "UK777",
                "Vistara",
                aircraft1,
                route3,
                LocalDateTime.of(2026,8,12,6,15),
                LocalDateTime.of(2026,8,12,9,20),
                5200,
                9800,
                16500
        );

        System.out.println();
        System.out.println("Flights Added Successfully");

        flightService.displayFlights();

        System.out.println();
        System.out.println("Searching Flight AI101");

        System.out.println(
                flightService.searchFlight("AI101")
        );

        System.out.println();

        System.out.println(
                "Updating Flight Status..."
        );

        flightService.updateFlightStatus(
                "AI101",
                FlightStatus.DELAYED
        );

        System.out.println(
                flightService.searchFlight("AI101")
        );

        System.out.println();

        System.out.println(
                "Updating Economy Fare..."
        );

        flightService.updateEconomyFare(
                "AI101",
                5000
        );

        System.out.println(
                flightService.searchFlight("AI101")
        );

        System.out.println();

        System.out.println(
                "Deleting Flight UK777..."
        );

        flightService.deleteFlight("UK777");

        flightService.displayFlights();

    }
    flightService.displayFlights();

System.out.println("\n===============================");
System.out.println("UC5 FLIGHT SEARCH");
System.out.println("===============================");

System.out.println("\nSearch By Source");

searchService.searchBySource("Hyderabad")
        .forEach(System.out::println);

System.out.println("\nSearch By Destination");

searchService.searchByDestination("Mumbai")
        .forEach(System.out::println);

System.out.println("\nSearch By Route");

searchService.searchByRoute(
        "Hyderabad",
        "Delhi")
        .forEach(System.out::println);

System.out.println("\nSearch By Airline");

searchService.searchByAirline("Air India")
        .forEach(System.out::println);

System.out.println("\nSearch By Date");

searchService.searchByDate(

        LocalDate.of(2026,8,10)

        ).forEach(System.out::println);

System.out.println("\nCheapest Flight");

System.out.println(

        searchService.getCheapestFlight()

        );

System.out.println("\nFlights Sorted By Fare");

searchService.sortByFare()

        .forEach(System.out::println);

System.out.println("\nFlights Sorted By Departure");

searchService.sortByDepartureTime()

        .forEach(System.out::println);

System.out.println("\nFlights Grouped By Airline");

searchService.groupFlightsByAirline()

        .forEach((airline,list)->{

        System.out.println("\n"+airline);

        list.forEach(System.out::println);

    });


}