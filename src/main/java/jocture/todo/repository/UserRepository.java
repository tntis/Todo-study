package jocture.todo.repository;

import jocture.todo.entity.User;

import java.util.Optional;

public interface UserRepository {
    void save(User user);

    Optional<User> findByEmail(String email);

    Boolean existByEmail(String email);

    Optional<User> findByEmailAndPassword(String email, String password);

}
