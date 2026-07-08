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

}