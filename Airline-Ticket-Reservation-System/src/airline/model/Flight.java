package airline.model;

import airline.enums.FlightStatus;

import java.time.LocalDateTime;

public class Flight {

    private String flightNumber;

    private String airlineName;

    private Aircraft aircraft;

    private Route route;

    private LocalDateTime departureTime;

    private LocalDateTime arrivalTime;

    private double economyFare;

    private double businessFare;

    private double firstClassFare;

    private FlightStatus status;

    public Flight() {
    }

    public Flight(String flightNumber,
                  String airlineName,
                  Aircraft aircraft,
                  Route route,
                  LocalDateTime departureTime,
                  LocalDateTime arrivalTime,
                  double economyFare,
                  double businessFare,
                  double firstClassFare,
                  FlightStatus status) {

        this.flightNumber = flightNumber;
        this.airlineName = airlineName;
        this.aircraft = aircraft;
        this.route = route;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.economyFare = economyFare;
        this.businessFare = businessFare;
        this.firstClassFare = firstClassFare;
        this.status = status;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public double getEconomyFare() {
        return economyFare;
    }

    public void setEconomyFare(double economyFare) {
        this.economyFare = economyFare;
    }

    public double getBusinessFare() {
        return businessFare;
    }

    public void setBusinessFare(double businessFare) {
        this.businessFare = businessFare;
    }

    public double getFirstClassFare() {
        return firstClassFare;
    }

    public void setFirstClassFare(double firstClassFare) {
        this.firstClassFare = firstClassFare;
    }

    public FlightStatus getStatus() {
        return status;
    }

    public void setStatus(FlightStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {

        return "Flight{" +
                "flightNumber='" + flightNumber + '\'' +
                ", airlineName='" + airlineName + '\'' +
                ", aircraft=" + aircraft.getAircraftName() +
                ", route=" + route +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                ", economyFare=" + economyFare +
                ", businessFare=" + businessFare +
                ", firstClassFare=" + firstClassFare +
                ", status=" + status +
                '}';
    }
}