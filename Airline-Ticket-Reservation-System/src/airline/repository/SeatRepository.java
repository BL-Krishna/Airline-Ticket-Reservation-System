package airline.repository;

import airline.model.Seat;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SeatRepository {

    private final Map<String, Seat> seats =
            new HashMap<>();

    public void save(Seat seat){

        seats.put(

                seat.getSeatNumber(),

                seat

        );

    }

    public Seat findSeat(String seatNo){

        return seats.get(seatNo);

    }

    public Collection<Seat> findAll(){

        return seats.values();

    }

    public boolean exists(String seatNo){

        return seats.containsKey(seatNo);

    }

}