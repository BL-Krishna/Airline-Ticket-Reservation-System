package airline.comparator;

import airline.model.Flight;

import java.time.Duration;
import java.util.Comparator;

public class DurationComparator
        implements Comparator<Flight> {

    @Override
    public int compare(Flight f1,
                       Flight f2) {

        Duration d1 = Duration.between(
                f1.getDepartureTime(),
                f1.getArrivalTime());

        Duration d2 = Duration.between(
                f2.getDepartureTime(),
                f2.getArrivalTime());

        return d1.compareTo(d2);

    }

}