package airline.model;

public class Route {

    private String source;

    private String destination;

    public Route() {
    }

    public Route(String source,
                 String destination) {

        this.source = source;
        this.destination = destination;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Override
    public String toString() {

        return source + " -> " + destination;

    }

}