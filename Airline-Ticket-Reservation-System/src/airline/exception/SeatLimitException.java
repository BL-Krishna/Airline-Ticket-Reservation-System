package airline.exception;

public class SeatLimitException extends AirlineException {
    public SeatLimitException(String message) {
        super(message);
    }
}
