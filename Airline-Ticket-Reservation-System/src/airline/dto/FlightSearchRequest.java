package airline.dto;

import java.time.LocalDate;

public class FlightSearchRequest {

    private String source;

    private String destination;

    private LocalDate departureDate;

    private String airline;

    private double minimumFare;

    private double maximumFare;

    public FlightSearchRequest() {
    }

    public FlightSearchRequest(String source,
                               String destination,
                               LocalDate departureDate,
                               String airline,
                               double minimumFare,
                               double maximumFare) {

        this.source = source;
        this.destination = destination;
        this.departureDate = departureDate;
        this.airline = airline;
        this.minimumFare = minimumFare;
        this.maximumFare = maximumFare;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public double getMinimumFare() {
        return minimumFare;
    }

    public void setMinimumFare(double minimumFare) {
        this.minimumFare = minimumFare;
    }

    public double getMaximumFare() {
        return maximumFare;
    }

    public void setMaximumFare(double maximumFare) {
        this.maximumFare = maximumFare;
    }
}