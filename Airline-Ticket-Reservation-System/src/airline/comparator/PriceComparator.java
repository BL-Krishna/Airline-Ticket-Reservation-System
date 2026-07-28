package airline.comparator;

import airline.model.Flight;

import java.util.Comparator;

public class PriceComparator
        implements Comparator<Flight> {

    @Override
    public int compare(Flight o1,
                       Flight o2) {

        return Double.compare(
                o1.getEconomyFare(),
                o2.getEconomyFare()
        );

    }

}