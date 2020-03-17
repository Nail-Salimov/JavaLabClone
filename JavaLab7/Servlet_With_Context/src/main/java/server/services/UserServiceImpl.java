package server.services;

import context.Component;
import server.dto.StateDto;
import server.dto.UserDto;
import server.model.User;
import server.repositories.UserRepository;

import java.util.Optional;

public class UserServiceImpl implements UserService, Component {

    private UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    public UserServiceImpl() {
    }

    @Override
    public UserDto getUser(int id) {
        Optional<User> optionalUser = repository.findClientById(id);

        return optionalUser.map(UserDto::from).orElse(null);
    }

    @Override
    public UserDto addClient(String name, String password) {
        Optional<User> optionalUser = repository.addClient(name, password);
        return optionalUser.map(UserDto::from).orElse(null);
    }

    @Override
    public StateDto checkName(String name) {
        if (repository.clientIsExist(name)) {
            return new StateDto("имя занята");
        }
        return new StateDto("имя подходит");
    }

    @Override
    public UserDto checkClient(String name, String password) {
        Optional<User> optionalUser = repository.findClient(name, password);
        return optionalUser.map(UserDto::from).orElse(null);
    }

    public UserRepository getRepository() {
        return repository;
    }

    public void setRepository(UserRepository repository) {
        this.repository = repository;
    }
}
