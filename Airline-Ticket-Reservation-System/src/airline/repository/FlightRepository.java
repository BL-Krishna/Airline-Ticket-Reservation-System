package airline.repository;

import airline.model.Flight;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

}