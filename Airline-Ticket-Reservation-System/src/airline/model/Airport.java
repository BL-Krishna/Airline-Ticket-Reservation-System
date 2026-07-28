package airline.model;

import java.util.ArrayList;
import java.util.List;

public class Airport {
    private String code; // IATA Code (e.g., HYD, DEL, BOM)
    private String name;
    private String city;
    private String country;
    private String timezone;
    private List<String> terminals = new ArrayList<>();
    private boolean active = true;
    private List<String> facilities = new ArrayList<>();
    private String contactDetails;

    public Airport() {
    }

    public Airport(String code, String name, String city, String country, String timezone, String contactDetails) {
        this.code = code;
        this.name = name;
        this.city = city;
        this.country = country;
        this.timezone = timezone;
        this.contactDetails = contactDetails;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public List<String> getTerminals() {
        return terminals;
    }

    public void setTerminals(List<String> terminals) {
        this.terminals = terminals;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<String> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<String> facilities) {
        this.facilities = facilities;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    @Override
    public String toString() {
        return "Airport{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", timezone='" + timezone + '\'' +
                ", active=" + active +
                ", terminals=" + terminals +
                ", facilities=" + facilities +
                ", contactDetails='" + contactDetails + '\'' +
                '}';
    }
}
