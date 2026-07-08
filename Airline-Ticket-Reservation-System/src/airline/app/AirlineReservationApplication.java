package airline.app;

import airline.exception.DuplicateUserException;
import airline.exception.InvalidUserException;
import airline.model.User;
import airline.service.UserService;

public class AirlineReservationApplication {

    public static void main(String[] args) {

        UserService service =

                new UserService();

        try {

            User passenger1 =

                    service.registerPassenger(

                            "Krrish CH",

                            "krrish@gmail.com",

                            "9876543210",

                            "password123"

                    );

            User passenger2 =

                    service.registerPassenger(

                            "Rahul Sharma",

                            "rahul@gmail.com",

                            "9988776655",

                            "rahul123"

                    );

            User passenger3 =

                    service.registerPassenger(

                            "Priya Reddy",

                            "priya@gmail.com",

                            "9123456789",

                            "priya123"

                    );

            System.out.println();

            System.out.println("Passenger Registered Successfully");

            System.out.println("--------------------------------");

            System.out.println(passenger1);

            System.out.println(passenger2);

            System.out.println(passenger3);

            service.displayUsers();

        }

        catch (DuplicateUserException |

               InvalidUserException exception) {

            System.out.println();

            System.out.println(exception.getMessage());

        }

    }

}