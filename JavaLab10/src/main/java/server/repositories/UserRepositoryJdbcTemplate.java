package server.repositories;

import server.entities.model.User;

import java.util.Optional;

public interface UserRepositoryJdbcTemplate extends  CrudRepository<Long, User> {
    Optional<User> findByUniqueData(String name, String password);
    Optional<User> findByNameAndEmailNotConfirmed(String name, String email);
    Optional<User> findByUsername(String name);
    Optional<User> findByUsernameNotConfirmed(String name);
    Optional<User> findByEmail(String email);

    void confirmUser(String email);
    void deleteWithNameOrEmail(String name, String email);
    void addToken(String username, String token);
}
