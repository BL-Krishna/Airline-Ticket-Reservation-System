package airline.model;

public class Route {

    private String source;
    private String destination;
    private Airport sourceAirport;
    private Airport destinationAirport;

    public Route() {
    }

    public Route(String source,
                 String destination) {
        this.source = source;
        this.destination = destination;
    }

    public Route(Airport sourceAirport,
                 Airport destinationAirport) {
        this.sourceAirport = sourceAirport;
        this.destinationAirport = destinationAirport;
        this.source = sourceAirport != null ? sourceAirport.getCity() : null;
        this.destination = destinationAirport != null ? destinationAirport.getCity() : null;
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

    public Airport getSourceAirport() {
        return sourceAirport;
    }

    public void setSourceAirport(Airport sourceAirport) {
        this.sourceAirport = sourceAirport;
        if (sourceAirport != null) {
            this.source = sourceAirport.getCity();
        }
    }

    public Airport getDestinationAirport() {
        return destinationAirport;
    }

    public void setDestinationAirport(Airport destinationAirport) {
        this.destinationAirport = destinationAirport;
        if (destinationAirport != null) {
            this.destination = destinationAirport.getCity();
        }
    }

    @Override
    public String toString() {
        String srcVal = sourceAirport != null ? sourceAirport.getName() + " (" + source + ")" : source;
        String destVal = destinationAirport != null ? destinationAirport.getName() + " (" + destination + ")" : destination;
        return srcVal + " -> " + destVal;
    }

}