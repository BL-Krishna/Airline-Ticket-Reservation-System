package airline.service;

import airline.enums.FlightStatus;
import airline.factory.FlightFactory;
import airline.model.Aircraft;
import airline.model.Flight;
import airline.model.Route;
import airline.repository.FlightRepository;
import airline.singleton.FlightManager;

import java.time.LocalDateTime;

public class FlightService {

    private final FlightRepository repository;

    public FlightService() {
        this.repository = FlightManager.getInstance().getFlightRepository();
    }

    public FlightService(FlightRepository repository) {

        this.repository = repository;

    }

    public Flight addFlight(

            String flightNumber,

            String airlineName,

            Aircraft aircraft,

            Route route,

            LocalDateTime departure,

            LocalDateTime arrival,

            double economyFare,

            double businessFare,

            double firstClassFare) {

        if (repository.exists(flightNumber)) {

            throw new IllegalArgumentException(
                    "Flight already exists."
            );

        }

        Flight flight =

                FlightFactory.createFlight(

                        flightNumber,

                        airlineName,

                        aircraft,

                        route,

                        departure,

                        arrival,

                        economyFare,

                        businessFare,

                        firstClassFare

                );

        repository.save(flight);

        return flight;

    }

    public Flight searchFlight(String flightNumber) {
        Flight flight = repository.findByFlightNumber(flightNumber);
        if (flight == null) {
            throw new airline.exception.FlightNotFoundException("Flight not found with number: " + flightNumber);
        }
        return flight;
    }

    public void displayFlights() {

        System.out.println();

        System.out.println("========= FLIGHTS =========");

        repository.findAll()

                .forEach(System.out::println);

        System.out.println("---------------------------");

        System.out.println(
                "Total Flights : " +
                        repository.count()
        );

    }

    public void deleteFlight(String flightNumber) {

        repository.delete(flightNumber);

    }

    public void updateFlightStatus(

            String flightNumber,

            FlightStatus status) {

        Flight flight =

                repository.findByFlightNumber(
                        flightNumber
                );

        if (flight == null) {

            throw new IllegalArgumentException(
                    "Flight not found."
            );

        }

        flight.setStatus(status);

    }

    public void updateEconomyFare(

            String flightNumber,

            double fare) {

        Flight flight =
                repository.findByFlightNumber(
                        flightNumber
                );

        if (flight != null) {

            flight.setEconomyFare(fare);

        }

    }

    public void updateBusinessFare(

            String flightNumber,

            double fare) {

        Flight flight =
                repository.findByFlightNumber(
                        flightNumber
                );

        if (flight != null) {

            flight.setBusinessFare(fare);

        }

    }

    public void updateFirstClassFare(

            String flightNumber,

            double fare) {

        Flight flight =
                repository.findByFlightNumber(
                        flightNumber
                );

        if (flight != null) {

            flight.setFirstClassFare(fare);

        }

    }

}