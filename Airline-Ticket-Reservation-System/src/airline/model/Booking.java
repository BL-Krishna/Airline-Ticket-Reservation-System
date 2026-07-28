package airline.model;

import airline.enums.BookingStatus;
import airline.enums.TravelClass;

import java.time.LocalDateTime;

public class Booking {

    private String bookingId;

    private Passenger passenger;

    private Flight flight;

    private TravelClass travelClass;

    private Seat seat;

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
                   Seat seat,
                   int numberOfSeats,
                   double totalFare,
                   LocalDateTime bookingTime,
                   BookingStatus bookingStatus) {

        this.bookingId = bookingId;
        this.passenger = passenger;
        this.flight = flight;
        this.travelClass = travelClass;
        this.seat = seat;
        this.numberOfSeats = numberOfSeats;
        this.totalFare = totalFare;
        this.bookingTime = bookingTime;
        this.bookingStatus = bookingStatus;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public TravelClass getTravelClass() {
        return travelClass;
    }

    public void setTravelClass(TravelClass travelClass) {
        this.travelClass = travelClass;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public double getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(double totalFare) {
        this.totalFare = totalFare;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    @Override
    public String toString() {

        return "\n===============================" +
                "\nBooking ID      : " + bookingId +
                "\nPassenger       : " + passenger.getName() +
                "\nFlight Number   : " + flight.getFlightNumber() +
                "\nAirline         : " + flight.getAirlineName() +
                "\nRoute           : "
                + flight.getRoute().getSource()
                + " -> "
                + flight.getRoute().getDestination() +
                "\nTravel Class    : " + travelClass +
                "\nSeat Number     : " + seat.getSeatNumber() +
                "\nSeats Booked    : " + numberOfSeats +
                "\nTotal Fare      : " + totalFare +
                "\nBooking Time    : " + bookingTime +
                "\nBooking Status  : " + bookingStatus +
                "\n===============================";
    }
}