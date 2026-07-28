package airline.service;

import airline.enums.UserRole;
import airline.model.User;
import airline.repository.UserRepository;

import java.util.Collection;

public class RoleService {

    private final UserRepository repository;

    public RoleService(UserRepository repository) {

        this.repository = repository;

    }

    public void promoteToAdmin(String userId) {

        User user = repository.findById(userId);

        if (user == null) {

            throw new IllegalArgumentException("User not found.");

        }

        user.setRole(UserRole.ADMIN);

    }

    public void demoteToPassenger(String userId) {

        User user = repository.findById(userId);

        if (user == null) {

            throw new IllegalArgumentException("User not found.");

        }

        if (repository.countAdmins() == 1 &&
                user.getRole() == UserRole.ADMIN) {

            throw new IllegalStateException(
                    "Cannot remove the last admin."
            );

        }

        user.setRole(UserRole.PASSENGER);

    }

    public Collection<User> getAdmins() {

        return repository.findByRole(UserRole.ADMIN);

    }

    public Collection<User> getPassengers() {

        return repository.findByRole(UserRole.PASSENGER);

    }

    public boolean isAdmin(String userId) {

        User user = repository.findById(userId);

        return user != null &&
                user.getRole() == UserRole.ADMIN;

    }

}