package airline.service;

import airline.dto.FlightSearchRequest;
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

    public List<Flight> smartSearch(FlightSearchRequest request) {
        if (request == null) {
            return new java.util.ArrayList<>(flightRepository.findAll());
        }
        return flightRepository.findAll().stream()
                .filter(flight -> {
                    // 1. Source check: Match city code, city name, or airport name (case-insensitive)
                    if (request.getSource() != null && !request.getSource().isBlank()) {
                        if (flight.getRoute() == null) return false;
                        String query = request.getSource().trim().toLowerCase();
                        boolean matchCity = flight.getRoute().getSource() != null && flight.getRoute().getSource().toLowerCase().contains(query);
                        boolean matchCode = flight.getRoute().getSourceAirport() != null && flight.getRoute().getSourceAirport().getCode() != null && flight.getRoute().getSourceAirport().getCode().toLowerCase().contains(query);
                        boolean matchName = flight.getRoute().getSourceAirport() != null && flight.getRoute().getSourceAirport().getName() != null && flight.getRoute().getSourceAirport().getName().toLowerCase().contains(query);
                        if (!matchCity && !matchCode && !matchName) return false;
                    }
                    // 2. Destination check: Match city code, city name, or airport name (case-insensitive)
                    if (request.getDestination() != null && !request.getDestination().isBlank()) {
                        if (flight.getRoute() == null) return false;
                        String query = request.getDestination().trim().toLowerCase();
                        boolean matchCity = flight.getRoute().getDestination() != null && flight.getRoute().getDestination().toLowerCase().contains(query);
                        boolean matchCode = flight.getRoute().getDestinationAirport() != null && flight.getRoute().getDestinationAirport().getCode() != null && flight.getRoute().getDestinationAirport().getCode().toLowerCase().contains(query);
                        boolean matchName = flight.getRoute().getDestinationAirport() != null && flight.getRoute().getDestinationAirport().getName() != null && flight.getRoute().getDestinationAirport().getName().toLowerCase().contains(query);
                        if (!matchCity && !matchCode && !matchName) return false;
                    }
                    // 3. Departure Date check: Match the date portion of departureTime
                    if (request.getDepartureDate() != null) {
                        if (flight.getDepartureTime() == null || !flight.getDepartureTime().toLocalDate().isEqual(request.getDepartureDate())) {
                            return false;
                        }
                    }
                    // 4. Airline check: Match airline name case-insensitively
                    if (request.getAirline() != null && !request.getAirline().isBlank()) {
                        if (flight.getAirlineName() == null || !flight.getAirlineName().toLowerCase().contains(request.getAirline().trim().toLowerCase())) {
                            return false;
                        }
                    }
                    // 5. Minimum Fare check: Compare economyFare
                    if (request.getMinimumFare() > 0) {
                        if (flight.getEconomyFare() < request.getMinimumFare()) {
                            return false;
                        }
                    }
                    // 6. Maximum Fare check: Compare economyFare
                    if (request.getMaximumFare() > 0) {
                        if (flight.getEconomyFare() > request.getMaximumFare()) {
                            return false;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }
}
