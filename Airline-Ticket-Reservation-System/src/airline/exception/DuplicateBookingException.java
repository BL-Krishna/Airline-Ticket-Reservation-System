package airline.exception;

public class DuplicateBookingException extends AirlineException {
    public DuplicateBookingException(String message) {
        super(message);
    }
}
