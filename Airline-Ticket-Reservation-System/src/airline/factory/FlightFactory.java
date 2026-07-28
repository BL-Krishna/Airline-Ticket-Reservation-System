package airline.factory;

import airline.enums.FlightStatus;
import airline.model.Aircraft;
import airline.model.Flight;
import airline.model.Route;

import java.time.LocalDateTime;

public class FlightFactory {

    private FlightFactory() {
    }

    public static Flight createFlight(
            String flightNumber,
            String airlineName,
            Aircraft aircraft,
            Route route,
            LocalDateTime departure,
            LocalDateTime arrival,
            double economyFare,
            double businessFare,
            double firstClassFare) {

        return new Flight(
                flightNumber,
                airlineName,
                aircraft,
                route,
                departure,
                arrival,
                economyFare,
                businessFare,
                firstClassFare,
                FlightStatus.SCHEDULED
        );

    }

}