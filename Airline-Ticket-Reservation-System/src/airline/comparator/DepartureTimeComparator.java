package airline.comparator;

import airline.model.Flight;

import java.util.Comparator;

public class DepartureTimeComparator
        implements Comparator<Flight> {

    @Override
    public int compare(Flight o1,
                       Flight o2) {

        return o1.getDepartureTime()
                .compareTo(
                        o2.getDepartureTime()
                );

    }

}