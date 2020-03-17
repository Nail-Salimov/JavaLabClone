package server.repositories;

import server.models.User;

import java.util.Optional;

public interface GeneralRepository extends CrudRepository<Long, String> {
    boolean checkClient(int id, String password);
    Optional<User> getUser(int id);
}
