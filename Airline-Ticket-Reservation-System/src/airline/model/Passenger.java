package airline.model;

import airline.enums.UserRole;

public class Passenger extends User {

    public Passenger() {

        super();

    }

    public Passenger(String userId,
                     String fullName,
                     String email,
                     String phoneNumber,
                     String password) {

        super(

                userId,

                fullName,

                email,

                phoneNumber,

                password,

                UserRole.PASSENGER

        );

    }

    private int age;
    private String gender;
    private String idProof;
    private String mealPreference;
    private String specialAssistance;
    private String passportNumber;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdProof() {
        return idProof;
    }

    public void setIdProof(String idProof) {
        this.idProof = idProof;
    }

    public String getMealPreference() {
        return mealPreference;
    }

    public void setMealPreference(String mealPreference) {
        this.mealPreference = mealPreference;
    }

    public String getSpecialAssistance() {
        return specialAssistance;
    }

    public void setSpecialAssistance(String specialAssistance) {
        this.specialAssistance = specialAssistance;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

}