package airline.service;

import airline.enums.UserRole;
import airline.exception.DuplicateUserException;
import airline.model.Passenger;
import airline.model.User;
import airline.repository.UserRepository;
import airline.validator.UserValidator;

import java.util.Collection;

public class UserService {

    private final UserRepository repository =
            new UserRepository();

    private static int userCounter = 1001;

    public UserService(UserRepository repository) {
    }

    public User registerPassenger(String name,
                                  String email,
                                  String phone,
                                  String password) {

        UserValidator.validateName(name);
        UserValidator.validateEmail(email);
        UserValidator.validatePhone(phone);
        UserValidator.validatePassword(password);

        if (repository.existsByEmail(email)) {

            throw new DuplicateUserException(
                    "User already registered with email : " + email
            );

        }

        String userId = "U" + userCounter++;

        Passenger passenger =

                new Passenger(

                        userId,

                        name,

                        email,

                        phone,

                        password

                );

        repository.save(passenger);

        return passenger;

    }

    public Collection<User> getAllUsers() {

        return repository.findAll();

    }

    public User findUser(String userId) {

        return repository.findById(userId);

    }

    public int totalUsers() {

        return repository.count();

    }

    public void displayUsers() {

        System.out.println();

        System.out.println("=========== REGISTERED USERS ===========");

        repository.findAll()

                .forEach(System.out::println);

        System.out.println("----------------------------------------");

        System.out.println("Total Users : " + repository.count());

    }
    public User registerAdmin(String name,
                              String email,
                              String phone,
                              String password) {

        UserValidator.validateName(name);
        UserValidator.validateEmail(email);
        UserValidator.validatePhone(phone);
        UserValidator.validatePassword(password);

        if (repository.existsByEmail(email)) {

            throw new DuplicateUserException(
                    "User already exists."
            );

        }

        String userId = "U" + userCounter++;

        User admin =

                new User(
                        userId,
                        name,
                        email,
                        phone,
                        password,
                        UserRole.ADMIN
                );

        repository.save(admin);

        return admin;

    }
    userService.registerAdmin(
            "Super Admin",
            "admin@airline.com",
            "9999999999",
            "admin123"
            );

    userService.registerPassenger(
        "Rahul",
        "rahul@gmail.com",
        "8888888888",
        "password123"
        );

    userService.registerPassenger(
        "Krrish",
        "krrish@gmail.com",
        "7777777777",
        "password123"
        );
    roleService.promoteToAdmin("U1002");

    System.out.println();

    System.out.println("===== ADMINS =====");

    roleService.getAdmins()
        .forEach(System.out::println);
        System.out.println();

        System.out.println("===== PASSENGERS =====");

        roleService.getPassengers()
                .forEach(System.out::println);

}