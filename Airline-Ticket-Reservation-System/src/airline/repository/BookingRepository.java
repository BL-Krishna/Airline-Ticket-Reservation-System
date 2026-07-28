package airline.repository;

import airline.model.Booking;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BookingRepository {

    private final Map<String, Booking> bookings =
            new HashMap<>();

    public void save(Booking booking){

        bookings.put(

                booking.getBookingId(),

                booking

        );

    }

    public Booking findBooking(String bookingId){

        return bookings.get(bookingId);

    }

    public Collection<Booking> findAll(){

        return bookings.values();

    }

    public void delete(String bookingId){

        bookings.remove(bookingId);

    }

    public boolean exists(String bookingId){

        return bookings.containsKey(bookingId);

    }

    public int count(){

        return bookings.size();

    }

}