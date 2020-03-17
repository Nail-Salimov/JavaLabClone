package server.repositories;

import server.models.User;

import java.util.Optional;

public interface GeneralRepository extends CrudRepository<Long, String> {
    boolean checkClient(Long id, String password);
    Optional<User> getUser(Long id);
}
