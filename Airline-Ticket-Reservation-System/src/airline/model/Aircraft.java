package airline.model;

public class Aircraft {

    private String aircraftId;

    private String aircraftName;

    private String aircraftType;

    private int economySeats;

    private int businessSeats;

    private int firstClassSeats;

    public Aircraft() {
    }

    public Aircraft(String aircraftId,
                    String aircraftName,
                    String aircraftType,
                    int economySeats,
                    int businessSeats,
                    int firstClassSeats) {

        this.aircraftId = aircraftId;
        this.aircraftName = aircraftName;
        this.aircraftType = aircraftType;
        this.economySeats = economySeats;
        this.businessSeats = businessSeats;
        this.firstClassSeats = firstClassSeats;
    }

    public String getAircraftId() {
        return aircraftId;
    }

    public void setAircraftId(String aircraftId) {
        this.aircraftId = aircraftId;
    }

    public String getAircraftName() {
        return aircraftName;
    }

    public void setAircraftName(String aircraftName) {
        this.aircraftName = aircraftName;
    }

    public String getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(String aircraftType) {
        this.aircraftType = aircraftType;
    }

    public int getEconomySeats() {
        return economySeats;
    }

    public void setEconomySeats(int economySeats) {
        this.economySeats = economySeats;
    }

    public int getBusinessSeats() {
        return businessSeats;
    }

    public void setBusinessSeats(int businessSeats) {
        this.businessSeats = businessSeats;
    }

    public int getFirstClassSeats() {
        return firstClassSeats;
    }

    public void setFirstClassSeats(int firstClassSeats) {
        this.firstClassSeats = firstClassSeats;
    }

    @Override
    public String toString() {
        return "Aircraft{" +
                "aircraftId='" + aircraftId + '\'' +
                ", aircraftName='" + aircraftName + '\'' +
                ", aircraftType='" + aircraftType + '\'' +
                ", economySeats=" + economySeats +
                ", businessSeats=" + businessSeats +
                ", firstClassSeats=" + firstClassSeats +
                '}';
    }
}