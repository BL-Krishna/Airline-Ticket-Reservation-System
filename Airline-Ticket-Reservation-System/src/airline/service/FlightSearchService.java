package airline.service;

import airline.model.Flight;
import airline.repository.FlightRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

public class FlightSearchService {
    private final FlightRepository flightRepository;

    public FlightSearchService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<Flight> searchBySource(String source) {
        return flightRepository.findBySource(source);
    }

    public List<Flight> searchByDestination(String destination) {
        return flightRepository.findByDestination(destination);
    }

    public List<Flight> searchByRoute(String source, String destination) {
        return flightRepository.findByRoute(source, destination);
    }

    public List<Flight> searchByAirline(String airline) {
        return flightRepository.findByAirline(airline);
    }

    public List<Flight> searchByDate(LocalDate date) {
        return flightRepository.findByDate(date);
    }

    public Flight getCheapestFlight() {
        return flightRepository.findAll().stream()
                .min(Comparator.comparingDouble(Flight::getEconomyFare))
                .orElse(null);
    }

    public List<Flight> sortByFare() {
        return flightRepository.findAll().stream()
                .sorted(Comparator.comparingDouble(Flight::getEconomyFare))
                .collect(Collectors.toList());
    }

    public List<Flight> sortByDepartureTime() {
        return flightRepository.findAll().stream()
                .sorted(Comparator.comparing(Flight::getDepartureTime))
                .collect(Collectors.toList());
    }
}
