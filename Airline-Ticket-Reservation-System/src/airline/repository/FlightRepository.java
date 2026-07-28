package airline.repository;

import airline.model.Flight;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class FlightRepository {

    private final Map<String, Flight> flights =
            new HashMap<>();

    public void save(Flight flight) {

        flights.put(
                flight.getFlightNumber(),
                flight
        );

    }

    public Flight findByFlightNumber(String flightNumber) {

        return flights.get(flightNumber);

    }

    public Collection<Flight> findAll() {

        return flights.values();

    }

    public boolean exists(String flightNumber) {

        return flights.containsKey(flightNumber);

    }

    public void delete(String flightNumber) {

        flights.remove(flightNumber);

    }

    public int count() {

        return flights.size();

    }

    // ===========================
    // UC5 SEARCH METHODS
    // ===========================

    public List<Flight> findBySource(String source){

        return flights.values()

                .stream()

                .filter(f ->
                        f.getRoute()
                                .getSource()
                                .equalsIgnoreCase(source))

                .collect(Collectors.toList());

    }

    public List<Flight> findByDestination(String destination){

        return flights.values()

                .stream()

                .filter(f ->
                        f.getRoute()
                                .getDestination()
                                .equalsIgnoreCase(destination))

                .collect(Collectors.toList());

    }

    public List<Flight> findByRoute(String source,
                                    String destination){

        return flights.values()

                .stream()

                .filter(f->

                        f.getRoute()
                                .getSource()
                                .equalsIgnoreCase(source)

                                &&

                                f.getRoute()
                                        .getDestination()
                                        .equalsIgnoreCase(destination)

                )

                .collect(Collectors.toList());

    }

    public List<Flight> findByAirline(String airline){

        return flights.values()

                .stream()

                .filter(f->

                        f.getAirlineName()
                                .equalsIgnoreCase(airline)

                )

                .collect(Collectors.toList());

    }

    public List<Flight> findByDate(LocalDate date){

        return flights.values()

                .stream()

                .filter(f->

                        f.getDepartureTime()

                                .toLocalDate()

                                .equals(date)

                )

                .collect(Collectors.toList());

    }

}