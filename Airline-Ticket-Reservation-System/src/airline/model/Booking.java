package airline.model;

import airline.enums.BookingStatus;
import airline.enums.TravelClass;

import java.time.LocalDateTime;

public class Booking {

    private String bookingId;

    private Passenger passenger;

    private Flight flight;

    private TravelClass travelClass;

    private int numberOfSeats;

    private double totalFare;

    private LocalDateTime bookingTime;

    private BookingStatus bookingStatus;

    public Booking() {
    }

    public Booking(String bookingId,
                   Passenger passenger,
                   Flight flight,
                   TravelClass travelClass,
                   int numberOfSeats,
                   double totalFare,
                   LocalDateTime bookingTime,
                   BookingStatus bookingStatus) {

        this.bookingId = bookingId;
        this.passenger = passenger;
        this.flight = flight;
        this.travelClass = travelClass;
        this.numberOfSeats = numberOfSeats;
        this.totalFare = totalFare;
        this.bookingTime = bookingTime;
        this.bookingStatus = bookingStatus;
    }

    public String getBookingId() {
        return bookingId;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public Flight getFlight() {
        return flight;
    }

    public TravelClass getTravelClass() {
        return travelClass;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public double getTotalFare() {
        return totalFare;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    @Override
    public String toString() {

        return "\nBooking ID : " + bookingId +

                "\nPassenger : " + passenger.getName() +

                "\nFlight : " + flight.getFlightNumber() +

                "\nRoute : " +

                flight.getRoute().getSource()

                + " -> " +

                flight.getRoute().getDestination()

                +

                "\nClass : " + travelClass +

                "\nSeats : " + numberOfSeats +

                "\nTotal Fare : " + totalFare +

                "\nStatus : " + bookingStatus +

                "\nBooking Time : " + bookingTime;

    }

}