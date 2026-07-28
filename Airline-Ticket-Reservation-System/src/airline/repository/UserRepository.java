package airline.repository;

import airline.enums.UserRole;
import airline.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UserRepository {

    private final Map<String, User> users =
            new HashMap<>();

    public void save(User user) {

        users.put(

                user.getUserId(),

                user

        );

    }

    public User findById(String userId) {

        return users.get(userId);

    }

    public User findByEmail(String email) {

        for (User user : users.values()) {

            if (user.getEmail()

                    .equalsIgnoreCase(email)) {

                return user;

            }

        }

        return null;

    }

    public boolean existsByEmail(String email) {

        return findByEmail(email) != null;

    }

    public Collection<User> findAll() {

        return users.values();

    }

    public int count() {

        return users.size();

    }
    public long countAdmins() {

        return users.values()
                .stream()
                .filter(user -> user.getRole() == UserRole.ADMIN)
                .count();

    }

    public Collection<User> findByRole(UserRole role) {

        return users.values()
                .stream()
                .filter(user -> user.getRole() == role)
                .toList();

    }

}