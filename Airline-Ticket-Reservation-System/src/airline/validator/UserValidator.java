package airline.validator;

import airline.exception.InvalidUserException;

public class UserValidator {

    private UserValidator() {
    }

    public static void validateName(String name) {

        if (name == null || name.trim().isEmpty()) {

            throw new InvalidUserException(
                    "Name cannot be empty."
            );

        }

    }

    public static void validateEmail(String email) {

        if (email == null ||
                !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

            throw new InvalidUserException(
                    "Invalid email address."
            );

        }

    }

    public static void validatePhone(String phone) {

        if (phone == null ||
                !phone.matches("\\d{10}")) {

            throw new InvalidUserException(
                    "Phone number must contain exactly 10 digits."
            );

        }

    }

    public static void validatePassword(String password) {

        if (password == null ||
                password.length() < 8) {

            throw new InvalidUserException(
                    "Password must contain at least 8 characters."
            );

        }

    }

}