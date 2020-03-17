package server.repositories;

import server.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findClient(String name, String password);
    Optional<User> findClientById(int id);
    Optional<User> addClient(String name, String password);
    boolean clientIsExist(String name);
}
