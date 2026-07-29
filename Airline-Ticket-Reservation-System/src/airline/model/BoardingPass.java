package airline.model;

import java.time.LocalDateTime;

public class BoardingPass {
    private final String boardingPassId;
    private final Booking booking;
    private final String seatNumber;
    private final String gate;
    private final LocalDateTime boardingTime;
    private final int baggageCount;

    public BoardingPass(String boardingPassId, Booking booking, String seatNumber, String gate, LocalDateTime boardingTime, int baggageCount) {
        this.boardingPassId = boardingPassId;
        this.booking = booking;
        this.seatNumber = seatNumber;
        this.gate = gate;
        this.boardingTime = boardingTime;
        this.baggageCount = baggageCount;
    }

    public String getBoardingPassId() {
        return boardingPassId;
    }

    public Booking getBooking() {
        return booking;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public String getGate() {
        return gate;
    }

    public LocalDateTime getBoardingTime() {
        return boardingTime;
    }

    public int getBaggageCount() {
        return baggageCount;
    }

    @Override
    public String toString() {
        return "\n=============================================" +
                "\n                BOARDING PASS" +
                "\n=============================================" +
                "\nBoarding Pass ID : " + boardingPassId +
                "\nPassenger Name   : " + booking.getPassenger().getName() +
                "\nFlight Number    : " + booking.getFlight().getFlightNumber() +
                "\nRoute            : " + booking.getFlight().getRoute() +
                "\nClass            : " + booking.getTravelClass() +
                "\nSeat Number      : " + seatNumber +
                "\nGate             : " + gate +
                "\nBoarding Time    : " + boardingTime +
                "\nBaggage Count    : " + baggageCount +
                "\n=============================================";
    }
}
