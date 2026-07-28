package airline.model;

import airline.enums.BookingPriority;

public class BookingRequest implements Comparable<BookingRequest> {
    private final Booking booking;
    private final BookingPriority priority;
    private final long timestamp; // Timestamp when request entered queue

    public BookingRequest(Booking booking, BookingPriority priority) {
        this.booking = booking;
        this.priority = priority;
        this.timestamp = System.currentTimeMillis();
    }

    public Booking getBooking() {
        return booking;
    }

    public BookingPriority getPriority() {
        return priority;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public int compareTo(BookingRequest other) {
        // Aging threshold: if a request is in queue for more than 10 seconds, it gets high priority to avoid starvation.
        long agingThresholdMs = 10000; 
        long now = System.currentTimeMillis();
        
        long ageSelf = now - this.timestamp;
        long ageOther = now - other.timestamp;

        boolean selfAgedOut = ageSelf > agingThresholdMs;
        boolean otherAgedOut = ageOther > agingThresholdMs;

        if (selfAgedOut && !otherAgedOut) {
            return -1; // Prioritize self
        } else if (!selfAgedOut && otherAgedOut) {
            return 1; // Prioritize other
        }

        // Standard comparison based on priority
        if (this.priority != other.priority) {
            return this.priority == BookingPriority.EXPRESS ? -1 : 1;
        }

        // Same priority: First-In-First-Out (FIFO)
        return Long.compare(this.timestamp, other.timestamp);
    }

    @Override
    public String toString() {
        return "BookingRequest{" +
                "bookingId='" + booking.getBookingId() + '\'' +
                ", priority=" + priority +
                ", timestamp=" + timestamp +
                '}';
    }
}
