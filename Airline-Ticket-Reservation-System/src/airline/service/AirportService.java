package airline.service;

import airline.model.Airport;
import airline.repository.AirportRepository;
import airline.singleton.FlightManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AirportService {
    private final AirportRepository repository;

    public AirportService() {
        this.repository = FlightManager.getInstance().getAirportRepository();
    }

    public AirportService(AirportRepository repository) {
        this.repository = repository;
    }

    public Airport addAirport(String code, String name, String city, String country, String timezone, String contactDetails) {
        Airport airport = new Airport(code, name, city, country, timezone, contactDetails);
        repository.save(airport);
        return airport;
    }

    public Airport searchByCode(String code) {
        return repository.findAirport(code);
    }

    public List<Airport> searchByCity(String city) {
        if (city == null) return new ArrayList<>();
        return repository.findAll().stream()
                .filter(a -> a.getCity().equalsIgnoreCase(city))
                .collect(Collectors.toList());
    }

    public List<Airport> searchByName(String name) {
        if (name == null) return new ArrayList<>();
        return repository.findAll().stream()
                .filter(a -> a.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Airport> listAirportsByCountry(String country) {
        if (country == null) return new ArrayList<>();
        return repository.findAll().stream()
                .filter(a -> a.getCountry().equalsIgnoreCase(country))
                .collect(Collectors.toList());
    }

    public List<Airport> getAutoSuggestions(String query) {
        if (query == null || query.isBlank()) {
            return new ArrayList<>();
        }
        String lowerQuery = query.toLowerCase();
        return repository.findAll().stream()
                .filter(a -> a.isActive() && (
                        a.getCode().toLowerCase().contains(lowerQuery) ||
                        a.getCity().toLowerCase().contains(lowerQuery) ||
                        a.getName().toLowerCase().contains(lowerQuery)
                ))
                .collect(Collectors.toList());
    }

    public void updateStatus(String code, boolean active) {
        Airport airport = repository.findAirport(code);
        if (airport != null) {
            airport.setActive(active);
            repository.save(airport);
        }
    }

    public void updateFacilities(String code, List<String> facilities) {
        Airport airport = repository.findAirport(code);
        if (airport != null) {
            airport.setFacilities(facilities);
            repository.save(airport);
        }
    }

    public void updateTerminals(String code, List<String> terminals) {
        Airport airport = repository.findAirport(code);
        if (airport != null) {
            airport.setTerminals(terminals);
            repository.save(airport);
        }
    }

    public void displayAirports() {
        System.out.println("\n========== AIRPORTS ==========");
        if (repository.count() == 0) {
            System.out.println("No Airports Registered.");
            return;
        }
        repository.findAll().forEach(System.out::println);
        System.out.println("==============================");
    }
}
