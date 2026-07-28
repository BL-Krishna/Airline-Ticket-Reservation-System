package airline.singleton;

import airline.enums.FlightStatus;
import airline.enums.SeatStatus;
import airline.model.Aircraft;
import airline.model.Flight;
import airline.model.Route;
import airline.model.Seat;
import airline.repository.FlightRepository;
import airline.repository.SeatRepository;
import airline.repository.AirportRepository;
import airline.factory.FlightFactory;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FlightManager {
    private static volatile FlightManager instance;
    private final FlightRepository flightRepository;
    private final SeatRepository seatRepository;
    private final AirportRepository airportRepository;
    private final Map<String, Flight> flightCache = new ConcurrentHashMap<>();

    private FlightManager() {
        this.flightRepository = new FlightRepository();
        this.seatRepository = new SeatRepository();
        this.airportRepository = new AirportRepository();
    }

    public static FlightManager getInstance() {
        if (instance == null) {
            synchronized (FlightManager.class) {
                if (instance == null) {
                    instance = new FlightManager();
                }
            }
        }
        return instance;
    }

    public FlightRepository getFlightRepository() {
        return flightRepository;
    }

    public SeatRepository getSeatRepository() {
        return seatRepository;
    }

    public AirportRepository getAirportRepository() {
        return airportRepository;
    }

    public synchronized Flight addFlight(
            String flightNumber,
            String airlineName,
            Aircraft aircraft,
            Route route,
            LocalDateTime departure,
            LocalDateTime arrival,
            double economyFare,
            double businessFare,
            double firstClassFare) {

        if (flightRepository.exists(flightNumber)) {
            throw new IllegalArgumentException("Flight already exists.");
        }

        Flight flight = FlightFactory.createFlight(
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

        flightRepository.save(flight);
        flightCache.put(flightNumber, flight);
        return flight;
    }

    public Flight searchFlight(String flightNumber) {
        if (flightNumber == null) return null;
        return flightCache.computeIfAbsent(flightNumber, flightRepository::findByFlightNumber);
    }

    public Collection<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    public void displayFlights() {
        System.out.println();
        System.out.println("========= FLIGHTS =========");
        flightRepository.findAll().forEach(System.out::println);
        System.out.println("---------------------------");
        System.out.println("Total Flights : " + flightRepository.count());
    }

    public synchronized void deleteFlight(String flightNumber) {
        flightRepository.delete(flightNumber);
        flightCache.remove(flightNumber);
    }

    public synchronized void updateFlightStatus(String flightNumber, FlightStatus status) {
        Flight flight = searchFlight(flightNumber);
        if (flight == null) {
            throw new IllegalArgumentException("Flight not found.");
        }
        flight.setStatus(status);
        flightRepository.save(flight);
        flightCache.put(flightNumber, flight);
    }

    public synchronized void updateEconomyFare(String flightNumber, double fare) {
        Flight flight = searchFlight(flightNumber);
        if (flight != null) {
            flight.setEconomyFare(fare);
            flightRepository.save(flight);
            flightCache.put(flightNumber, flight);
        }
    }

    public synchronized void updateBusinessFare(String flightNumber, double fare) {
        Flight flight = searchFlight(flightNumber);
        if (flight != null) {
            flight.setBusinessFare(fare);
            flightRepository.save(flight);
            flightCache.put(flightNumber, flight);
        }
    }

    public synchronized void updateFirstClassFare(String flightNumber, double fare) {
        Flight flight = searchFlight(flightNumber);
        if (flight != null) {
            flight.setFirstClassFare(fare);
            flightRepository.save(flight);
            flightCache.put(flightNumber, flight);
        }
    }

    public synchronized boolean allocateSeat(String flightNumber, String seatNumber) {
        Seat seat = seatRepository.findSeat(seatNumber);
        if (seat != null && seat.getSeatStatus() == SeatStatus.AVAILABLE) {
            seat.setSeatStatus(SeatStatus.RESERVED);
            return true;
        }
        return false;
    }

    public synchronized boolean releaseSeat(String flightNumber, String seatNumber) {
        Seat seat = seatRepository.findSeat(seatNumber);
        if (seat != null) {
            seat.setSeatStatus(SeatStatus.AVAILABLE);
            return true;
        }
        return false;
    }
}
