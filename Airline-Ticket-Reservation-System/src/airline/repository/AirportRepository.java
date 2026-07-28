package airline.repository;

import airline.model.Airport;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AirportRepository {
    private final Map<String, Airport> airports = new HashMap<>();

    public void save(Airport airport) {
        if (airport != null && airport.getCode() != null) {
            airports.put(airport.getCode().toUpperCase(), airport);
        }
    }

    public Airport findAirport(String code) {
        if (code == null) return null;
        return airports.get(code.toUpperCase());
    }

    public Collection<Airport> findAll() {
        return airports.values();
    }

    public void delete(String code) {
        if (code != null) {
            airports.remove(code.toUpperCase());
        }
    }

    public boolean exists(String code) {
        if (code == null) return false;
        return airports.containsKey(code.toUpperCase());
    }

    public int count() {
        return airports.size();
    }
}
